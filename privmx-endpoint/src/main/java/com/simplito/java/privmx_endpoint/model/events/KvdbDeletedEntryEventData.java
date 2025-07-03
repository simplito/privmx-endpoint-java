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
 * Holds information of `KvdbDeletedEntryEvent`.
 *
 * @category core
 * @group Events
 */
public class KvdbDeletedEntryEventData {

    /**
     * KVDB ID
     */
    public String kvdbId;

    /**
     * Key of deleted Entry
     */
    public String kvdbEntryKey;

    public KvdbDeletedEntryEventData(String kvdbId, String kvdbEntryKey) {
        this.kvdbId = kvdbId;
        this.kvdbEntryKey = kvdbEntryKey;
    }
}