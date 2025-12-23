package com.simplito.java.privmx_endpoint.model;

import com.simplito.java.privmx_endpoint_streams.model.PeerConnection2;

public class JanusConnection {
    public final PeerConnection2 peerConnection;
    public final long sessionId;
    public final boolean hasSubscriptions;

    public JanusConnection(PeerConnection2 peerConnection, long sessionId, boolean hasSubscriptions) {
        this.peerConnection = peerConnection;
        this.sessionId = sessionId;
        this.hasSubscriptions = hasSubscriptions;
    }
}