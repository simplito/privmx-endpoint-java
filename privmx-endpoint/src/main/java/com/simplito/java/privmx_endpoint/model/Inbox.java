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
 * Holds all available information about an Inbox.
 *
 * @category inbox
 * @group Inbox
 */
@SuppressWarnings("CanBeFinal")
public class Inbox {

    /**
     * ID of the Inbox.
     */
    public String inboxId;

    /**
     * ID of the Context.
     */
    public String contextId;

    /**
     * Inbox creation timestamp.
     */
    public Long createDate;

    /**
     * ID of the user who created the Inbox.
     */
    public String creator;

    /**
     * Inbox last modification timestamp.
     */
    public Long lastModificationDate;

    /**
     * ID of the user who last modified the Inbox.
     */
    public String lastModifier;

    /**
     * List of users (their IDs) with access to the Inbox.
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
     * Inbox public metadata.
     */
    public byte[] publicMeta;

    /**
     * Inbox private metadata.
     */
    public byte[] privateMeta;

    /**
     * Inbox files configuration.
     */
    public FilesConfig filesConfig;

    /**
     * Inbox policies.
     */
    public ContainerPolicyWithoutItem policy;

    /**
     * Status code of retrieval and decryption of the {@code Inbox}.
     */
    public Long statusCode;

    /**
     * Version of the Inbox data structure and how it is encoded/encrypted.
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code Inbox}.
     *
     * @param inboxId              ID of the Inbox.
     * @param contextId            ID of the Context.
     * @param createDate           Inbox creation timestamp.
     * @param creator              ID of the user who created the Inbox.
     * @param lastModificationDate Inbox last modification timestamp.
     * @param lastModifier         ID of the user who last modified the Inbox.
     * @param users                List of users (their IDs) with access to the Inbox.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param publicMeta           Inbox public metadata.
     * @param privateMeta          Inbox private metadata.
     * @param filesConfig          Inbox files configuration.
     * @param policy               Inbox policies.
     * @param statusCode           Status code of retrieval and decryption of the {@code Inbox}.
     * @param schemaVersion        Version of the Inbox data structure and how it is encoded/encrypted
     */
    public Inbox(
            String inboxId,
            String contextId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            ContainerPolicyWithoutItem policy,
            Long statusCode,
            Long schemaVersion
    ) {
        this.inboxId = inboxId;
        this.contextId = contextId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.filesConfig = filesConfig;
        this.policy = policy;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }

    /**
     * Creates instance of {@code Inbox} with null policy value.
     *
     * @param inboxId              ID of the Inbox.
     * @param contextId            ID of the Context.
     * @param createDate           Inbox creation timestamp.
     * @param creator              ID of the user who created the Inbox.
     * @param lastModificationDate Inbox last modification timestamp.
     * @param lastModifier         ID of the user who last modified the Inbox.
     * @param users                List of users (their IDs) with access to the Inbox.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param publicMeta           Inbox public metadata.
     * @param privateMeta          Inbox private metadata.
     * @param filesConfig          Inbox files configuration.
     * @param statusCode           Status code of retrieval and decryption of the {@code Inbox}.
     */
    @Deprecated
    public Inbox(
            String inboxId,
            String contextId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            Long statusCode
    ) {
        this.inboxId = inboxId;
        this.contextId = contextId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.filesConfig = filesConfig;
        this.statusCode = statusCode;
    }
}