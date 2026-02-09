package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.MediaStreamTrack;

public interface TrackObserver {
    void onTrack(MediaStreamTrack track);
    void onVideoTrack(String trackId);
    void onRemoveVideoTrack(String trackId);
}
