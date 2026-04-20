package com.simplito.java.privmx_endpoint.model.stream.events;

/**
 * Data describing the event that occurs when a StreamRoom is deleted.
 */
public class StreamRoomDeletedEventData {
    /**
     * Identifier of the StreamRoom that was deleted
     */
    public String streamRoomId;

    /**
     * Constructs a new {@link StreamRoomDeletedEventData} instance.
     *
     * @param streamRoomId Identifier of the StreamRoom that was deleted
     */
    public StreamRoomDeletedEventData(String streamRoomId) {
        this.streamRoomId = streamRoomId;
    }
}
