package com.simplito.java.privmx_endpoint.model.stream.events;

import java.util.List;

public class StreamLeftEventData {
    public String streamRoomId;
    public Long streamId;
    public String userId;

    public StreamLeftEventData(String streamRoomId, Long streamId, String userId) {
        this.streamRoomId = streamRoomId;
        this.streamId = streamId;
        this.userId = userId;
    }
}
