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

package com.simplito.java.privmx_endpoint_extra.storeFileStream;

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.store.StoreApi;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Manages handle for file writing.
 *
 * @category store
 */
public class StoreFileStreamWriter extends StoreFileStream {

    private StoreFileStreamWriter(Long handle, StoreApi storeApi) {
        super(handle, storeApi);
    }

    /**
     * Creates a new file in given Store.
     *
     * @param api         reference to Store API
     * @param storeId     ID of the Store
     * @param publicMeta  byte array of any arbitrary metadata that can be read by anyone
     * @param privateMeta byte array of any arbitrary metadata that will be encrypted before sending
     * @param size        size of data to write
     * @return Instance ready to write to the created Store file
     * @throws IllegalStateException when storeApi is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while creating Store file metadata
     * @throws NativeException       if there is an unknown error while creating store file metadata
     */
    public static StoreFileStreamWriter createFile(
            StoreApi api,
            String storeId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size
    ) throws PrivmxException, NativeException, IllegalStateException {
        if (api == null) throw new NullPointerException("api could not be null");
        return new StoreFileStreamWriter(
                api.createFile(storeId, publicMeta, privateMeta, size),
                api
        );
    }


    /**
     * Updates an existing file.
     *
     * @param api         reference to Store API
     * @param fileId      ID of the file to update
     * @param publicMeta  new public metadata for the matching file
     * @param privateMeta new private (encrypted) metadata for the matching file
     * @param size        size of data to write
     * @return {@link StoreFileStreamWriter} instance prepared for writing
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while updating Store file metadata
     * @throws NativeException       if there is an unknown error while updating Store file metadata
     */
    public static StoreFileStreamWriter updateFile(
            StoreApi api,
            String fileId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size
    ) throws PrivmxException, NativeException, IllegalStateException {
        if (api == null) throw new NullPointerException("api could not be null");
        return new StoreFileStreamWriter(
                api.updateFile(fileId, publicMeta, privateMeta, size),
                api
        );
    }

    /**
     * Creates a new file in given Store and writes data from given {@link InputStream}.
     *
     * @param api         reference to Store API
     * @param storeId     ID of the Store
     * @param publicMeta  byte array of any arbitrary metadata that can be read by anyone
     * @param privateMeta byte array of any arbitrary metadata that will be encrypted before sending
     * @param size        size of data to write
     * @param inputStream stream with data to write to the file using optimal chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @return ID of the created file
     * @throws IOException           if there is an error while reading stream or {@code this} is closed
     * @throws IllegalStateException when storeApi is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while creating Store file metadata
     * @throws NativeException       if there is an unknown error while creating Store file metadata
     */
    public static String createFile(
            StoreApi api,
            String storeId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size,
            InputStream inputStream
    ) throws IOException, PrivmxException, NativeException, IllegalStateException {
        return StoreFileStreamWriter.createFile(api, storeId, publicMeta, privateMeta, size, inputStream, null);
    }

    /**
     * Creates new file in given Store and writes data from given {@link InputStream}.
     *
     * @param api              reference to Store API
     * @param storeId          ID of the Store
     * @param publicMeta       byte array of any arbitrary metadata that can be read by anyone
     * @param privateMeta      byte array of any arbitrary metadata that will be encrypted before sending
     * @param size             size of data to write
     * @param inputStream      stream with data to write to the file using optimal chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @param streamController controls the process of writing file
     * @return ID of the created file
     * @throws IOException           if there is an error while reading stream or {@code this} is closed
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while creating Store file metadata
     * @throws NativeException       if there is an unknown error while creating Store file metadata
     */
    public static String createFile(
            StoreApi api,
            String storeId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size,
            InputStream inputStream,
            Controller streamController
    ) throws IOException, PrivmxException, NativeException, IllegalStateException {
        if (api == null) throw new NullPointerException("api could not be null");
        StoreFileStreamWriter output = StoreFileStreamWriter.createFile(
                api,
                storeId,
                publicMeta,
                privateMeta,
                size
        );

        if (streamController != null) {
            output.setProgressListener(streamController);
        }
        byte[] chunk = new byte[(int) StoreFileStream.OPTIMAL_SEND_SIZE];
        int read;
        while ((read = inputStream.read(chunk)) >= 0) {
            if (streamController != null && streamController.isStopped()) {
                output.close();
            }
            output.write(Arrays.copyOf(chunk, read));
        }
        return output.close();
    }

    /**
     * Updates existing file and writes data from passed {@link InputStream}.
     *
     * @param api         reference to Store API
     * @param fileId      ID of the file to update
     * @param publicMeta  new public metadata for the matching file
     * @param privateMeta new private (encrypted) metadata for the matching file
     * @param size        size of data to write
     * @param inputStream stream with data to write to the file using optimal chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @return Updated file ID
     * @throws IOException           if there is an error while reading stream or {@code this} is closed
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while updating Store file metadata
     * @throws NativeException       if there is an unknown error while updating Store file metadata
     */
    public static String updateFile(
            StoreApi api,
            String fileId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size,
            InputStream inputStream
    ) throws IOException, PrivmxException, NativeException, IllegalStateException {
        return StoreFileStreamWriter.updateFile(api, fileId, publicMeta, privateMeta, size, inputStream, null);
    }

    /**
     * Updates existing file and writes data from passed {@link InputStream}.
     *
     * @param api              reference to Store API
     * @param fileId           ID of the file to update
     * @param publicMeta       new public metadata for the matching file
     * @param privateMeta      new private (encrypted) metadata for the matching file
     * @param size             size of data to write
     * @param inputStream      stream with data to write to the file using optimal chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @param streamController controls the process of writing file
     * @return Updated file ID
     * @throws IOException           if there is an error while reading stream or {@code this} is closed
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     * @throws PrivmxException       if there is an error while updating Store file metadata
     * @throws NativeException       if there is an unknown error while updating Store file metadata
     */
    public static String updateFile(
            StoreApi api,
            String fileId,
            byte[] publicMeta,
            byte[] privateMeta,
            long size,
            InputStream inputStream,
            Controller streamController
    ) throws IOException, PrivmxException, NativeException, IllegalStateException {
        if (api == null) throw new NullPointerException("api could not be null");
        StoreFileStreamWriter output = StoreFileStreamWriter.updateFile(api, fileId, publicMeta, privateMeta, size);
        if (streamController != null) {
            output.setProgressListener(streamController);
        }
        byte[] chunk = new byte[(int) StoreFileStream.OPTIMAL_SEND_SIZE];
        int read;
        while (true) {
            if (streamController != null && streamController.isStopped()) {
                output.close();
            }
            if ((read = inputStream.read(chunk)) <= 0) {
                break;
            }
            output.write(Arrays.copyOf(chunk, read));
        }
        return output.close();
    }


    /**
     * Writes data to Store file.
     *
     * @param data data to write (the recommended size of data chunk is {@link StoreFileStream#OPTIMAL_SEND_SIZE})
     * @throws PrivmxException       if there is an error while writing chunk
     * @throws NativeException       if there is an unknown error while writing chunk
     * @throws IllegalStateException when storeApi is not initialized or there's no connection
     * @throws IOException           when {@code this} is closed
     * @throws PrivmxException       when method encounters an exception
     * @throws NativeException       when method encounters an unknown exception
     * @throws IllegalStateException when {@link #storeApi} is closed
     */
    public void write(byte[] data) throws PrivmxException, NativeException, IllegalStateException, IOException {
        if (isClosed()) throw new IOException("File handle is closed");
        storeApi.writeToFile(handle, data);
        callChunkProcessed((long) data.length);
    }
}
