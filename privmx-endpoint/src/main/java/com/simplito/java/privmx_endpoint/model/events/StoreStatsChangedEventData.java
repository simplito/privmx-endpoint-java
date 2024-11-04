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
 * Holds information about changes in a Store's statistics.
 * @category core
 * @group Events
 */
public class StoreStatsChangedEventData {
    /**
     * ID of the changed Store's Context.
     */
    public final String contextId;

    /**
     * ID of the changed Store.
     */
    public final String storeId;

    /**
     * Updated date of the last file in the Store.
     */
    public final Long lastFileDate;

    /**
     * Updated number of files in the Store.
     */
    public final Long filesCount;

    /**
     * Creates instance of {@code StoreStatsChangedEventData}.
     * @param storeId ID of the changed Store's Context.
     * @param contextId ID of the changed Store.
     * @param lastFileDate Updated date of the last file in the Store.
     * @param filesCount Updated number of files in the Store.
     */
    public StoreStatsChangedEventData(
            String storeId,
            String contextId,
            Long lastFileDate,
            Long filesCount
    ){
        this.storeId = storeId;
        this.contextId = contextId;
        this.lastFileDate = lastFileDate;
        this.filesCount = filesCount;
    }
}
