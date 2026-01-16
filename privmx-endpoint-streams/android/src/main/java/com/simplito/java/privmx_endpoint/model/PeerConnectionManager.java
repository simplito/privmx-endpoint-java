package com.simplito.java.privmx_endpoint.model;

import org.webrtc.PeerConnection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class PeerConnectionManager {

    private Map<String, Map<ConnectionType, JanusConnection>> connections = new HashMap<>();

    public PeerConnectionManager() {
    }

    public PeerConnectionManager(
            Function<String, PeerConnection> peerConnectionCreator,
            Function<Long, String> onTrickle
    ) {

    }
    public void initialize (String streamRoomId, ConnectionType connectionType, Long sessionId){
        if(connections.containsKey(streamRoomId)) {
            Map<ConnectionType, JanusConnection> roomConnections = connections.get(streamRoomId);

            assert roomConnections != null;
            if(roomConnections.getOrDefault(connectionType, null) == null){
                // throw exception
            }
        }

//        PeerConnection pc = createPeerConnection(streamRoomId);
//        setOnIceCandidate

    }

    public void initialize (String streamRoomId, ConnectionType connectionType){
        initialize(streamRoomId, connectionType, -1L);
    }

   public  void updateSessionForConnection(String streamRoomId, ConnectionType connectionType, long sessionId){

    }

//    boolean hasConnection(String streamRoomId, ConnectionType connectionType){
//
//    }
//
    public JanusConnection getConnectionWithSession(String streamRoomId, ConnectionType connectionType){
        return connections.get(streamRoomId).get(connectionType);
//        return new JanusConnection(null, 0, false);  // todo
    }

    private PeerConnection createPeerConnection(String streamRoomId){

        return null;        // todo
    }
}


