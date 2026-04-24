package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ConnectionType;

import org.webrtc.*;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class JanusSubscriber extends JanusConnection {
    private DataChannel bootstrapDataChannel = null;
    public JanusSubscriber(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            RemoteStreamObserver observer,
            BiConsumer<Long, String> onTrickle,
            InternalDataChannelEncryption dataChannelEncryption
    ) {
        super(pcFactory, keyStore, ConnectionType.Subscriber, observer, onTrickle,null,dataChannelEncryption);
    }

    public synchronized String createAnswer(String offerSdp, String type) {
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
        if(bootstrapDataChannel == null || bootstrapDataChannel.state() == DataChannel.State.CLOSED) {
            //TODO: Check if this bootstrap channel will be closed then we should create a new one?
            // Probably yes, because datachannel should be closed when connection is lost and we wont to reconnect
            bootstrapDataChannel = peerConnection.createDataChannel("JanusDataChannel", new DataChannel.Init());
        }
        peerConnection.setRemoteDescription(new SdpObserver(null), new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), offerSdp));
        peerConnection.createAnswer(new SdpObserver(res), new MediaConstraints());
        try {
            SessionDescription answer = res.get();
            peerConnection.setLocalDescription(new SdpObserver(null), answer);
            return answer.description;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create answer");
        }
    }
}
