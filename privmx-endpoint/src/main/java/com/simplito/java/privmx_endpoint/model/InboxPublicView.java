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

/**
 * Holds Inbox public information.
 *
 * @category inbox
 * @group Inbox
 */
public class InboxPublicView {

    /**
     * ID of the Inbox.
     */
    public String inboxId;

    /**
     * Version of the Inbox.
     */
    public Long version;

    /**
     * Inbox public metadata.
     */
    public byte[] publicMeta;

    /**
     * Creates instance of {@code InboxPublicView}.
     * @param inboxId ID of the Inbox.
     * @param version Version of the Inbox.
     * @param publicMeta Inbox public metadata.
     */
    public InboxPublicView(
            String inboxId,
            Long version,
            byte[] publicMeta
    ) {
        this.inboxId = inboxId;
        this.version = version;
        this.publicMeta = publicMeta;
    }
}
