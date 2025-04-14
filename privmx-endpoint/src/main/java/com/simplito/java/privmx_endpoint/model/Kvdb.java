package com.simplito.java.privmx_endpoint.model;

import java.util.List;

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
     * list of users (their IDs) with access to the Kvdb
     */
    List<String> users;

    /**
     * list of users (their IDs) with management rights
     */
    List<String> managers;

    /**
     * version number (changes on updates)
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
     * total number of items in the Kvdb
     */
    long items;

    /**
     * timestamp of last new item
     */
    long lastItemDate;

    /**
     * Kvdb's policies
     */
    ContainerPolicy policy;

    /**
     * status code of retrieval and decryption of the Kvdb
     */
    long statusCode;

    public Kvdb(String contextId, String kvdbId, long createDate, String creator, long lastModificationDate, String lastModifier, List<String> users, List<String> managers, long version, byte[] publicMeta, byte[] privateMeta, long items, long lastItemDate, ContainerPolicy policy, long statusCode) {
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
        this.items = items;
        this.lastItemDate = lastItemDate;
        this.policy = policy;
        this.statusCode = statusCode;
    }
}
