package com.simplito.java.privmx_endpoint.model;

public class StreamTrackModificationPair {
    public StreamTrackInfo before;
    public StreamTrackInfo after;

    public StreamTrackModificationPair(StreamTrackInfo before, StreamTrackInfo after) {
        this.before = before;
        this.after = after;
    }

    public StreamTrackModificationPair(StreamTrackInfo before) {
        this(before, null);
    }

    public StreamTrackModificationPair() {
        this(null, null);
    }
}
