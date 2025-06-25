package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds Kvdb statistical data.
 *
 * @category core
 * @group Events
 */
public class KvdbStatsEventData {

    /**
     * Kvdb ID
     */
    public String kvdbId;

    /**
     * Timestamp of the most recent Kvdb item
     */
    public Long lastEntryDate;

    /**
     * Updated number of entries in the Kvdb
     */
    public Long entries;

    public KvdbStatsEventData(String kvdbId, Long lastEntryDate, Long entries) {
        this.kvdbId = kvdbId;
        this.lastEntryDate = lastEntryDate;
        this.entries = entries;
    }
}