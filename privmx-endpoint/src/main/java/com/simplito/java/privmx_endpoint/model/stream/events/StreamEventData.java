package com.simplito.java.privmx_endpoint.model.stream.events;

import java.util.List;

public class StreamEventData {
    public String streamRoomId;
    public List<Long> streamIds;
    public String userId;

    public StreamEventData(String streamRoomId, List<Long> streamIds, String userId) {
        this.streamRoomId = streamRoomId;
        this.streamIds = streamIds;
        this.userId = userId;
    }
}
