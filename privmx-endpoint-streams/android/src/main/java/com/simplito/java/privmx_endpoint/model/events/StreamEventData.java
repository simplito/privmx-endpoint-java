package com.simplito.java.privmx_endpoint.model.events;

import java.util.List;

public class StreamEventData {
    public final String streamRoomId;
    public final List<Long> streamIds;
    public final String userId;

    public StreamEventData(String streamRoomId, List<Long> streamIds, String userId) {
        this.streamRoomId = streamRoomId;
        this.streamIds = streamIds;
        this.userId = userId;
    }
}
