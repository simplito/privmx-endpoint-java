package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.PcObserver;

import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.util.concurrent.CompletableFuture;

public class JanusConnection {
    protected final PeerConnection peerConnection;
    protected final PeerConnectionFactory peerConnectionFactory;
    protected PcObserver observer;
    protected PmxKeyStore keyStore;
    public final ConnectionType connectionType;
    long sessionId = 0L;

    public JanusConnection(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            PeerConnection peerConnection,
            ConnectionType connectionType
    ) {
        this.peerConnection = peerConnection;
        this.connectionType = connectionType;
        this.peerConnectionFactory = pcFactory;
        this.keyStore = keyStore;
    }

    //TODO: Does sessionID is required here?
//    public JanusConnection(PeerConnection peerConnection, long sessionId, boolean hasSubscriptions) {
//        this.peerConnection = peerConnection;
//        this.sessionId = sessionId;
//        this.hasSubscriptions = hasSubscriptions;
//    }

    static class SdpObserver implements org.webrtc.SdpObserver {
        private final CompletableFuture<SessionDescription> res;

        SdpObserver(CompletableFuture<SessionDescription> res) {
            this.res = res;
        }

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            res.complete(sessionDescription);
        }

        @Override
        public void onSetSuccess() {

        }

        @Override
        public void onCreateFailure(String s) {

        }

        @Override
        public void onSetFailure(String s) {

        }
    }

    public void close(){
        peerConnection.dispose();
    }
}