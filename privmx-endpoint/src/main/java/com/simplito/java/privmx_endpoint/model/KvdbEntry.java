package com.simplito.java.privmx_endpoint.model;

/**
 * Holds all available information about a Item.
 */
public class KvdbEntry {
    /**
     * Item information created by server
     */
    ServerKvdbEntryInfo info;

    /**
     * Item public metadata
     */
    byte[] publicMeta;

    /**
     * Item private metadata
     */
    byte[] privateMeta;

    /**
     * Item data
     */
    byte[] data;

    /**
     * public key of an author of the message
     */
    String authorPubKey;

    /**
     * status code of retrieval and decryption of the Kvdb
     */
    long statusCode;

    public KvdbEntry(ServerKvdbEntryInfo info, byte[] publicMeta, byte[] privateMeta, byte[] data, String authorPubKey, long statusCode) {
        this.info = info;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.data = data;
        this.authorPubKey = authorPubKey;
        this.statusCode = statusCode;
    }
};