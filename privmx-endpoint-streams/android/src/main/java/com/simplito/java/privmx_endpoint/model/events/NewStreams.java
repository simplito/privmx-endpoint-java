package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.StreamInfo;

import java.util.List;

public class NewStreams {
    public final String room;
    public final List<StreamInfo> streams;

    public NewStreams(String room, List<StreamInfo> streams) {
        this.room = room;
        this.streams = streams;
    }
}
