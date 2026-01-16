package com.simplito.java.privmx_endpoint.model;

import java.util.List;

public class StreamTrackModification {
    public long streamId;
    public List<StreamTrackModificationPair> tracks;

    public StreamTrackModification(long streamId, List<StreamTrackModificationPair> tracks) {
        this.streamId = streamId;
        this.tracks = tracks;
    }
}
