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
 * Holds information about a message deleted from a Thread.
 * @category core
 * @group Events
 */
public class ThreadDeletedMessageEventData {

    /**
     * ID of the deleted message's Thread.
     */
    public final String threadId;

    /**
     * ID of the deleted Message.
     */
    public final String messageId;

    /**
     * Creates instance of {@code ThreadDeletedMessageEventData}.
     * @param threadId ID of the deleted message's Thread.
     * @param messageId ID of the deleted Message.
     */
    ThreadDeletedMessageEventData(
            String threadId,
            String messageId
    ){
        this.threadId = threadId;
        this.messageId = messageId;
    }
}
