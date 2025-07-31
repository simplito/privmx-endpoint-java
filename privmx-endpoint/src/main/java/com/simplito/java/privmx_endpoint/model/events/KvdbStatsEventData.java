//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds data of event that arrives when KVDB stats change.
 *
 * @category core
 * @group Events
 */
public class KvdbStatsEventData {

    /**
     * KVDB ID
     */
    public String kvdbId;

    /**
     * Timestamp of the most recent KVDB item
     */
    public Long lastEntryDate;

    /**
     * Updated number of entries in the KVDB
     */
    public Long entries;

    public KvdbStatsEventData(String kvdbId, Long lastEntryDate, Long entries) {
        this.kvdbId = kvdbId;
        this.lastEntryDate = lastEntryDate;
        this.entries = entries;
    }
}