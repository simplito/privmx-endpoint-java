//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information about a file deleted from Store.
 * @category core
 * @group Events
 */
public class StoreFileDeletedEventData {
    /**
     * ID of the deleted file.
     */
    public final String fileId;

    /**
     * ID of the Store's Context.
     */
    public final String contextId;

    /**
     * ID of the deleted file's Store.
     */
    public final String storeId;

    /**
     * Creates instance of {@code StoreFileDeletedEventData}.
     * @param fileId ID of the deleted file.
     * @param contextId ID of the Store's Context.
     * @param storeId ID of the Store of the deleted file.
     */
    public StoreFileDeletedEventData(
            String fileId,
            String contextId,
            String storeId
    ) {
        this.fileId = fileId;
        this.contextId = contextId;
        this.storeId = storeId;
    }
}
