package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information of `KvdbDeletedEvent`.
 *
 * @category core
 * @group Events
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