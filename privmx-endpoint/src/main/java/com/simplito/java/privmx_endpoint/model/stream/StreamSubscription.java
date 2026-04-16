package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Describes a remote stream or a specific track within that stream,
 * used when subscribing to, modifying, or unsubscribing from remote streams.
 */
public class StreamSubscription {
    /**
     * Unique identifier of the stream
     */
    public Long streamId;

    /**
     * Identifier of a specific track within the stream.
     * If not provided, the entire stream is targeted.
     */
    public String streamTrackId;

    /**
     * Constructs a new {@link StreamSubscription} instance.
     *
     * @param streamId      Unique identifier of the stream
     * @param streamTrackId Identifier of a specific track within the stream
     */
    public StreamSubscription(long streamId, String streamTrackId) {
        this.streamId = streamId;
        this.streamTrackId = streamTrackId;
    }

    /**
     * Constructs a new {@link StreamSubscription} instance.
     *
     * @param streamId Unique identifier of the stream
     */
    public StreamSubscription(long streamId) {
        this.streamId = streamId;
    }
}