package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Represents data of a published stream.
 */
public class PublishedStreamData {
    /**
     * Identifier of the room in which the stream is published
     */
    public String streamRoomId;

    /**
     * Information about the published stream
     */
    public StreamInfo stream;

    /**
     * Identifier of the user who published the stream
     */
    public String userId;

    /**
     * Constructs a new {@link PublishedStreamData} instance.
     *
     * @param streamRoomId Identifier of the room in which the stream is published
     * @param stream       Information about the published stream
     * @param userId       Identifier of the user who published the stream
     */
    public PublishedStreamData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}
