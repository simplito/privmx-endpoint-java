package com.simplito.java.privmx_endpoint.streams.model;

import java.util.List;

public class StreamTrackModification {
    public Long streamId;
    public List<StreamTrackModificationPair> tracks;

    public StreamTrackModification(Long streamId, List<StreamTrackModificationPair> tracks) {
        this.streamId = streamId;
        this.tracks = tracks;
    }
}
