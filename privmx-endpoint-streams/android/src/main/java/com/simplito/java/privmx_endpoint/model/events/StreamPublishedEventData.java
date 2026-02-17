package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.StreamInfo;

// todo: which to choose
// same as StreamPublishedEventData
public class StreamPublishedEventData {

    /**
     * StreamRoom ID
     */
    public final String streamRoomId;

    /**
     * Stream ID's
     */
    public final StreamInfo stream;

    public final String userId;

    public StreamPublishedEventData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}