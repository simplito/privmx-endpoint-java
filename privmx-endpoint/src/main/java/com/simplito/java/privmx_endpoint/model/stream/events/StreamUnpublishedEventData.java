package com.simplito.java.privmx_endpoint.model.stream.events;

public class StreamUnpublishedEventData {
    public String streamRoomId;
    public Long streamId;

    public StreamUnpublishedEventData(String streamRoomId, Long streamId) {
        this.streamRoomId = streamRoomId;
        this.streamId = streamId;
    }
}
