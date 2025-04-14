package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information of `KvdbDeletedItemEvent`.
 */
public class KvdbDeletedItemEventData {

    /**
     * Kvdb ID
     */
    String kvdbId;

    /**
     * Key of deleted Item
     */
    String kvdbItemKey;

    public KvdbDeletedItemEventData(String kvdbId, String kvdbItemKey) {
        this.kvdbId = kvdbId;
        this.kvdbItemKey = kvdbItemKey;
    }
}