package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.MediaStreamTrack;

public interface RemoteStreamObserver {
    void OnRemoteTrack(String streamId, MediaStreamTrack track);
    //TODO: Maybe replace byte[] with ByteBuffer then developer should read data from it. This can optimize memory usage.
    void OnRemoteData(String streamId, byte[] msg);
}
