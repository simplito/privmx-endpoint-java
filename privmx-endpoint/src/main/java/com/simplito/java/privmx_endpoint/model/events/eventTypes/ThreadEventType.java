package com.simplito.java.privmx_endpoint.model.events.eventTypes;

/**
 * The type of event you want to listen (subscribe) for.
 * Determines kind of action performed on a Thread or on a Message.
 *
 * @category core
 * @group EventTypes
 */
public enum ThreadEventType implements EventType {
    THREAD_CREATE,
    THREAD_UPDATE,
    THREAD_DELETE,
    THREAD_STATS,
    MESSAGE_CREATE,
    MESSAGE_UPDATE,
    MESSAGE_DELETE
}
