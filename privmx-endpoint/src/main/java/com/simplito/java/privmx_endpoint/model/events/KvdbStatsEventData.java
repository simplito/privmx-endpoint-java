package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds KVDB statistical data.
 *
 * @category core
 * @group Events
 */
public class KvdbStatsEventData {

    /**
     * KVDB ID
     */
    public String kvdbId;

    /**
     * Timestamp of the most recent KVDB item
     */
    public Long lastEntryDate;

    /**
     * Updated number of entries in the KVDB
     */
    public Long entries;

    public KvdbStatsEventData(String kvdbId, Long lastEntryDate, Long entries) {
        this.kvdbId = kvdbId;
        this.lastEntryDate = lastEntryDate;
        this.entries = entries;
    }
}