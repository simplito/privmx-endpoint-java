package com.simplito.java.privmx_endpoint.streams.model;

public class StreamSubscription {
    public long streamId;
    public String streamTrackId;

    public StreamSubscription(long streamId, String streamTrackId) {
        this.streamId = streamId;
        this.streamTrackId = streamTrackId;
    }

    public StreamSubscription(long streamId) {
        this.streamId = streamId;
    }
}