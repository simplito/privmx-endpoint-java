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

package com.simplito.java.privmx_endpoint_extra.inboxFileStream;

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.inbox.InboxApi;
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream;

import java.io.IOException;
import java.io.OutputStream;


/**
 * Manages handle for file reading from Inbox.
 *
 * @category inbox
 */
public class InboxFileStreamReader extends InboxFileStream {

    private InboxFileStreamReader(
            Long handle,
            InboxApi api
    ) {
        super(handle, api);
    }

    /**
     * Opens Inbox file.
     *
     * @param api    reference to Inbox API
     * @param fileId ID of the file to open
     * @return Instance ready to read from the Inbox file
     * @throws IllegalStateException when {@code inboxApi} is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while opening Inbox file
     * @throws NativeException       if there is an unknown error while opening Inbox file
     */
    public static InboxFileStreamReader openFile(
            InboxApi api,
            String fileId
    ) throws IllegalStateException, PrivmxException, NativeException {
        return new InboxFileStreamReader(
                api.openFile(fileId),
                api
        );
    }

    /**
     * Opens Inbox file and writes it into {@link OutputStream} with optimized chunk size {@link  InboxFileStream#OPTIMAL_SEND_SIZE}.
     *
     * @param api          reference to Inbox API
     * @param fileId       ID of the file to open
     * @param outputStream stream to write downloaded data
     * @return ID of the read file
     * @throws IOException           if there is an error while writing the stream
     * @throws IllegalStateException when inboxApi is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while opening Inbox file
     * @throws NativeException       if there is an unknown error while opening Inbox file
     */
    public static String openFile(
            InboxApi api,
            String fileId,
            OutputStream outputStream
    ) throws IOException, IllegalStateException, PrivmxException, NativeException {
        return InboxFileStreamReader.openFile(api, fileId, outputStream, null);
    }

    /**
     * Opens Inbox file and writes it into {@link OutputStream} with optimized chunk size {@link  InboxFileStream#OPTIMAL_SEND_SIZE}.
     *
     * @param api              reference to Inbox API
     * @param fileId           ID of the file to open
     * @param outputStream     stream to write downloaded data
     * @param streamController controls the process of reading file
     * @return ID of the read file
     * @throws IOException           if there is an error while writing stream
     * @throws IllegalStateException when inboxApi is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while reading Inbox file
     * @throws NativeException       if there is an unknown error while reading Inbox file
     */
    public static String openFile(
            InboxApi api,
            String fileId,
            OutputStream outputStream,
            StoreFileStream.Controller streamController
    ) throws IOException, IllegalStateException, PrivmxException, NativeException {
        InboxFileStreamReader input = InboxFileStreamReader.openFile(api, fileId);
        if (streamController != null) {
            input.setProgressListener(streamController);
        }
        byte[] chunk;
        do {
            if (streamController != null && streamController.isStopped()) {
                input.close();
            }
            chunk = input.read(InboxFileStream.OPTIMAL_SEND_SIZE);
            outputStream.write(chunk);
        } while (chunk.length == InboxFileStream.OPTIMAL_SEND_SIZE);

        return input.close();
    }

    /**
     * Reads file data and moves the cursor. If read data size is less than length, then EOF.
     *
     * @param size size of data to read (the recommended size is {@link  InboxFileStream#OPTIMAL_SEND_SIZE})
     * @return Read data
     * @throws IOException           when {@code this} is closed
     * @throws PrivmxException       when method encounters an exception
     * @throws NativeException       when method encounters an unknown exception
     * @throws IllegalStateException when {@link #inboxApi} is closed
     */
    public byte[] read(Long size) throws IOException, PrivmxException, NativeException, IllegalStateException {
        if (isClosed()) throw new IOException("File handle is closed");
        byte[] result = inboxApi.readFromFile(handle, size);
        callChunkProcessed((long) result.length);
        return result;
    }

    /**
     * Moves read cursor.
     *
     * @param position new cursor position
     * @throws IllegalStateException if {@code inboxApi} is not initialized or connected
     * @throws PrivmxException       if there is an error while seeking
     * @throws NativeException       if there is an unknown error while seeking
     */
    public void seek(long position) throws IllegalStateException, PrivmxException, NativeException {
        inboxApi.seekInFile(handle, position);
    }
}
