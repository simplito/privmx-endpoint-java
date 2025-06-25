package com.simplito.java.privmx_endpoint.model;

import java.util.List;

/**
 * Holds all available information about a Kvdb.
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
     * ID of the Kvdb
     */
    public String kvdbId;

    /**
     * Kvdb creation timestamp
     */
    public Long createDate;

    /**
     * ID of user who created the Kvdb
     */
    public String creator;

    /**
     * Kvdb last modification timestamp
     */
    public Long lastModificationDate;

    /**
     * ID of the user who last modified the Kvdb
     */
    public String lastModifier;
    /**
     * List of users (their IDs) with access to the Kvdb
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
     * Kvdb's public metadata
     */
    public byte[] publicMeta;
    /**
     * Kvdb's private metadata
     */
    public byte[] privateMeta;
    /**
     * Total number of entries in the Kvdb
     */
    public Long entries;
    /**
     * Timestamp of the last added entry
     */
    public Long lastEntryDate;
    /**
     * Kvdb's policies
     */
    public ContainerPolicy policy;
    /**
     * Retrieval and decryption status code
     */
    public Long statusCode;
    /**
     * Version of the Kvdb data structure and how it is encoded/encrypted
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code Kvdb}.
     *
     * @param contextId            ID of the Context
     * @param kvdbId               ID of the Kvdb
     * @param createDate           Kvdb creation timestamp
     * @param creator              ID of user who created the Kvdb
     * @param lastModificationDate Kvdb last modification timestamp
     * @param lastModifier         ID of the user who last modified the Kvdb
     * @param users                List of users (their IDs) with access to the Kvdb
     * @param managers             List of users (their IDs) with management rights
     * @param version              Version number (changes on updates)
     * @param publicMeta           Kvdb's public metadata
     * @param privateMeta          Kvdb's private metadata
     * @param entries              Total number of entries in the Kvdb
     * @param lastEntryDate        Timestamp of the last added entry
     * @param policy               Kvdb's policies
     * @param statusCode           Retrieval and decryption status code
     * @param schemaVersion        Version of the Kvdb data structure and how it is encoded/encrypted
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
