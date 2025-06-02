package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds Kvdb statistical data.
 */
public class KvdbStatsEventData {

    /**
     * Kvdb ID
     */
    String kvdbId;

    /**
     * Timestamp of the most recent Kvdb item
     */
    long lastEntryDate;

    /**
     * Updated number of entries in the Kvdb
     */
    long entries;

    public KvdbStatsEventData(String kvdbId, long lastEntryDate, long entries) {
        this.kvdbId = kvdbId;
        this.lastEntryDate = lastEntryDate;
        this.entries = entries;
    }
}