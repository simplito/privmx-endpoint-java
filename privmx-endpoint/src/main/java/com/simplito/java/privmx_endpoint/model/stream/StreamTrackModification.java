package com.simplito.java.privmx_endpoint.model.stream;

import java.util.List;

/**
 * Represents a set of track modifications applied to a specific stream.
 */
public class StreamTrackModification {
    /**
     * Identifier of the stream for which tracks were modified
     */
    public Long streamId;

    /**
     * List of track modifications, each containing the state of a track before and after the change
     */
    public List<StreamTrackModificationPair> tracks;

    /**
     * Constructs a new {@link StreamTrackModification} instance.
     *
     * @param streamId Identifier of the stream for which tracks were modified
     * @param tracks   List of track modifications, each containing the state of a track before and after the change
     */
    public StreamTrackModification(Long streamId, List<StreamTrackModificationPair> tracks) {
        this.streamId = streamId;
        this.tracks = tracks;
    }
}
