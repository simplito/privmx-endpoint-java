package com.simplito.java.privmx_endpoint.model;

/**
 * Holds Kvdb entry's information created by the server.
 *
 * @category kvdb
 */
public class ServerKvdbEntryInfo {
    /**
     * ID of the Kvdb
     */
    String kvdbId;

    /**
     * Kvdb entry's key
     */
    String key;

    /**
     * Entry's creation timestamp
     */
    long createDate;

    /**
     * ID of the user who created the entry
     */
    String author;

    /**
     * Creates instance of {@code ServerKvdbEntryInfo}.
     *
     * @param kvdbId     ID of the Kvdb
     * @param key        Kvdb entry's key
     * @param createDate Entry's creation timestamp
     * @param author     ID of the user who created the entry
     */
    public ServerKvdbEntryInfo(String kvdbId, String key, long createDate, String author) {
        this.kvdbId = kvdbId;
        this.key = key;
        this.createDate = createDate;
        this.author = author;
    }
}