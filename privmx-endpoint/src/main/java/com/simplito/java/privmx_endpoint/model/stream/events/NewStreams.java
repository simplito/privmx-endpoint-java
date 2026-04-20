package com.simplito.java.privmx_endpoint.model.stream.events;


import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;

import java.util.List;

/**
 * Data describing the event that occurs when new streams become available for subscription in a stream room.
 */
public class NewStreams {
    /**
     * Identifier of the stream room
     */
    public String room;

    /**
     * List of newly available streams that can be subscribed to
     */
    public List<StreamInfo> streams;

    /**
     * Constructs a new {@link NewStreams} instance.
     *
     * @param room    Identifier of the stream room
     * @param streams List of newly available streams that can be subscribed to
     */
    public NewStreams(String room, List<StreamInfo> streams) {
        this.room = room;
        this.streams = streams;
    }
}
