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
     * timestamp of the most recent Kvdb item
     */
    long lastItemDate;

    /**
     * updated number of items in the Kvdb
     */
    long items;

    public KvdbStatsEventData(String kvdbId, long lastItemDate, long items) {
        this.kvdbId = kvdbId;
        this.lastItemDate = lastItemDate;
        this.items = items;
    }
}