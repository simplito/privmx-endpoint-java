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
 * Holds data of event that arrives when KVDB is deleted.
 *
 * @category core
 * @group Events
 */
public class KvdbDeletedEventData {

    /**
     * KVDB ID
     */
    public String kvdbId;

    public KvdbDeletedEventData(String kvdbId) {
        this.kvdbId = kvdbId;
    }
}