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

package com.simplito.java.privmx_endpoint.model.stream;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;

import java.util.List;

/**
 * Represents a stream room within a PrivMX context
 */
public class StreamRoom {
    /**
     * Identifier of the context this stream room belongs to
     */
    public String contextId;

    /**
     * Identifier of the stream room
     */
    public String streamRoomId;

    /**
     * Timestamp of when the stream room was created
     */
    public Long createDate;

    /**
     * Identifier of the user who created the stream room
     */
    public String creator;

    /**
     * Timestamp of when the stream room was last modified
     */
    public Long lastModificationDate;

    /**
     * Identifier of the user who last modified the stream room
     */
    public String lastModifier;
    /**
     * List of usernames with access to the stream room
     */
    public List<String> users;
    /**
     * List of usernames with management privileges over the stream room
     */
    public List<String> managers;

    /**
     * Version number (changes on updates).
     */
    public Long version;

    /**
     * StreamRoom's public metadata
     */
    public byte[] publicMeta;

    /**
     * StreamRoom's private metadata
     */

    public byte[] privateMeta;

    /**
     * StreamRoom's policies
     */
    public ContainerPolicy policy;

    /**
     * Status code of retrieval and decryption of the {@code StreamRoom}
     */
    public Long statusCode;

    /**
     * Version of the StreamRoom data structure and how it is encoded/encrypted
     */
    public Long schemaVersion;

    /**
     * Indicates if the stream room is closed
     */
    public Boolean closed;

    /**
     * Constructs a new {@link StreamRoom} instance.
     *
     * @param contextId            Identifier of the context this stream room belongs to
     * @param streamRoomId         Identifier of the stream room
     * @param createDate           Timestamp of when the stream room was created
     * @param creator              Identifier of the user who created the stream room
     * @param lastModificationDate Timestamp of when the stream room was last modified
     * @param lastModifier         Identifier of the user who last modified the stream room
     * @param users                List of usernames with access to the stream room
     * @param managers             List of usernames with management privileges over the stream room
     * @param version              Version number (changes on updates)
     * @param publicMeta           StreamRoom's public metadata
     * @param privateMeta          StreamRoom's private metadata
     * @param policy               StreamRoom's policies
     * @param statusCode           Status code of retrieval and decryption of the {@code StreamRoom}
     * @param schemaVersion        Version of the StreamRoom data structure and how it is encoded/encrypted
     * @param closed               Indicates if the stream room is closed
     */
    public StreamRoom(
            String contextId,
            String streamRoomId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policy,
            Long statusCode,
            Long schemaVersion,
            Boolean closed
    ) {
        this.contextId = contextId;
        this.streamRoomId = streamRoomId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.policy = policy;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
        this.closed = closed;
    }
}
