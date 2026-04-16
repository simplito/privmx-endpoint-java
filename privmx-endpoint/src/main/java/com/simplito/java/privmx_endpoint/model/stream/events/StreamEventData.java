package com.simplito.java.privmx_endpoint.model.stream.events;

import java.util.List;

/**
 * Data describing the event that occurs when a participant joins a StreamRoom.
 */
public class StreamEventData {
    /**
     * Identifier of the stream room that was joined
     */
    public String streamRoomId;

    public List<Long> streamIds;

    /**
     * Identifier of the user who joined the StreamRoom
     */
    public String userId;

    /**
     * Constructs a new {@link StreamEventData} instance.
     *
     * @param streamRoomId Identifier of the stream room that was joined
     * @param streamIds
     * @param userId       Identifier of the user who joined the StreamRoom
     */
    public StreamEventData(String streamRoomId, List<Long> streamIds, String userId) {
        this.streamRoomId = streamRoomId;
        this.streamIds = streamIds;
        this.userId = userId;
    }
}
