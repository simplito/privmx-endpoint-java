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

public class PcObserver implements PeerConnection.Observer {
    private final Map<String, PmxFrameCryptor> frameCryptorMap = new HashMap<>();
    private final PmxKeyStore keyStore;
    private final PeerConnectionFactory peerConnectionFactory;
    private final TrackObserver trackObserver;
    private final Consumer<IceCandidate> onIceCandidate;
    private final Runnable onRenegotiationNeeded;
    private final Consumer<PeerConnection.IceConnectionState> onIceConnectionChange;

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            TrackObserver observer,
            Consumer<IceCandidate> onIceCandidate,
            Runnable onRenegotiationNeeded,
            Consumer<PeerConnection.IceConnectionState> onIceConnectionChange
    ) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.keyStore = store;
        this.trackObserver = observer;
        this.onIceCandidate = onIceCandidate;
        this.onRenegotiationNeeded = onRenegotiationNeeded;
        this.onIceConnectionChange = onIceConnectionChange;
    }

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            TrackObserver observer,
            Consumer<IceCandidate> onIceCandidate,
            Runnable onRenegotiationNeeded
    ){
        this(peerConnectionFactory,store,observer,onIceCandidate,onRenegotiationNeeded,null);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        if(onIceConnectionChange != null) {
            onIceConnectionChange.accept(iceConnectionState);
        }
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
        MediaStreamTrack track = receiver.track();
        if(track != null) {
            PmxFrameCryptor removedCryptor = frameCryptorMap.remove(track.id());
            if(removedCryptor != null) {
                removedCryptor.dispose();
            }
        }
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        frameCryptorMap.forEach((k, v) -> v.setOptions(options));
    }
}
