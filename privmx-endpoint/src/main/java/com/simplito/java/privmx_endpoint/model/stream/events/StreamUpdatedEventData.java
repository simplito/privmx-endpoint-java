package com.simplito.java.privmx_endpoint.model.stream.events;


import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;
import com.simplito.java.privmx_endpoint.model.stream.StreamTrackModification;

import java.util.List;

/**
 * Data describing changes that occurred within a stream room during an active session.
 * Received when an existing publisher adds, removes, or modifies their tracks.
 */
public class StreamUpdatedEventData {
    /**
     * Identifier of the stream room in which the update occurred
     */
    public String streamRoomId;

    /**
     * List of streams that were added to the stream room
     */
    public List<StreamInfo> streamsAdded;

    /**
     * List of streams that were removed from the stream room
     */
    public List<StreamInfo> streamsRemoved;

    /**
     * List of streams that were modified in the stream room
     */
    public List<StreamTrackModification> streamsModified;

    /**
     * Constructs a new {@link StreamUpdatedEventData} instance.
     *
     * @param streamRoomId    Identifier of the stream room in which the update occurred
     * @param streamsAdded    List of streams that were added to the stream room
     * @param streamsRemoved  List of streams that were removed from the stream room
     * @param streamsModified List of streams that were modified in the stream room
     */
    public StreamUpdatedEventData(String streamRoomId, List<StreamInfo> streamsAdded, List<StreamInfo> streamsRemoved, List<StreamTrackModification> streamsModified) {
        this.streamRoomId = streamRoomId;
        this.streamsAdded = streamsAdded;
        this.streamsRemoved = streamsRemoved;
        this.streamsModified = streamsModified;
    }
}