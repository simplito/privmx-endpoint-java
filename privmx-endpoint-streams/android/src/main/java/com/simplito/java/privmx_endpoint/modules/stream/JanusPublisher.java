package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.AudioTrackInfo;
import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.VideoTrackInfo;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.RtpSender;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class JanusPublisher extends JanusConnection{
    public final Map<String, AudioTrackInfo> audioTracks = new HashMap<>();
    public final Map<String, VideoTrackInfo> videoTracks = new HashMap<>();
    //TODO: Add videoCapturer to VideoTrackInfo
    public final Map<String, VideoCapturer> videoCapturers = new HashMap<>();


    public JanusPublisher(PeerConnectionFactory pcFactory, PmxKeyStore keyStore, TrackObserver observer, BiConsumer<Long,String> onTrickle) {
        super(pcFactory, keyStore, ConnectionType.Publisher, observer, onTrickle);
    }

    public void addAudioTrack(org.webrtc.AudioTrack audioTrack) {
        synchronized (audioTracks) {
            RtpSender rtpSender2 = peerConnection.addTrack(audioTrack);
            PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                    peerConnectionFactory,
                    rtpSender2,
                    keyStore
                    // options ?
            );

            audioTracks.put(
                    audioTrack.id(),
                    new AudioTrackInfo(
                            audioTrack,
                            rtpSender2,
                            frameCryptor
                    )
            );
        }
    }

    public void addVideoTrack(
            org.webrtc.VideoTrack videoTrack,
            VideoCapturer videoCapturer
    ) {
        if (peerConnectionFactory != null) {
            synchronized (videoTracks) {
                RtpSender rtpSender = peerConnection.addTrack(videoTrack);
                PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                        peerConnectionFactory,
                        rtpSender,
                        keyStore
                        // options ?
                );

                videoTracks.put(
                        videoTrack.id(),
                        new VideoTrackInfo(
                                videoTrack,
                                rtpSender,
                                frameCryptor
                        )
                );
                if (videoCapturer != null) {
                    videoCapturers.put(videoTrack.id(), videoCapturer);
                }
            }
        }
    }

    public void addVideoTrack(org.webrtc.VideoTrack videoTrack) {
        addVideoTrack(videoTrack,null);
    }

    public void removeAudioTrack(String id) {
        synchronized (audioTracks) {
            AudioTrackInfo audioTrackInfo = audioTracks.get(id);
            if (audioTrackInfo == null) return;
            peerConnection.removeTrack(audioTrackInfo.sender);
            audioTracks.remove(id);
        }
    }

    public void removeVideoTrack(String id) {
        synchronized (videoTracks) {
            VideoTrackInfo videoTrackInfo = videoTracks.get(id);
            if (videoTrackInfo == null) return;
            peerConnection.removeTrack(videoTrackInfo.sender);
            videoTracks.remove(id);
            videoCapturers.remove(id);
        }
    }

    public String createOffer(){
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
        peerConnection.createOffer(new SdpObserver(res), new MediaConstraints());
        try {
            SessionDescription offer = res.get();
            peerConnection.setLocalDescription(new SdpObserver(null),offer);
            return offer.description;
        }catch (Exception e){
            throw new RuntimeException("Cannot create offer");
        }
    }

    public void setAnswer(String sdp){
        peerConnection.setRemoteDescription(new SdpObserver(null),new SessionDescription(SessionDescription.Type.ANSWER,sdp));
    }

    @Nullable
    public VideoCapturer getVideoCapturer(String trackId){
        return videoCapturers.get(trackId);
    }

    @Override
    public void close() {
        super.close();
        audioTracks.clear();
        videoTracks.clear();

        videoCapturers.forEach((k, v) -> v.dispose());
        videoCapturers.clear();
    }

    @Override
    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        this.videoTracks.values().forEach(it ->{
            it.frameCryptor.setOptions(options);
        });
        this.audioTracks.values().forEach(it ->{
            it.frameCryptor.setOptions(options);
        });
    }
}
