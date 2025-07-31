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

package com.simplito.java.privmx_endpoint.model;

import java.util.List;

/**
 * Holds all available information about a KVDB.
 *
 * @category kvdb
 * @group Kvdb
 */
public class Kvdb {

    /**
     * ID of the Context
     */
    public String contextId;

    /**
     * ID of the KVDB
     */
    public String kvdbId;

    /**
     * KVDB creation timestamp
     */
    public Long createDate;

    /**
     * ID of user who created the KVDB
     */
    public String creator;

    /**
     * KVDB last modification timestamp
     */
    public Long lastModificationDate;

    /**
     * ID of the user who last modified the KVDB
     */
    public String lastModifier;
    /**
     * List of users (their IDs) with access to the KVDB
     */
    public List<String> users;
    /**
     * List of users (their IDs) with management rights
     */
    public List<String> managers;
    /**
     * Version number (changes on updates)
     */
    public Long version;
    /**
     * KVDB's public metadata
     */
    public byte[] publicMeta;
    /**
     * KVDB's private metadata
     */
    public byte[] privateMeta;
    /**
     * Total number of entries in the KVDB
     */
    public Long entries;
    /**
     * Timestamp of the last added entry
     */
    public Long lastEntryDate;
    /**
     * KVDB's policies
     */
    public ContainerPolicy policy;
    /**
     * Retrieval and decryption status code
     */
    public Long statusCode;
    /**
     * Version of the KVDB data structure and how it is encoded/encrypted
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code Kvdb}.
     *
     * @param contextId            ID of the Context
     * @param kvdbId               ID of the KVDB
     * @param createDate           KVDB creation timestamp
     * @param creator              ID of user who created the KVDB
     * @param lastModificationDate KVDB last modification timestamp
     * @param lastModifier         ID of the user who last modified the KVDB
     * @param users                List of users (their IDs) with access to the KVDB
     * @param managers             List of users (their IDs) with management rights
     * @param version              Version number (changes on updates)
     * @param publicMeta           KVDB's public metadata
     * @param privateMeta          KVDB's private metadata
     * @param entries              Total number of entries in the KVDB
     * @param lastEntryDate        Timestamp of the last added entry
     * @param policy               KVDB's policies
     * @param statusCode           Retrieval and decryption status code
     * @param schemaVersion        Version of the KVDB data structure and how it is encoded/encrypted
     */
    public Kvdb(
            String contextId,
            String kvdbId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            Long entries,
            Long lastEntryDate,
            ContainerPolicy policy,
            Long statusCode,
            Long schemaVersion
    ) {
        this.contextId = contextId;
        this.kvdbId = kvdbId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.entries = entries;
        this.lastEntryDate = lastEntryDate;
        this.policy = policy;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }
}
