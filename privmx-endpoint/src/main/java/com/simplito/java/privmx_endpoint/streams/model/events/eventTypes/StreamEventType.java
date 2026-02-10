package com.simplito.java.privmx_endpoint.streams.model.events.eventTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType;

public enum StreamEventType implements EventType {
    STREAMROOM_CREATE,
    STREAMROOM_UPDATE,
    STREAMROOM_DELETE,
    EMPTY,
    STREAM_JOIN,
    STREAM_LEAVE,
    STREAM_PUBLISH,
    STREAM_UNPUBLISH
}


