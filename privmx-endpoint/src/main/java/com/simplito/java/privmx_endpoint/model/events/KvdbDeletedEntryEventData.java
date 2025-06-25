package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information of `KvdbDeletedEntryEvent`.
 *
 * @category core
 * @group Events
 */
public class KvdbDeletedEntryEventData {

    /**
     * Kvdb ID
     */
    String kvdbId;

    /**
     * Key of deleted Entry
     */
    String kvdbEntryKey;

    public KvdbDeletedEntryEventData(String kvdbId, String kvdbEntryKey) {
        this.kvdbId = kvdbId;
        this.kvdbEntryKey = kvdbEntryKey;
    }
}