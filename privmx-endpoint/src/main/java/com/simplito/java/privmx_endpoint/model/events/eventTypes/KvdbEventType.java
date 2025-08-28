package com.simplito.java.privmx_endpoint.model.events.eventTypes;

/**
 * The type of event you want to listen (subscribe) for.
 * Determines kind of action performed on a Kvdb or on a KvdbEntry.
 *
 * @category core
 * @group EventTypes
 */
public enum KvdbEventType implements EventType {
    KVDB_CREATE,
    KVDB_UPDATE,
    KVDB_DELETE,
    KVDB_STATS,
    ENTRY_CREATE,
    ENTRY_UPDATE,
    ENTRY_DELETE
}
