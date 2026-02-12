package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class TrackFactory {
    private final PeerConnectionFactory factory;
    TrackFactory(JanusPublisher publisher){
        factory = publisher.peerConnectionFactory;
    }

    //TODO: Maybe creating sources should be hidden
    public VideoSource createVideoSource(boolean isScreenCast){
        return factory.createVideoSource(isScreenCast);
    }

    //TODO: Maybe creating sources should be hidden
    public VideoSource createVideoSource(boolean isScreenCast, boolean alignTimestamps){
        return factory.createVideoSource(isScreenCast,alignTimestamps);
    }

    //TODO: Maybe creating sources should be hidden
    public AudioSource createAudioSource(){
        return factory.createAudioSource(new MediaConstraints());
    }

    public VideoTrack createVideoTrack(
            String id,
            VideoSource videoSource
    ){
        return factory.createVideoTrack(id,videoSource);
    }

    public AudioTrack createAudioTrack(
            String id,
            AudioSource audioSource
    ){
        return factory.createAudioTrack(id,audioSource);
    }

}