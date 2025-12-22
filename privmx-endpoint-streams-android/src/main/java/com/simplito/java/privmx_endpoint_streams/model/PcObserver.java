package com.simplito.java.privmx_endpoint_streams.model;

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

public class PcObserver implements PeerConnection.Observer {
    Map<String, PmxFrameCryptor> frameCryptorMap = new HashMap<>();
    PmxKeyStore keyStore;
    private PeerConnectionFactory peerConnectionFactory;
    private String streamRoomId;
    private PmxFrameCryptor.PmxFrameCryptorOptions options;

    private BiConsumer<List<MediaStream>, RtpReceiver> onAddTrack;
    private Consumer<String> onVideoTrack;
    private OnFrameCallback onFrameCallback = null;
    private Consumer<String> onRemoveVideoTrack = null;

    private Consumer<PeerConnection.SignalingState> onSignalingState;
    private Consumer<PeerConnection.PeerConnectionState> onPeerConnectionState;
    private Consumer<PeerConnection.IceGatheringState> onIceGatheringState;
    private Consumer<PeerConnection.IceConnectionState> onIceConnectionState;
    private Consumer<IceCandidate> onIceCandidate;
    private Consumer<MediaStream> onAddStream;
    private Consumer<MediaStream> onRemoveStream;
    private Consumer<DataChannel> onDataChannel;
    private Runnable onRenegotiationNeeded;
    private Consumer<RtpTransceiver> onTrack;
    private Consumer<RtpReceiver> onRemoveTrack;


    public PcObserver(PeerConnectionFactory peerConnectionFactory, String streamRoomId, PmxKeyStore store, PmxFrameCryptor.PmxFrameCryptorOptions options) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.streamRoomId = streamRoomId;
        this.keyStore = store;
        this.options = options;
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

    }

    @Override
    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {

    }

    @Override
    public void onAddStream(MediaStream mediaStream) {

    }

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
        if (peerConnectionFactory != null && receiver.track() != null && receiver.track().id().equals(null)) {
            frameCryptorMap.put(
                    receiver.track().id(),
                    PmxFrameCryptorFactory.createPmxFrameCryptorForRtpReceiver(
                            peerConnectionFactory,
                            receiver,
                            keyStore
                    )
            );
        }
        if (onAddTrack != null) {
            onAddTrack.accept(Arrays.asList(mediaStreams), receiver);

            if (Objects.equals(receiver.track().kind(), MediaStreamTrack.VIDEO_TRACK_KIND)) {
                onVideoTrack.accept(receiver.track().id());
            }
        }
    }

    @Override
    public void onTrack(RtpTransceiver transceiver) {
        // todo -check
//        RtpReceiver rtpReceiver = transceiver.getReceiver();
//        MediaStreamTrack track = rtpReceiver.track();
//        if (trackObserver != null) trackObserver.onTrack(track);
//        // TODO: check if no duplication
//        PmxFrameCryptorFactory.createPmxFrameCryptorForRtpReceiver(peerConnectionFactory, rtpReceiver, store);

        onTrack.accept(transceiver);
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        this.options = options;
        frameCryptorMap.forEach((k, v) -> v.setOptions(options));
    }

    public void setOnFrame(OnFrameCallback onFrame) {
        this.onFrameCallback = onFrame;
    }

    public void setOnRemoveVideoTrack(Consumer<String> onRemoveVideoTrack) {
        this.onRemoveVideoTrack = onRemoveVideoTrack;
    }
}
