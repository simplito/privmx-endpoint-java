package com.simplito.java.privmx_endpoint.model.stream.events;

/**
 * Data describing the event that occurs when a publisher stops publishing their stream in a StreamRoom.
 */
public class StreamUnpublishedEventData {
    /**
     * Identifier of the StreamRoom in which the stream was unpublished
     */
    public String streamRoomId;

    /**
     * Identifier of the stream that was unpublished
     */
    public Long streamId;

    /**
     * Constructs a new {@link StreamUnpublishedEventData} instance.
     *
     * @param streamRoomId Identifier of the StreamRoom in which the stream was unpublished
     * @param streamId     Identifier of the stream that was unpublished
     */
    public StreamUnpublishedEventData(String streamRoomId, Long streamId) {
        this.streamRoomId = streamRoomId;
        this.streamId = streamId;
    }
}