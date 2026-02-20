package com.simplito.java.privmx_endpoint.model.stream.events;


import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;

import java.util.List;

public class NewStreams {
    public String room;
    public List<StreamInfo> streams;
    public NewStreams(String room, List<StreamInfo> streams) {
        this.room = room;
        this.streams = streams;
    }
}
