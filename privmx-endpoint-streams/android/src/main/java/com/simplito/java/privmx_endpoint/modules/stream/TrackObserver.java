package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.MediaStreamTrack;

public interface TrackObserver {
    void OnRemoteTrack(String streamId, MediaStreamTrack track);
}
