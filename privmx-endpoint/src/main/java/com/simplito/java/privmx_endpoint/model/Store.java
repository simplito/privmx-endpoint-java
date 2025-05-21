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
 * Holds all available information about a Store.
 *
 * @category store
 * @group Store
 */
@SuppressWarnings("CanBeFinal")
public class Store {
    /**
     * ID of the Store.
     */
    public String storeId;

    /**
     * ID of the Context.
     */
    public String contextId;

    /**
     * Store creation timestamp.
     */
    public Long createDate;

    /**
     * ID of the user who created the Store.
     */
    public String creator;

    /**
     * Store last modification timestamp.
     */
    public Long lastModificationDate;

    /**
     * Timestamp of the last created file.
     */
    public Long lastFileDate;

    /**
     * ID of the user who last modified the Store.
     */
    public String lastModifier;

    /**
     * List of users (their IDs) with access to the Store.
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
     * Store's public metadata.
     */
    public byte[] publicMeta;

    /**
     * Store's private metadata.
     */
    public byte[] privateMeta;

    /**
     * Store's policies
     */
    public ContainerPolicy policy;

    /**
     * Total number of files in the Store.
     */
    public Long filesCount;

    /**
     * Status code of retrieval and decryption of the {@code Store}.
     */
    public Long statusCode;

    /**
     * Creates instance of {@code Store}.
     *
     * @param storeId              ID of the Store.
     * @param contextId            ID of the Context.
     * @param createDate           Store creation timestamp.
     * @param creator              ID of the user who created the Store.
     * @param lastModificationDate Store last modification timestamp.
     * @param lastFileDate         Timestamp of the last created file.
     * @param lastModifier         ID of the user who last modified the Store.
     * @param users                List of users (their IDs) with access to the Store.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param publicMeta           Store's public metadata.
     * @param privateMeta          Store's private metadata.
     * @param policy               Store's policies
     * @param filesCount           Total number of files in the Store.
     * @param statusCode           Status code of retrieval and decryption of the {@code Store}.
     */
    public Store(
            String storeId,
            String contextId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            Long lastFileDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policy,
            Long filesCount,
            Long statusCode
    ) {
        this.storeId = storeId;
        this.contextId = contextId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastFileDate = lastFileDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.policy = policy;
        this.filesCount = filesCount;
        this.statusCode = statusCode;
    }

    /**
     * Creates instance of {@code Store} with null policy value.
     *
     * @param storeId              ID of the Store.
     * @param contextId            ID of the Context.
     * @param createDate           Store creation timestamp.
     * @param creator              ID of the user who created the Store.
     * @param lastModificationDate Store last modification timestamp.
     * @param lastFileDate         Timestamp of the last created file.
     * @param lastModifier         ID of the user who last modified the Store.
     * @param users                List of users (their IDs) with access to the Store.
     * @param managers             List of users (their IDs) with management rights.
     * @param version              Version number (changes on updates).
     * @param publicMeta           Store's public metadata.
     * @param privateMeta          Store's private metadata.
     * @param filesCount           Total number of files in the Store.
     * @param statusCode           Status code of retrieval and decryption of the {@code Store}.
     */
    @Deprecated
    public Store(
            String storeId,
            String contextId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            Long lastFileDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            Long filesCount,
            Long statusCode
    ) {
        this(storeId, contextId, createDate, creator, lastModificationDate, lastFileDate, lastModifier, users, managers, version, publicMeta, privateMeta, null, filesCount, statusCode);
    }
}