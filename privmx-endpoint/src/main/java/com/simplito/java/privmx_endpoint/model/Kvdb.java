package com.simplito.java.privmx_endpoint.model;

import java.util.List;

/**
 * Holds all available information about a Kvdb.
 *
 * @category kvdb
 */
public class Kvdb {

    /**
     * ID of the Context
     */
    String contextId;

    /**
     * ID of the Kvdb
     */
    String kvdbId;

    /**
     * Kvdb creation timestamp
     */
    long createDate;

    /**
     * ID of user who created the Kvdb
     */
    String creator;

    /**
     * Kvdb last modification timestamp
     */
    long lastModificationDate;

    /**
     * ID of the user who last modified the Kvdb
     */
    String lastModifier;

    /**
     * List of users (their IDs) with access to the Kvdb
     */
    List<String> users;

    /**
     * List of users (their IDs) with management rights
     */
    List<String> managers;

    /**
     * Version number (changes on updates)
     */
    long version;

    /**
     * Kvdb's public metadata
     */
    byte[] publicMeta;

    /**
     * Kvdb's private metadata
     */
    byte[] privateMeta;

    /**
     * Total number of entries in the Kvdb
     */
    long entries;

    /**
     * Timestamp of the last added entry
     */
    long lastEntryDate;

    /**
     * Kvdb's policies
     */
    ContainerPolicy policy;

    /**
     * Retrieval and decryption status code
     */
    long statusCode;

    /**
     * Version of the Kvdb data structure and how it is encoded/encrypted
     */
    long schemaVersion;

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
    public Kvdb(String contextId, String kvdbId, long createDate, String creator, long lastModificationDate, String lastModifier, List<String> users, List<String> managers, long version, byte[] publicMeta, byte[] privateMeta, long entries, long lastEntryDate, ContainerPolicy policy, long statusCode, long schemaVersion) {
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
