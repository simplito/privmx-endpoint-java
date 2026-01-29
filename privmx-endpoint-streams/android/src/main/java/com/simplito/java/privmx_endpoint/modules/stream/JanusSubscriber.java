package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ConnectionType;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.SessionDescription;

import java.util.concurrent.CompletableFuture;

public class JanusSubscriber extends JanusConnection{
    public JanusSubscriber(PeerConnectionFactory pcFactory, PmxKeyStore keyStore, PeerConnection peerConnection) {
        super(pcFactory, keyStore, peerConnection, ConnectionType.Subscriber);
    }

    public String createAnswer(String offerSdp){
        //TODO: Check if is in correct state
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
        peerConnection.setRemoteDescription(new SdpObserver(null), new SessionDescription(SessionDescription.Type.OFFER,offerSdp));
        peerConnection.createAnswer(new SdpObserver(res), new MediaConstraints());
        try {
            SessionDescription answer = res.get();
            peerConnection.setLocalDescription(new SdpObserver(null),answer);
            return answer.description;
        }catch (Exception e){
            throw new RuntimeException("Cannot create answer");
        }
    }
}
