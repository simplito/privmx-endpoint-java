package com.simplito.java.privmx_endpoint.model;

/**
 * Holds KVDB entry's information created by the server.
 *
 * @category kvdb
 * @group Kvdb
 */
public class ServerKvdbEntryInfo {
    /**
     * ID of the KVDB
     */
    public String kvdbId;

    /**
     * KVDB entry's key
     */
    public String key;

    /**
     * Entry's creation timestamp
     */
    public Long createDate;

    /**
     * ID of the user who created the entry
     */
    public String author;

    /**
     * Creates instance of {@code ServerKvdbEntryInfo}.
     *
     * @param kvdbId     ID of the KVDB
     * @param key        KVDB entry's key
     * @param createDate Entry's creation timestamp
     * @param author     ID of the user who created the entry
     */
    public ServerKvdbEntryInfo(String kvdbId, String key, Long createDate, String author) {
        this.kvdbId = kvdbId;
        this.key = key;
        this.createDate = createDate;
        this.author = author;
    }
}