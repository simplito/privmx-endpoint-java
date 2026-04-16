package com.simplito.java.privmx_endpoint.model.stream.events;

/**
 * Data describing the event that occurs when a participant leaves a StreamRoom.
 */
public class StreamLeftEventData {
    /**
     * Identifier of the StreamRoom that was left
     */
    public String streamRoomId;

    /**
     * Identifier of the stream that was left
     */
    public Long streamId;

    /**
     * Identifier of the user who left the StreamRoom
     */
    public String userId;

    /**
     * Constructs a new {@link StreamLeftEventData} instance.
     *
     * @param streamRoomId Identifier of the StreamRoom that was left
     * @param streamId     Identifier of the stream that was left
     */
    public StreamLeftEventData(String streamRoomId, Long streamId, String userId) {
        this.streamRoomId = streamRoomId;
        this.streamId = streamId;
        this.userId = userId;
    }
}
