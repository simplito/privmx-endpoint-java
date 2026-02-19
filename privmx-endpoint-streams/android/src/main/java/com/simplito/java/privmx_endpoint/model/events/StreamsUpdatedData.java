package com.simplito.java.privmx_endpoint.model.events;

import java.util.List;

public class StreamsUpdatedData {
    public final String room;
    public final List<UpdatedStreamData> streams;

    public StreamsUpdatedData(String room, List<UpdatedStreamData> streams) {
        this.room = room;
        this.streams = streams;
    }
}