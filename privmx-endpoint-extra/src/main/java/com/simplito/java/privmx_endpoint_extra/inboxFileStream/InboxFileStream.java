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
import com.simplito.java.privmx_endpoint_extra.storeFileStream.StoreFileStream.ProgressListener;

/**
 * Base class for Inbox file streams.
 *
 * @category inbox
 */
public abstract class InboxFileStream {

    /**
     * Constant value with optimal size of reading/sending data.
     */
    public static final long OPTIMAL_SEND_SIZE = 128 * 1024L;

    /**
     * Reference to file handle.
     */
    protected final Long handle;

    /**
     * Reference to {@link com.simplito.java.privmx_endpoint.modules.inbox.InboxApi} instance.
     */
    protected final InboxApi inboxApi;

    private Long processedBytes = 0L;
    private StoreFileStream.ProgressListener progressListener;
    private Boolean closed = false;

    /**
     * Creates instance of {@link InboxFileStream}.
     *
     * @param handle   handle to Inbox file
     * @param inboxApi {@link InboxApi} instance that calls read/write methods on files
     */
    protected InboxFileStream(
            Long handle,
            InboxApi inboxApi
    ) {
        this.handle = handle;
        this.inboxApi = inboxApi;
    }

    /**
     * Gets current file handle.
     *
     * @return file handle.
     */
    public Long getFileHandle() {
        return handle;
    }

    /**
     * Sets listening for single chunk sent/read.
     *
     * @param progressListener callback triggered when chunk is sent/read
     */
    public void setProgressListener(ProgressListener progressListener) {
        this.progressListener = progressListener;
    }

    /**
     * Increases the size of current sent/read data by chunkSize and calls {@link StoreFileStream.ProgressListener#onChunkProcessed(Long)}.
     *
     * @param chunkSize size of processed chunk
     */
    protected void callChunkProcessed(Long chunkSize) {
        processedBytes += chunkSize;
        if (progressListener != null) {
            progressListener.onChunkProcessed(processedBytes);
        }
    }

    /**
     * Gets size of sent data.
     *
     * @return size of sent data.
     */
    public Long getProcessedBytes() {
        return this.processedBytes;
    }

    /**
     * Returns information whether the instance is closed.
     *
     * @return {@code true} if file handle is closed
     */
    public Boolean isClosed() {
        return closed;
    }

    /**
     * Closes file handle.
     *
     * @return ID of the closed file
     * @throws PrivmxException       if there is an error while closing file
     * @throws NativeException       if there is an unknown error while closing file
     * @throws IllegalStateException when {@link #inboxApi} is not initialized or there's no connection
     */
    public synchronized String close() throws PrivmxException, NativeException, IllegalStateException {
        String result = inboxApi.closeFile(handle);
        closed = true;
        return result;
    }
}
