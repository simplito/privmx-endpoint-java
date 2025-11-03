package com.simplito.java.privmx_endpoint.model.events.eventTypes;

public enum StreamEventType implements EventType{
    STREAMROOM_CREATE,
    STREAMROOM_UPDATE,
    STREAMROOM_DELETE,
    STREAM_JOIN,
    STREAM_LEAVE,
    STREAM_PUBLISH,
    STREAM_UNPUBLISH
}
