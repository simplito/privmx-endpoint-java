package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Represents a single track modification, holding the state of a track before and after the change.
 */
public class StreamTrackModificationPair {
    /**
     * State of the track before the modification
     */
    public StreamTrackInfo before;

    /**
     * State of the track after the modification
     */
    public StreamTrackInfo after;

    /**
     * Constructs a new {@link StreamTrackModificationPair} instance.
     *
     * @param before The state of the track before the modification
     * @param after  The state of the track after the modification
     */
    public StreamTrackModificationPair(StreamTrackInfo before, StreamTrackInfo after) {
        this.before = before;
        this.after = after;
    }
}
