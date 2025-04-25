package com.simplito.java.privmx_endpoint.model;

/**
 * Holds message's information created by server.
 */
public class ServerItemInfo {
    /**
     * ID of the Kvdb
     */
    String kvdbId;

    /**
     * Key of Item
     */
    String key;

    /**
     * Item's creation timestamp
     */
    long createDate;

    /**
     * ID of the user who created the message
     */
    String author;

    public ServerItemInfo(String kvdbId, String key, long createDate, String author) {
        this.kvdbId = kvdbId;
        this.key = key;
        this.createDate = createDate;
        this.author = author;
    }
};