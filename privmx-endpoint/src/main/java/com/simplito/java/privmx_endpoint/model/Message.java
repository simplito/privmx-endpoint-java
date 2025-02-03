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
 * Holds information about the Message.
 *
 * @category thread
 * @group Thread
 */
@SuppressWarnings("CanBeFinal")
public class Message {

    /**
     * Message's information created by server.
     */
    public ServerMessageInfo info;

    /**
     * Message's public metadata.
     */
    public byte[] publicMeta;

    /**
     * Message's private metadata.
     */
    public byte[] privateMeta;

    /**
     * Message's data.
     */
    public byte[] data;

    /**
     * Public key of the author of the message.
     */
    public String authorPubKey;

    /**
     * Status code of retrieval and decryption of the {@code Message}.
     */
    public Long statusCode;

    /**
     * Creates instance of {@code Message}.
     *
     * @param info         Message's information created by server.
     * @param publicMeta   Message's public metadata.
     * @param privateMeta  Message's private metadata.
     * @param data         Message's data.
     * @param authorPubKey Public key of the author of the message.
     * @param statusCode   Status code of retrieval and decryption of the {@code Message}.
     */
    public Message(
            ServerMessageInfo info,
            byte[] publicMeta,
            byte[] privateMeta,
            byte[] data,
            String authorPubKey,
            Long statusCode
    ) {
        this.info = info;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.authorPubKey = authorPubKey;
        this.data = data;
        this.statusCode = statusCode;
    }
}
