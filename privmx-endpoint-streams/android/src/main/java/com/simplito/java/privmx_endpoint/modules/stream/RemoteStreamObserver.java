package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.MediaStreamTrack;

public interface RemoteStreamObserver {
    void OnRemoteTrack(String streamId, MediaStreamTrack track);
    void OnRemoteData(String streamId, byte[] msg);
}
