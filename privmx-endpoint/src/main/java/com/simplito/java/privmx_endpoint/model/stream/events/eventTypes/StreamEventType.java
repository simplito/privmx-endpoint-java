package com.simplito.java.privmx_endpoint.model.stream.events.eventTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType;

/**
 * Defines the types of events that can occur within the stream for which a client can subscribe.
 * This enum lists the various actions or changes that can happen to
 * stream rooms and their streams, allowing observers to be notified of specific occurrences.
 */
public enum StreamEventType implements EventType {
    /**
     * Type of event triggered when a new StreamRoom is created.
     */
    STREAMROOM_CREATE,
    /**
     * Type of event triggered when an existing StreamRoom is updated.
     */
    STREAMROOM_UPDATE,
    /**
     * Type of event triggered when a StreamRoom is deleted.
     */
    STREAMROOM_DELETE,
    /**
     * Do not use.
     */
    EMPTY,
    /**
     * Type of event triggered when a participant joins a StreamRoom.
     */
    STREAM_JOIN,
    /**
     * Type of event triggered when a participant leaves a StreamRoom.
     */
    STREAM_LEAVE,
    /**
     * Type of event triggered when a participant starts publishing a stream.
     */
    STREAM_PUBLISH,
    /**
     * Type of event triggered when a participant stops publishing a stream.
     */
    STREAM_UNPUBLISH
}