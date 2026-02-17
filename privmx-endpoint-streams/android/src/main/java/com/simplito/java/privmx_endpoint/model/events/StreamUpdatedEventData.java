package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.StreamInfo;
import com.simplito.java.privmx_endpoint.model.StreamTrackModification;

import java.util.List;

public class StreamUpdatedEventData {
    public final String streamRoomId;
    public final List<StreamInfo> streamsAdded;
    public final List<StreamInfo> streamsRemoved;
    public final List<StreamTrackModification> streamsModified;

    public StreamUpdatedEventData(String streamRoomId, List<StreamInfo> streamsAdded, List<StreamInfo> streamsRemoved, List<StreamTrackModification> streamsModified) {
        this.streamRoomId = streamRoomId;
        this.streamsAdded = streamsAdded;
        this.streamsRemoved = streamsRemoved;
        this.streamsModified = streamsModified;
    }
}