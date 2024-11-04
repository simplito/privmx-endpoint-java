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

/**
 * Base class for Store file streams. Implements progress listeners.
 *
 * @category store
 */
public abstract class StoreFileStream {
    private Long processedBytes = 0L;
    private ProgressListener progressListener;
    private Boolean closed = false;

    /**
     * Reference to file handle.
     */
    protected final Long handle;

    /**
     * Reference to {@link StoreApi}.
     */
    protected final StoreApi storeApi;

    /**
     * Constant value with optimal size of reading/sending data.
     */
    public static final long OPTIMAL_SEND_SIZE = 128 * 1024L;

    /**
     * Creates instance of {@code StoreFileStream}.
     * @param handle handle to Store file
     * @param storeApi {@link StoreApi} instance that calls read/write methods on files
     */
    protected StoreFileStream(
            Long handle,
            StoreApi storeApi
    ) {
        this.handle = handle;
        this.storeApi = storeApi;
    }

    /**
     * Sets listening for single chunk sent/read.
     * @param progressListener callback triggered when chunk is sent/read
     */
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * Increases the size of current sent/read data by chunkSize and calls {@link ProgressListener#onChunkProcessed(Long)}.
     * @param chunkSize size of processed chunk
     */
    protected void callChunkProcessed(Long chunkSize){
        processedBytes += chunkSize;
        if(progressListener != null){
            progressListener.onChunkProcessed(processedBytes);
        }
    }

    /**
     * Interface to listen to progress of sending/reading files.
     */
    public interface ProgressListener{
        /**
         * A callback called after each successful read/write operation.
         * @param processedBytes full size of current sent/read data
         */
        void onChunkProcessed(Long processedBytes);
    }

    /**
     * Returns information whether the instance is closed.
     *
     * @return {@code true} if file handle is closed
     */
    public Boolean isClosed(){return closed;}

    /**
     * Closes file handle.
     * @return ID of the closed file
     * @throws PrivmxException if there is an error while closing file
     * @throws NativeException if there is an unknown error while closing file
     * @throws IllegalStateException when {@code storeApi} is not initialized or there's no connection
     */
    public String close() throws PrivmxException, NativeException, IllegalStateException{
        closed = true;
        return (/*closedFileId =*/ storeApi.closeFile(handle));
    }

    /**
     * Manages sending/reading files using {@link java.io.InputStream}/{@link java.io.OutputStream}.
     */
    public static class Controller implements ProgressListener{
        private boolean isStopped = false;

        /**
         * Stops reading/writing file after processing the current chunk.
         */
        public final void stop(){
            isStopped = true;
        }

        /**
         * Returns information whether the stream should be stopped.
         * @return {@code true} if controller is set to stop
         */
        public final boolean isStopped(){
            return isStopped;
        }

        /**
         * Override this method to handle event when each chunk was sent successfully.
         *
         * @param processedBytes full size of current sent/read data
         */
        @Override
        public void onChunkProcessed(Long processedBytes) {

        }

        /**
         * Creates instance of Controller.
         */
        public Controller(){}
    }
}
