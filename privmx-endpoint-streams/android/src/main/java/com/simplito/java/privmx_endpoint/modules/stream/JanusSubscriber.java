package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ConnectionType;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.SessionDescription;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class JanusSubscriber extends JanusConnection{
    public JanusSubscriber(PeerConnectionFactory pcFactory, PmxKeyStore keyStore, TrackObserver observer, BiConsumer<Long,String> onTrickle) {
        super(pcFactory, keyStore, ConnectionType.Publisher, observer, onTrickle,null);
    }

    public String createAnswer(String offerSdp, String type) {
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
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
