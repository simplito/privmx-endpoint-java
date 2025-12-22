package com.simplito.java.privmx_endpoint.model;

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