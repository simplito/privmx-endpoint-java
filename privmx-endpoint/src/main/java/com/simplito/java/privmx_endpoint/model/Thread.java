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
 * Holds all available information about a Thread.
 *
 * @category thread
 * @group Thread
 */
@SuppressWarnings("CanBeFinal")
public class Thread {

    /**
     * ID of the Thread's Context.
     */
    public String contextId;

    /**
     * ID of the Thread.
     */
    public String threadId;

    /**
     * Thread creation timestamp.
     */
    public Long createDate;

    /**
     * ID of the user who created the Thread.
     */
    public String creator;

    /**
     * Thread last modification timestamp.
     */
    public Long lastModificationDate;

    /**
     * ID of the user who last modified the Thread.
     */
    public String lastModifier;

    /**
     * List of users (their IDs) with access to the Thread.
     */
    public List<String> users;

    /**
     * List of users (their IDs) with management rights.
     */
    public List<String> managers;

    /**
     * Version number (changes on updates).
     */
    public Long version;

    /**
     * Timestamp of the last posted message.
     */
    public Long lastMsgDate;

    /**
     * Total number of messages in the Thread.
     */
    public Long messagesCount;

    /**
     * Thread's public metadata.
     */
    public byte[] publicMeta;

    /**
     * Thread's private metadata.
     */
    public byte[] privateMeta;

    /**
     * Thread's policies
     */
    public ContainerPolicy policy;

    /**
     * Status code of retrieval and decryption of the {@code Thread}.
     */
    public Long statusCode;

    /**
     * Version of the Thread data structure and how it is encoded/encrypted.
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code Thread}.
     *
     * @param contextId            ID of the Context.
     * @param threadId             ID of the Thread.
     * @param createDate           Thread creation timestamp.
     * @param creator              ID of the user who created the Thread.
     * @param lastModificationDate Thread last modification timestamp.
     * @param lastModifier         ID of the user who last modified the Thread.
     * @param users                List of users (their IDs) with access to the Thread.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param lastMsgDate          Timestamp of the last posted message.
     * @param publicMeta           Total number of messages in the Thread.
     * @param privateMeta          Thread's public metadata.
     * @param policy               Thread's policies.
     * @param messagesCount        Thread's private metadata.
     * @param statusCode           Status code of retrieval and decryption of the {@code Thread}.
     * @param schemaVersion        Version of the Thread data structure and how it is encoded/encrypted.
     */
    public Thread(
            String contextId,
            String threadId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            Long lastMsgDate,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policy,
            Long messagesCount,
            Long statusCode,
            Long schemaVersion
    ) {
        this.contextId = contextId;
        this.threadId = threadId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.lastMsgDate = lastMsgDate;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.policy = policy;
        this.messagesCount = messagesCount;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }

    /**
     * Creates instance of {@code Thread}.
     *
     * @param contextId            ID of the Context.
     * @param threadId             ID of the Thread.
     * @param createDate           Thread creation timestamp.
     * @param creator              ID of the user who created the Thread.
     * @param lastModificationDate Thread last modification timestamp.
     * @param lastModifier         ID of the user who last modified the Thread.
     * @param users                List of users (their IDs) with access to the Thread.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param lastMsgDate          Timestamp of the last posted message.
     * @param publicMeta           Total number of messages in the Thread.
     * @param privateMeta          Thread's public metadata.
     * @param policy               Thread's policies.
     * @param messagesCount        Thread's private metadata.
     * @param statusCode           Status code of retrieval and decryption of the {@code Thread}.
     */
    @Deprecated
    public Thread(
            String contextId,
            String threadId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            Long lastMsgDate,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policy,
            Long messagesCount,
            Long statusCode
    ) {
        this(contextId, threadId, createDate, creator, lastModificationDate, lastModifier, users, managers, version, lastMsgDate, publicMeta, privateMeta, policy, messagesCount, statusCode, null);
    }

    /**
     * Creates instance of {@code Thread} with null policy value.
     *
     * @param contextId            ID of the Context.
     * @param threadId             ID of the Thread.
     * @param createDate           Thread creation timestamp.
     * @param creator              ID of the user who created the Thread.
     * @param lastModificationDate Thread last modification timestamp.
     * @param lastModifier         ID of the user who last modified the Thread.
     * @param users                List of users (their IDs) with access to the Thread.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param lastMsgDate          Timestamp of the last posted message.
     * @param publicMeta           Total number of messages in the Thread.
     * @param privateMeta          Thread's public metadata.
     * @param messagesCount        Thread's private metadata.
     * @param statusCode           Status code of retrieval and decryption of the {@code Thread}.
     */
    @Deprecated
    public Thread(
            String contextId,
            String threadId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            Long lastMsgDate,
            byte[] publicMeta,
            byte[] privateMeta,
            Long messagesCount,
            Long statusCode
    ) {
        this(contextId, threadId, createDate, creator, lastModificationDate, lastModifier, users, managers, version, lastMsgDate, publicMeta, privateMeta, null, messagesCount, statusCode);
    }
}