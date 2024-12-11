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

/**
 * Provides a streamlined process for creating and sending inbox entries
 * with optional file attachments.
 * <p> This class simplifies interacting with the Inbox API for sending entries,
 * especially when dealing with multiple files. It manages the lifecycle of the entry creation
 * process, including file uploads and final entry submission.
 *
 * @category inbox
 */
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

    /**
     * Creates {@link InboxEntryStream} instance ready for streaming.
     * <p> This method initializes an {@link InboxEntryStream} and prepares it for sending
     * an entry with the provided data. It creates an Inbox handle and sets the
     * initial state of the stream to {@link State#FILES_SENT}
     *
     * @param inboxApi            reference to Inbox API
     * @param inboxId             ID of the Inbox
     * @param entryStreamListener the listener for stream state changes.
     * @param data                entry data to send
     * @return instance of {@link InboxEntryStream} prepared for streaming.
     */
    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data
    ) throws PrivmxException, NativeException, IllegalStateException {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, data, null);
    }

    /**
     * Creates {@link InboxEntryStream} instance ready for streaming.
     * <p>This method initializes an {@link InboxEntryStream} and prepares it for sending an entry with
     * associated files and empty data. It creates Inbox and file handles, setting the initial state of the stream
     * to {@link State#PREPARED}, indicating readiness for file transfer.
     *
     * @param inboxApi            reference to Inbox API
     * @param inboxId             ID of the Inbox
     * @param entryStreamListener the listener for stream state changes.
     * @param filesConfig         information about each entry's file to send.
     * @return instance of {@link InboxEntryStream} prepared for streaming.
     */
    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            List<FileInfo> filesConfig
    ) throws PrivmxException, NativeException, IllegalStateException {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, "".getBytes(StandardCharsets.UTF_8), filesConfig);
    }

    /**
     * Creates an {@link InboxEntryStream} instance ready for streaming, with optional files and encryption.
     * <p>This method initializes an {@link InboxEntryStream} and prepares it for sending an entry with
     * the provided data and optional associated files. It creates Inbox and file handles (if
     * {@code filesConfig} is provided), setting the initial state of the stream to
     * {@link State#PREPARED}, indicating readiness for data and file transfer.
     *
     * @param inboxApi            reference to Inbox API
     * @param inboxId             ID of the Inbox
     * @param entryStreamListener the listener for stream state changes
     * @param data                entry data to send
     * @param filesConfig         information about each entry's file to send.
     * @return instance of {@link InboxEntryStream} prepared for streaming.
     */
    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data,
            List<FileInfo> filesConfig
    ) throws PrivmxException, NativeException, IllegalStateException {
        return prepareEntry(inboxApi, inboxId, entryStreamListener, data, filesConfig, null);
    }

    /**
     * Creates an {@link InboxEntryStream} instance ready for streaming, with optional files and encryption.
     * <p>This method initializes an {@link InboxEntryStream} and prepares it for sending an entry with the provided data,
     * optional associated files, and optional encryption using the sender's private key. It creates an Inbox handle
     * and initializes file handles for any associated files. The initial state of the stream is determined based
     * on the presence of files: if no files are provided, the state is set to {@link State#FILES_SENT}; otherwise,
     * it's set to {@link State#PREPARED}, indicating readiness for file transfer.
     *
     * @param inboxApi            reference to Inbox API
     * @param inboxId             ID of the Inbox
     * @param entryStreamListener the listener for stream state changes
     * @param data                entry data to send
     * @param filesConfig         information about each entry's file to send
     * @param userPrivKey         sender's private key which can be used later to encrypt data for that sender
     * @return instance of {@link InboxEntryStream} prepared for streaming.
     */
    public static InboxEntryStream prepareEntry(
            InboxApi inboxApi,
            String inboxId,
            EntryStreamListener entryStreamListener,
            byte[] data,
            List<FileInfo> filesConfig,
            String userPrivKey
    ) throws PrivmxException, NativeException, IllegalStateException {
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

    /**
     * Initiates the process of sending files using the provided executor.
     * <p>This method submits each file for sending to the {@code fileStreamExecutor}
     * and wait for completion.
     *
     * @param fileStreamExecutor the executor service responsible for executing file sending tasks
     * @throws IllegalStateException If the stream is not in the {@link State#PREPARED} state.
     */
    public synchronized void sendFiles(
            ExecutorService fileStreamExecutor
    ) throws IllegalStateException {
        if (streamState != State.PREPARED) {
            throw new IllegalStateException("Stream should be in state PREPARED. Current state is: " + streamState.name());
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

    /**
     * Sends files using a single-threaded executor (see: {@link Executors#newSingleThreadExecutor}).
     *
     * @throws IllegalStateException when this stream is not in state {@link State#PREPARED}.
     */
    public void sendFiles() {
        try (ExecutorService executor = Executors.newSingleThreadExecutor()) {
            sendFiles(executor);
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

    private void onError(Throwable t) {
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

    /**
     * Cancels the stream and sets its state to {@link State#ABORTED}.
     * <p>If the stream is currently sending files, all pending file operations will be canceled.
     * <p>If the stream is in the process of sending the entry, this operation will not have any effect.
     */
    public void cancel() {
        if (streamState == State.ERROR) return;
        if (streamState == State.ABORTED) return;
        if (streamState == State.SENT) return;
        synchronized (this) {
            stopFileStreams();
            closeFileHandles();
            updateState(State.ABORTED);
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

    /**
     * Sends the entry data and closes this stream, transitioning it to the {@link State#SENT} state.
     * <p>This method should only be called after all files associated with the entry have been
     * successfully sent, indicated by the stream being in the {@link State#FILES_SENT} state.
     *
     * @throws PrivmxException       when method encounters an exception during call {@link InboxApi#sendEntry} method
     * @throws NativeException       when method encounters an unknown exception during call {@link InboxApi#sendEntry} method
     * @throws IllegalStateException if the stream is not in the {@link State#FILES_SENT} state
     */
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

    /**
     * Contains available states of {@link InboxEntryStream}.
     */
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

    /**
     * Represents information about a file to be sent by {@link InboxEntryStream}.
     */
    public static class FileInfo {

        /**
         * Byte array of any arbitrary metadata that can be read by anyone.
         */
        public byte[] publicMeta;

        /**
         * Byte array of any arbitrary metadata that will be encrypted before sending.
         */
        public byte[] privateMeta;

        /**
         * The total size of the file data.
         */
        public long fileSize;

        /**
         * An optional {@link InputStream} providing the file data.
         * <p>If this value is {@code null}, the stream will call
         * {@link EntryStreamListener#onNextChunkRequest} to request chunks of data
         * for sending.
         */
        public InputStream fileStream;

        /**
         * Creates instance of {@link FileInfo}.
         *
         * @param publicMeta  byte array of any arbitrary metadata that can be read by anyone
         * @param privateMeta byte array of any arbitrary metadata that will be encrypted before sending
         * @param fileSize    the total size of the file data
         * @param fileStream  reference to {@link InputStream} instance used as source for stream file
         */
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

    /**
     * Interface for listening to state changes and exchanging data with an {@link InboxEntryStream} instance.
     * <p>
     * This interface provides callbacks for various events that occur during the lifecycle of an inbox entry stream,
     * such as starting and ending file sending, requesting file chunks, handling errors, and updating the stream state.
     * <p>
     * Implement this interface to monitor and interact with the entry stream.
     */
    public abstract static class EntryStreamListener {

        /**
         * Override this method to handle when file start sending.
         *
         * @param file information about the file being sent
         */
        public void onStartFileSending(FileInfo file) {

        }

        /**
         * Override this method to handle when file has been send successfully.
         *
         * @param file information about the sent file
         */
        public void onEndFileSending(FileInfo file) {

        }

        /**
         * Override this method to handle event when {@link FileInfo#fileStream} is {@code null}
         * and the stream requests a chunk of the file to send.
         * <p>This method should return the next chunk of the file.
         * <p>Returning {@code null} will cause a
         * {@link NullPointerException} during file sending and stop the {@link InboxEntryStream} instance with
         * the state {@link State#ERROR}.
         *
         * @param file info about file for which chunk is requested
         * @return next chunk of file.
         */
        public byte[] onNextChunkRequest(FileInfo file) {
            return null;
        }

        /**
         * Override this method to handle event when each chunk of a file has been sent successfully.
         *
         * @param file           information about the file for which the chunk was processed
         * @param processedBytes accumulated size of sent data
         */
        public void onFileChunkProcessed(FileInfo file, long processedBytes) {

        }

        /**
         * Override this method to handle event when some error occurs during file sending.
         *
         * @param file      information about the file that caused the error
         * @param throwable exception that occurred during file sending
         */
        public void onErrorDuringSending(FileInfo file, Throwable throwable) {

        }

        /**
         * Override this method to handle event when some error occurs during creating entry.
         *
         * @param throwable exception that occurred during entry creation
         */
        public void onError(Throwable throwable) {

        }

        /**
         * Override this method to handle event when stream state has been updated.
         *
         * @param currentState current state of the stream
         */
        public void onUpdateState(State currentState) {

        }
    }
}
