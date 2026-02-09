package com.simplito.java.privmx_endpoint.streams.model.events;


import com.simplito.java.privmx_endpoint.streams.model.StreamInfo;
import com.simplito.java.privmx_endpoint.streams.model.StreamTrackModification;

import java.util.List;

public class StreamUpdatedEventData {
    public String streamRoomId;
    public List<StreamInfo> streamsAdded;
    public List<StreamInfo> streamsRemoved;
    public List<StreamTrackModification> streamsModified;

    public StreamUpdatedEventData(String streamRoomId, List<StreamInfo> streamsAdded, List<StreamInfo> streamsRemoved, List<StreamTrackModification> streamsModified) {
        this.streamRoomId = streamRoomId;
        this.streamsAdded = streamsAdded;
        this.streamsRemoved = streamsRemoved;
        this.streamsModified = streamsModified;
    }
}