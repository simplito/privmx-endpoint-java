package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information of `KvdbDeletedEvent`.
 */
public class KvdbDeletedEventData {

    /**
     * Kvdb ID
     */
    String kvdbId;

    public KvdbDeletedEventData(String kvdbId) {
        this.kvdbId = kvdbId;
    }
}