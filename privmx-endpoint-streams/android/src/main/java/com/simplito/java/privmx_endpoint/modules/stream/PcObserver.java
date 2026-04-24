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

import com.simplito.java.privmx_endpoint.model.stream.DecryptedDataChannelMessage;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PcObserver implements PeerConnection.Observer {
    private final Map<String, PmxFrameCryptor> frameCryptorMap = new HashMap<>();
    private final PmxKeyStore keyStore;
    private final PeerConnectionFactory peerConnectionFactory;
    private final RemoteStreamObserver trackObserver;
    private final Consumer<IceCandidate> onIceCandidate;
    private final Runnable onRenegotiationNeeded;
    private final Consumer<PeerConnection.IceConnectionState> onIceConnectionChange;
    private InternalDataChannelEncryption dataChannelEncryption = null;

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            RemoteStreamObserver observer,
            Consumer<IceCandidate> onIceCandidate,
            Runnable onRenegotiationNeeded,
            Consumer<PeerConnection.IceConnectionState> onIceConnectionChange,
            InternalDataChannelEncryption dataChannelEncryption
    ) {
        this.peerConnectionFactory = peerConnectionFactory;
        this.keyStore = store;
        this.trackObserver = observer;
        this.onIceCandidate = onIceCandidate;
        this.onRenegotiationNeeded = onRenegotiationNeeded;
        this.onIceConnectionChange = onIceConnectionChange;
        this.dataChannelEncryption = dataChannelEncryption;
    }

    public PcObserver(
            PeerConnectionFactory peerConnectionFactory,
            PmxKeyStore store,
            RemoteStreamObserver observer,
            Consumer<IceCandidate> onIceCandidate,
            Runnable onRenegotiationNeeded
    ) {
        this(peerConnectionFactory, store, observer, onIceCandidate, onRenegotiationNeeded, null, null);
    }

    @Override
    public void onSignalingChange(PeerConnection.SignalingState signalingState) {

    }

    @Override
    public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
        if (onIceConnectionChange != null) {
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
    public void onAddStream(MediaStream mediaStream) {
    }

    @Override
    public void onRemoveStream(MediaStream mediaStream) {

    }

    @Override
    public void onDataChannel(DataChannel dataChannel) {
        //TODO: Register decryptor to this channel
        dataChannel.registerObserver(new DataChannel.Observer() {
            @Override
            public void onBufferedAmountChange(long l) {

            }

            @Override
            public void onStateChange() {

            }

            @Override
            public void onMessage(DataChannel.Buffer buffer) {
                dataChannel.bufferedAmount();
                byte[] data;
                byte[] collectedData;
                if(dataChannelEncryption != null){
                    DecryptedDataChannelMessage message = dataChannelEncryption.decryptDataChannelMessage(buffer.data);
                    collectedData = messageSeqCollector.collect(message.data,message.seq);
                }else{
                    data = new byte[buffer.data.capacity()];
                    buffer.data.get(data);
                    collectedData = messageSeqCollector.collect(data,0);
                }
                if(collectedData != null) {
                    trackObserver.OnRemoteData(dataChannel.label(), collectedData);
                }
            }
        });
    }

    @Override
    public void onRenegotiationNeeded() {
        if (onRenegotiationNeeded != null) {
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
                            keyStore,
                            null
                    )
            );
            if (trackObserver != null) {
                String streamId = mediaStreams.length > 0 ? mediaStreams[0].getId() : null;
                if (streamId != null) {
                    trackObserver.OnRemoteTrack(streamId, track);
                }
            }
        }
    }

    @Override
    public void onRemoveTrack(RtpReceiver receiver) {
        MediaStreamTrack track = receiver.track();
        if (track != null) {
            PmxFrameCryptor removedCryptor = frameCryptorMap.remove(track.id());
            if (removedCryptor != null) {
                removedCryptor.dispose();
            }
        }
    }

    public void setDataChannelEncryption(InternalDataChannelEncryption dataChannelEncryption) {
        this.dataChannelEncryption = dataChannelEncryption;
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        frameCryptorMap.forEach((k, v) -> v.setOptions(options));
    }

    private final ChannelSeqCollector messageSeqCollector = new ChannelSeqCollector();

    private static class ChannelSeqCollector{
        private byte[] chunks = new byte[0];
        private long lastSeq = -1;
        synchronized byte[] collect(byte[] data, long seq){
            byte[] collectedMessage = null;
            if(seq <= lastSeq){
                collectedMessage = chunks;
                chunks = new byte[0];
            }
            byte[] combined = new byte[chunks.length + data.length];
            System.arraycopy(chunks,0,combined,0,chunks.length);
            System.arraycopy(data,0,combined,chunks.length,data.length);
            lastSeq = seq;
            chunks = combined;
            return collectedMessage;
        }
    }
}
