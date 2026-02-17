package com.simplito.java.privmx_endpoint.model.stream;

public class PublishedStreamData {
    public String streamRoomId;
    public StreamInfo stream;
    public String userId;

    public PublishedStreamData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}
