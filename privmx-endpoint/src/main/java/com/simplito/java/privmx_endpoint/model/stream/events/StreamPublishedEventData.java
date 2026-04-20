package com.simplito.java.privmx_endpoint.model.stream.events;


import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;

/**
 * Data describing the event that occurs when a publisher starts publishing their stream in a StreamRoom.
 */
public class StreamPublishedEventData {
    /**
     * Identifier of the stream room in which the stream was published
     */
    public String streamRoomId;

    /**
     * Detailed information about the published stream
     */
    public StreamInfo stream;

    /**
     * Identifier of the user who published the stream
     */
    public String userId;

    /**
     * Constructs a new {@link StreamPublishedEventData} instance.
     *
     * @param streamRoomId Identifier of the stream room in which the stream was published
     * @param stream       Detailed information about the published stream
     * @param userId       Identifier of the user who published the stream
     */
    public StreamPublishedEventData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}