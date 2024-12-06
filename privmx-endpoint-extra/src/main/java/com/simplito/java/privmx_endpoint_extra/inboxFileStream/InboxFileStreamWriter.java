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
import java.io.InputStream;
import java.util.Arrays;

/**
 * Manages handle for file writing.
 *
 * @category inbox
 */
public class InboxFileStreamWriter extends InboxFileStream {

    private InboxFileStreamWriter(Long handle, InboxApi inboxApi) {
        super(handle, inboxApi);
    }

    /**
     * Creates a new file in given Inbox.
     *
     * @param api         reference to Inbox API
     * @param publicMeta  byte array of any arbitrary metadata that can be read by anyone
     * @param privateMeta byte array of any arbitrary metadata that will be encrypted before sending
     * @param size        size of data to write
     * @return Instance ready to write to the created Inbox file
     * @throws IllegalStateException when inboxApi is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while creating Inbox file metadata
     * @throws NativeException       if there is an unknown error while creating inbox file metadata
     */
    public static InboxFileStreamWriter createFile(
            InboxApi api,
            byte[] publicMeta,
            byte[] privateMeta,
            long size
    ) throws PrivmxException, NativeException, IllegalStateException {
        if (api == null) throw new NullPointerException("api cannot be null");
        return new InboxFileStreamWriter(
                api.createFileHandle(publicMeta, privateMeta, size),
                api
        );
    }

    /**
     * Writes data to Inbox file.
     *
     * @param data data to write (the recommended size of data chunk is {@link InboxFileStream#OPTIMAL_SEND_SIZE})
     * @throws PrivmxException       if there is an error while writing chunk
     * @throws NativeException       if there is an unknown error while writing chunk
     * @throws IllegalStateException when inboxApi is not initialized or there's no connection
     * @throws IOException           when {@code this} is closed
     * @throws PrivmxException       when method encounters an exception
     * @throws NativeException       when method encounters an unknown exception
     * @throws IllegalStateException when {@link #inboxApi} is closed
     */
    public void write(long inboxHandle, byte[] data) throws PrivmxException, NativeException, IllegalStateException, IOException {
        if (isClosed()) throw new IOException("File handle is closed");
        inboxApi.writeToFile(inboxHandle, handle, data);
        callChunkProcessed((long) data.length);
    }

    public void writeStream(long inboxHandle, InputStream stream) throws PrivmxException, NativeException, IllegalStateException, IOException {
        writeStream(inboxHandle, stream, null);
    }

    public void writeStream(long inboxHandle, InputStream inputStream, StoreFileStream.Controller streamController) throws PrivmxException, NativeException, IllegalStateException, IOException {
        if (streamController != null) {
            setProgressListener(streamController);
        }
        byte[] chunk = new byte[(int) InboxFileStream.OPTIMAL_SEND_SIZE];
        int readed;
        while (true) {
            if (streamController != null && streamController.isStopped()) {
                return;
            }
            if ((readed = inputStream.read(chunk)) <= 0) {
                return;
            }
            write(inboxHandle, Arrays.copyOf(chunk, readed));
        }
    }
}
