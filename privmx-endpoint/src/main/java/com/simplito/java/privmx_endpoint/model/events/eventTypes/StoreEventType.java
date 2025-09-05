package com.simplito.java.privmx_endpoint.model.events.eventTypes;

/**
 * The type of event you want to listen (subscribe) for.
 * Determines kind of action performed on a Store or on a File.
 *
 * @category core
 * @group EventTypes
 */
public enum StoreEventType implements EventType {
    STORE_CREATE,
    STORE_UPDATE,
    STORE_DELETE,
    STORE_STATS,
    FILE_CREATE,
    FILE_UPDATE,
    FILE_DELETE,
    COLLECTION_CHANGE
}
