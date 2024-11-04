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
 * Holds information about a entry deleted from Inbox.
 *
 * @category core
 * @group Events
 */
public class InboxEntryDeletedEventData {
    /**
     * ID of the deleted entry's Inbox.
     */
    public final String inboxId;

    /**
     * ID of the deleted entry.
     */
    public final String entryId;

    /**
     * Creates instance of {@code InboxEntryDeletedEventData}.
     * @param inboxId ID of the deleted entry's Inbox.
     * @param entryId ID of the deleted entry.
     */
    public InboxEntryDeletedEventData(
            String inboxId,
            String entryId
    ) {
        this.inboxId = inboxId;
        this.entryId = entryId;
    }
}
