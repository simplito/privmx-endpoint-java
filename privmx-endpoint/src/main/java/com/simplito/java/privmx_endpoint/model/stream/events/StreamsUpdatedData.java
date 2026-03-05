package com.simplito.java.privmx_endpoint.model.stream.events;


import java.util.List;

public class StreamsUpdatedData {
    public String room;
    public List<UpdatedStreamData> streams;

    public StreamsUpdatedData(String room, List<UpdatedStreamData> streams) {
        this.room = room;
        this.streams = streams;
    }
}