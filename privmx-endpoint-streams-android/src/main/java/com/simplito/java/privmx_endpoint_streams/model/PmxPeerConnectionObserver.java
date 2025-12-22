//package com.simplito.java.privmx_endpoint_streams;
//
//import org.webrtc.CandidatePairChangeEvent;
//import org.webrtc.DataChannel;
//import org.webrtc.FrameCryptorAlgorithm;
//import org.webrtc.FrameCryptorFactory;
//import org.webrtc.IceCandidate;
//import org.webrtc.IceCandidateErrorEvent;
//import org.webrtc.MediaStream;
//import org.webrtc.PeerConnection;
//import org.webrtc.PeerConnectionFactory;
//import org.webrtc.PmxFrameCryptor;
//import org.webrtc.PmxFrameCryptorFactory;
//import org.webrtc.PmxKeyStore;
//import org.webrtc.RtpReceiver;
//import org.webrtc.RtpTransceiver;
//
//public class PmxPeerConnectionObserver implements PeerConnection.Observer {
//
//    public PeerConnectionFactory peerConnectionFactory;
//    public String streamRoomId;
//    public PmxKeyStore store;
//    public PmxFrameCryptor.PmxFrameCryptorOptions options;
//
//    public PmxPeerConnectionObserver(PeerConnectionFactory peerConnectionFactory, String streamRoomId, PmxKeyStore store, PmxFrameCryptor.PmxFrameCryptorOptions options) {
//        this.peerConnectionFactory = peerConnectionFactory;
//        this.streamRoomId = streamRoomId;
//        this.store = store;
//        this.options = options;
//    }
//
//    @Override
//    public void onSignalingChange(PeerConnection.SignalingState signalingState) {
//
//    }
//
//    @Override
//    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
//
//    }
//
//    @Override
//    public void onStandardizedIceConnectionChange(PeerConnection.IceConnectionState newState) {
//        PeerConnection.Observer.super.onStandardizedIceConnectionChange(newState);
//    }
//
//    @Override
//    public void onConnectionChange(PeerConnection.PeerConnectionState newState) {
//        PeerConnection.Observer.super.onConnectionChange(newState);
//    }
//
//    @Override
//    public void onIceConnectionReceivingChange(boolean b) {
//
//    }
//
//    @Override
//    public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
//
//    }
//
//    @Override
//    public void onIceCandidate(IceCandidate iceCandidate) {
//
//    }
//
//    @Override
//    public void onIceCandidateError(IceCandidateErrorEvent event) {
//        PeerConnection.Observer.super.onIceCandidateError(event);
//    }
//
//    @Override
//    public void onIceCandidatesRemoved(IceCandidate[] iceCandidates) {
//
//    }
//
//    @Override
//    public void onSelectedCandidatePairChanged(CandidatePairChangeEvent event) {
//        PeerConnection.Observer.super.onSelectedCandidatePairChanged(event);
//    }
//
//    @Override
//    public void onAddStream(MediaStream mediaStream) {
//
//    }
//
//    @Override
//    public void onRemoveStream(MediaStream mediaStream) {
//
//    }
//
//    @Override
//    public void onDataChannel(DataChannel dataChannel) {
//
//    }
//
//    @Override
//    public void onRenegotiationNeeded() {
//
//    }
//
//    @Override
//    public void onAddTrack(RtpReceiver receiver, MediaStream[] mediaStreams) {
//
//        FrameCryptorFactory.createFrameCryptorForRtpReceiver(peerConnectionFactory,receiver, FrameCryptorAlgorithm.AES_GCM, store);
//        PeerConnection.Observer.super.onAddTrack(receiver, mediaStreams);
//    }
//
//    @Override
//    public void onRemoveTrack(RtpReceiver receiver) {
//        PeerConnection.Observer.super.onRemoveTrack(receiver);
//    }
//
//    @Override
//    public void onTrack(RtpTransceiver transceiver) {
//        PeerConnection.Observer.super.onTrack(transceiver);
//    }
//}
