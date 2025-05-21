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

package com.simplito.java.privmx_endpoint.model;

import java.util.List;

/**
 * Holds information about Inbox entry.
 *
 * @category inbox
 * @group Inbox
 */
@SuppressWarnings("CanBeFinal")
public class InboxEntry {

    /**
     * ID of the entry.
     */
    public String entryId;

    /**
     * ID of the Inbox.
     */
    public String inboxId;

    /**
     * Entry data.
     */
    public byte[] data;

    /**
     * List of files attached to the entry.
     */
    public List<File> files;

    /**
     * Public key of the author of an entry.
     */
    public String authorPubKey;

    /**
     * Inbox entry creation timestamp.
     */
    public Long createDate;

    /**
     * Status code of retrieval and decryption of the {@code Inbox} entry.
     */
    public Long statusCode;

    /**
     * Creates instance of {@code InboxEntry}.
     *
     * @param entryId      ID of the entry.
     * @param inboxId      ID of the Inbox.
     * @param data         Entry data.
     * @param files        List of files attached to the entry.
     * @param authorPubKey Public key of the author of an entry.
     * @param createDate   Inbox entry creation timestamp.
     * @param statusCode   Status code of retrieval and decryption of the {@code Inbox} entry.
     */
    public InboxEntry(
            String entryId,
            String inboxId,
            byte[] data,
            List<File> files,
            String authorPubKey,
            Long createDate,
            Long statusCode
    ) {
        this.entryId = entryId;
        this.inboxId = inboxId;
        this.data = data;
        this.files = files;
        this.authorPubKey = authorPubKey;
        this.createDate = createDate;
        this.statusCode = statusCode;
    }
}