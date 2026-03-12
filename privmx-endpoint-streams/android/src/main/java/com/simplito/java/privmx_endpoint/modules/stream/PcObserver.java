package com.simplito.java.privmx_endpoint.modules.stream;


import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaStream;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.RtpReceiver;
import org.webrtc.RtpTransceiver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

//TODO: Fix warnings
public class PcObserver implements PeerConnection.Observer {
    Map<String, PmxFrameCryptor> frameCryptorMap = new HashMap<>();
    PmxKeyStore keyStore;
    private PeerConnectionFactory peerConnectionFactory;
    public TrackObserver trackObserver;

    private BiConsumer<List<MediaStream>, RtpReceiver> onAddTrack;
    private Consumer<String> onVideoTrack;

    private Consumer<IceCandidate> onIceCandidate;
    private final Map<String,String> streamIdsByTracks = new HashMap<>();
    private final Runnable onRenegotiationNeeded;

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            TrackObserver observer,
            Consumer<IceCandidate> onIceCandidate,
            Runnable onRenegotiationNeeded
    ) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.keyStore = store;
        this.trackObserver = observer;
        this.onIceCandidate = onIceCandidate;
        this.onRenegotiationNeeded = onRenegotiationNeeded;
    }

    public void setOnAddTrack(BiConsumer<List<MediaStream>, RtpReceiver> onAddTrack) {
        this.onAddTrack = onAddTrack;
    }

    public void setOnVideoTrack(Consumer<String> onVideoTrack) {
        this.onVideoTrack = onVideoTrack;
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {

    }

    @Override
    public void onIceConnectionReceivingChange(boolean b) {

    }

    @Override
    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {

    }

    @Override
    public void onIceCandidate(IceCandidate iceCandidate) {
        onIceCandidate.accept(iceCandidate);
    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {}

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {

    }

    @Override
    public void onRenegotiationNeeded() {
        if(onRenegotiationNeeded != null){
            onRenegotiationNeeded.run();
        }
    }

    @Override
    public void onAddTrack(RtpReceiver receiver, MediaStream[] mediaStreams) {
        MediaStreamTrack track = receiver.track();
        if (peerConnectionFactory != null && track != null && track.id() != null) {
            frameCryptorMap.put(
                    track.id(),
                    PmxFrameCryptorFactory.createPmxFrameCryptorForRtpReceiver(
                            peerConnectionFactory,
                            receiver,
                            keyStore
                    )
            );
            if(trackObserver != null){
                String streamId = mediaStreams.length > 0 ? mediaStreams[0].getId() : null;
                if(streamId != null) {
                    trackObserver.OnRemoteTrack(streamId, track);
                }
            }
        }
    }

    @Override
    public void onRemoveTrack(RtpReceiver receiver) {
        //TODO: cleanup track cryptors (?)
//        onRemoveTrack.accept(receiver.track());
        receiver.dispose();
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        frameCryptorMap.forEach((k, v) -> v.setOptions(options));
    }
}
