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
 * Holds message's information created by server.
 *
 * @category thread
 * @group Thread
 */
public class ServerMessageInfo {

    /**
     * ID of the message's Thread.
     */
    public String threadId;

    /**
     * ID of the message.
     */
    public String messageId;

    /**
     * Message's creation timestamp.
     */
    public Long createDate;

    /**
     * ID of the user who created the message.
     */
    public String author;

    /**
     * Creates instance of {@code ServerMessageInfo}.
     * @param threadId ID of the message's Thread.
     * @param messageId ID of the message.
     * @param createDate Message's creation timestamp.
     * @param author ID of the user who created the message.
     */
    public ServerMessageInfo(
            String threadId,
            String messageId,
            Long createDate,
            String author
    ) {
        this.threadId = threadId;
        this.messageId = messageId;
        this.createDate = createDate;
        this.author = author;
    }
}
