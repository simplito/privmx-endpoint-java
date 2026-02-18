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

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            TrackObserver observer,
            Consumer<IceCandidate> onIceCandidate
    ) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.keyStore = store;
        this.trackObserver = observer;
        this.onIceCandidate = onIceCandidate;
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

    }

    @Override
    public void onAddTrack(RtpReceiver receiver, MediaStream[] mediaStreams) {
        if (trackObserver != null) {
            trackObserver.OnRemoteTrack(mediaStreams[0].getId(),receiver.track());
        }
    }

    @Override
    public void onTrack(RtpTransceiver transceiver) {
        RtpReceiver rtpReceiver = transceiver.getReceiver();
        if (peerConnectionFactory != null && rtpReceiver.track() != null && rtpReceiver.track().id() != null) {

            PmxFrameCryptorFactory.createPmxFrameCryptorForRtpReceiver(peerConnectionFactory, rtpReceiver, keyStore);
            frameCryptorMap.put(
                    rtpReceiver.track().id(),
                    PmxFrameCryptorFactory.createPmxFrameCryptorForRtpReceiver(
                            peerConnectionFactory,
                            rtpReceiver,
                            keyStore
                    )
            );
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
