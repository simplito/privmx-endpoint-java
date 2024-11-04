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
import java.io.OutputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * Manages handle for file reading.
 *
 * @category store
 */
public class StoreFileStreamReader extends StoreFileStream {


    private StoreFileStreamReader(
            Long handle,
            StoreApi api
    ) {
        super(handle, api);
    }

    /**
     * Opens Store file.
     *
     * @param api reference to Store API
     * @param fileId ID of the file to open
     * @return Instance ready to read from the Store file
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     * @throws PrivmxException if there is an error while opening Store file
     * @throws NativeException if there is an unknown error while opening Store file
     */
    public static StoreFileStreamReader openFile(
            StoreApi api,
            String fileId
    ) throws IllegalStateException, PrivmxException, NativeException {
        return new StoreFileStreamReader(
                api.openFile(fileId),
                api
        );
    }

    /**
     * Opens Store file and writes it into {@link OutputStream}.
     *
     * @param api reference to Store API
     * @param fileId ID of the file to open
     * @param outputStream stream to write downloaded data with optimized chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @return ID of the read file
     * @throws IOException if there is an error while writing the stream
     * @throws IllegalStateException when storeApi is not initialized or there's no connection
     * @throws PrivmxException if there is an error while opening Store file
     * @throws NativeException if there is an unknown error while opening Store file
     */
    public static String openFile(
            StoreApi api,
            String fileId,
            OutputStream outputStream
    ) throws IOException, IllegalStateException, PrivmxException, NativeException {
        return StoreFileStreamReader.openFile(api, fileId, outputStream, null);
    }

    /**
     * Opens Store file and writes it into {@link OutputStream}.
     *
     * @param api reference to Store API
     * @param fileId ID of the file to open
     * @param outputStream stream to write downloaded data with optimized chunk size {@link  StoreFileStream#OPTIMAL_SEND_SIZE}
     * @param streamController controls the process of reading file
     * @return ID of the read file
     * @throws IOException if there is an error while writing stream
     * @throws IllegalStateException when storeApi is not initialized or there's no connection
     * @throws PrivmxException if there is an error while reading Store file
     * @throws NativeException if there is an unknown error while reading Store file
     */
    public static String openFile(
            StoreApi api,
            String fileId,
            OutputStream outputStream,
            Controller streamController
    ) throws IOException, IllegalStateException, PrivmxException, NativeException {
        StoreFileStreamReader input = StoreFileStreamReader.openFile(api,fileId);
        if (streamController != null) {
            input.setProgressListener(streamController);
        }
        byte[] chunk;
        do {
            if (streamController != null && streamController.isStopped()) {
                input.close();
            }
            chunk = input.read(StoreFileStream.OPTIMAL_SEND_SIZE);
            outputStream.write(chunk);
        } while (chunk.length == StoreFileStream.OPTIMAL_SEND_SIZE);

        return input.close();
    }

    /**
     * Reads file data and moves the cursor. If read data size is less than length, then EOF.
     *
     * @param size size of data to read (the recommended size is {@link  StoreFileStream#OPTIMAL_SEND_SIZE})
     * @return Read data
     * @throws IOException when {@code this} is closed
     * @throws PrivmxException when method encounters an exception
     * @throws NativeException when method encounters an unknown exception
     * @throws IllegalStateException when {@link #storeApi} is closed    
     */
    public byte[] read(Long size) throws IOException, PrivmxException, NativeException, IllegalStateException {
        if (isClosed()) throw new IOException("File handle is closed");
        byte[] result = storeApi.readFromFile(handle, size);
        callChunkProcessed((long) result.length);
        return result;
    }

    /**
     * Moves read cursor.
     *
     * @param position new cursor position
     * @throws IllegalStateException if {@code storeApi} is not initialized or connected
     * @throws PrivmxException if there is an error while seeking
     * @throws NativeException if there is an unknown error while seeking
     */
    public void seek(long position) throws IllegalStateException, PrivmxException, NativeException {
        storeApi.seekInFile(handle,position);
        ((ThreadPoolExecutor) Executors.newSingleThreadExecutor()).getQueue().clear();
    }
}
