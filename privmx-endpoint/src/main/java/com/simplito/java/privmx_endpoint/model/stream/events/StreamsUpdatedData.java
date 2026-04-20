package com.simplito.java.privmx_endpoint.model.stream.events;


import java.util.List;

/**
 * Data received when a new participant joins the StreamRoom.
 * Contains a snapshot of all currently active streams in the room.
 */
public class StreamsUpdatedData {
    /**
     * Identifier of the StreamRoom
     */
    public String room;

    /**
     * List of streams in the room
     */
    public List<UpdatedStreamData> streams;

    /**
     * Constructs a new {@link StreamsUpdatedData} instance.
     *
     * @param room    Identifier of the StreamRoom
     * @param streams List of streams in the room
     */
    public StreamsUpdatedData(String room, List<UpdatedStreamData> streams) {
        this.room = room;
        this.streams = streams;
    }
}