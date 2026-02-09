package com.simplito.java.privmx_endpoint.streams.model.events;


import com.simplito.java.privmx_endpoint.streams.model.StreamInfo;

// todo: which to choose
// same as StreamPublishedEventData
public class StreamPublishedEventData {

    /**
     * StreamRoom ID
     */
    public String streamRoomId;

    /**
     * Stream ID's
     */
    public StreamInfo stream;

    public String  userId;

    public StreamPublishedEventData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}