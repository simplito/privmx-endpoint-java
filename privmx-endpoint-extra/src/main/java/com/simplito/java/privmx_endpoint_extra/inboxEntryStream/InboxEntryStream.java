//
// PrivMX Endpoint Java Extra.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.inboxEntryStream;

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.inbox.InboxApi;
import com.simplito.java.privmx_endpoint_extra.inboxFileStream.InboxFileStreamWriter;
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class InboxEntryStream {
    private final InboxApi inboxApi;
    private final Map<FileInfo, InboxFileStreamWriter> inboxFiles;
    private final long inboxHandle;
    private final EntryStreamListener entryStreamListener;
    private final List<Future<?>> sendingFiles = new ArrayList<>();
    private State streamState = State.PREPARED;

    private InboxEntryStream(InboxApi api, Map<FileInfo, InboxFileStreamWriter> inboxFiles, long inboxHandle, EntryStreamListener entryStreamListener) {
        Objects.requireNonNull(inboxFiles);
        Objects.requireNonNull(entryStreamListener);
        this.inboxApi = api;
        this.inboxFiles = inboxFiles;
        this.inboxHandle = inboxHandle;
        this.entryStreamListener = entryStreamListener;
        if (inboxFiles.isEmpty()) {
            streamState = State.FILES_SENT;
        }
    }

    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data
    ) {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, data, null);
    }

    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            List<FileInfo> filesConfig
    ) {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, "".getBytes(StandardCharsets.UTF_8), filesConfig);
    }

    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data,
            List<FileInfo> filesConfig
    ) {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, data, filesConfig, null);
    }

    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data,
            List<FileInfo> filesConfig,
            String userPrivKey
    ) {
        Map<FileInfo, InboxFileStreamWriter> files = Optional.ofNullable(filesConfig)
                .orElse(Collections.emptyList())
                .stream()
                .collect(
                        Collectors.toMap(
                                fileInfo -> fileInfo,
                                config -> InboxFileStreamWriter.createFile(
                                        inboxApi,
                                        config.publicMeta,
                                        config.privateMeta,
                                        config.fileSize
                                )
                        )
                );
        List<Long> fileHandles = files.values().stream().map(InboxFileStreamWriter::getFileHandle).collect(Collectors.toList());
        Long inboxHandle = inboxApi.prepareEntry(inboxId, data, fileHandles, userPrivKey);
        return new InboxEntryStream(inboxApi, files, inboxHandle, entryStreamListener);
    }

    public synchronized void sendFiles(
            ExecutorService fileStreamExecutor
    ) {
        if (streamState != State.PREPARED) {
            throw new IllegalStateException("Stream should be in state PREPARED. Current state is: " + streamState.name());
        }
        if (!sendingFiles.isEmpty()) {
            throw new IllegalStateException("Uploading files in progress");
        }
        inboxFiles.forEach((fileInfo, fileHandle) -> {
            sendingFiles.add(fileStreamExecutor.submit(() -> {
                try {
                    sendFile(fileInfo, fileHandle);
                    entryStreamListener.onEndFileSending(fileInfo);
                } catch (Exception e) {
                    stopFileStreams();
                    onError(e);
                    entryStreamListener.onErrorDuringSending(fileInfo, e);
                }
            }));
        });
        for (Future<?> future : sendingFiles) {
            if (Thread.interrupted()) {
                cancel();
                return;
            }
            try {
                future.get();
            } catch (InterruptedException | CancellationException e) {
                // catch when in async mode someone call cancel on result Future.
                cancel();
                return;
            } catch (Exception ignore) {
            }
        }
        if (sendingFiles.stream().allMatch(Future::isDone)) {
            updateState(State.FILES_SENT);
        } else {
            onError(new IllegalStateException("Some files cannot be sent"));
        }

    }

    private void sendFile(
            FileInfo fileInfo,
            InboxFileStreamWriter fileHandle
    ) throws PrivmxException, NativeException, IllegalStateException, IOException {
        final StoreFileStream.Controller controller = new StoreFileStream.Controller() {
            @Override
            public void onChunkProcessed(Long processedBytes) {
                entryStreamListener.onFileChunkProcessed(fileInfo, processedBytes);
                if (Thread.interrupted()) {
                    this.stop();
                }
            }
        };

        entryStreamListener.onStartFileSending(fileInfo);
        if (fileInfo.fileStream == null) {
            fileHandle.setProgressListener(controller);
            while (fileInfo.fileSize > fileHandle.getProcessedBytes() && !fileHandle.isClosed()) {
                if (controller.isStopped()) {
                    break;
                }
                byte[] nextChunk = entryStreamListener.onNextChunkRequest(fileInfo);
                if (nextChunk == null) {
                    throw new NullPointerException("Data chunk cannot be null");
                }
                fileHandle.write(inboxHandle, nextChunk);
            }
        } else {
            fileHandle.writeStream(inboxHandle, fileInfo.fileStream, controller);
        }
    }

    public void onError(Throwable t) {
        cancel();
        stopFileStreams();
        closeFileHandles();
        updateState(State.ERROR);
        entryStreamListener.onError(t);
    }

    private void updateState(State newState) {
        if (streamState != newState) {
            synchronized (this) {
                streamState = newState;
                entryStreamListener.onUpdateState(streamState);
            }
        }
    }

    public void cancel() {
        if (streamState == State.ERROR) return;
        if (streamState == State.ABORTED) return;
        synchronized (this) {
            stopFileStreams();
            closeFileHandles();
            if (streamState != State.SENT) {
                updateState(State.ABORTED);
            }
        }
    }

    private void stopFileStreams() {
        if (streamState == State.PREPARED && !sendingFiles.isEmpty()) {
            synchronized (sendingFiles) {
                sendingFiles.forEach((task) -> {
                    task.cancel(true);
                });
            }
        }
    }

    private void closeFileHandles() {
        synchronized (inboxFiles) {
            inboxFiles.values().forEach(file -> {
                try {
                    if (!file.isClosed()) {
                        file.close();
                    }
                } catch (Exception ignore) {
                }
            });
        }
    }

    public void sendFiles() {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            sendFiles(executor);
        }
    }

    public synchronized void sendEntry() throws PrivmxException, NativeException, IllegalStateException {
        if (streamState != State.FILES_SENT) {
            throw new IllegalStateException("Stream should be in state FILES_SENT. Current state is: " + streamState.name());
        }
        try {
            inboxApi.sendEntry(inboxHandle);
            updateState(State.SENT);
        } catch (Exception e) {
            onError(e);
        }
    }

    public enum State {
        /**
         * The initial state, indicating that {@link InboxEntryStream} is ready to send files.
         */
        PREPARED,
        /**
         * Indicates that all files have been sent successfully and the entry is ready to be sent.
         * This state is set when:
         * 1. The {@link InboxEntryStream} has been initialized and there are no files to send.
         * 2. All files have been sent successfully.
         */
        FILES_SENT,
        /**
         * Indicates that an error occurred during the process of sending files or the Entry.
         */
        ERROR,
        /**
         * Indicates that the entry has been sent successfully.
         */
        SENT,
        /**
         * Indicates that the {@link InboxEntryStream} has been canceled.
         */
        ABORTED
    }

    public static class FileInfo {
        public byte[] publicMeta;
        public byte[] privateMeta;
        public long fileSize;
        public InputStream fileStream;

        public FileInfo(
                byte[] publicMeta,
                byte[] privateMeta,
                long fileSize,
                InputStream fileStream
        ) {
            this.publicMeta = publicMeta;
            this.privateMeta = privateMeta;
            this.fileSize = fileSize;
            this.fileStream = fileStream;
        }
    }

    public abstract static class EntryStreamListener {
        public void onStartFileSending(FileInfo file) {
        }

        public void onEndFileSending(FileInfo file) {

        }

        public byte[] onNextChunkRequest(FileInfo file) {
            return null;
        }

        public void onFileChunkProcessed(FileInfo file, long chunk) {

        }

        public void onErrorDuringSending(FileInfo file, Throwable throwable) {

        }

        public void onError(Throwable t) {

        }

        public void onUpdateState(State currentState) {

        }
    }
}
