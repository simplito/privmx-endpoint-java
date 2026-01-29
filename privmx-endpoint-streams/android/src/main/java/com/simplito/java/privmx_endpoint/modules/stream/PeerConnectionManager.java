package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.StreamHandle;

import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class PeerConnectionManager {
    private Map<String, RoomJanusSession> sessions = new HashMap<>();
    private Map<Long, String> sessionHandles = new HashMap<>();
    private final PeerConnectionFactory pcFactory;

    public PeerConnectionManager(PeerConnectionFactory pcFactory) {
        this.pcFactory = pcFactory;
    }

    @NonNull
    public RoomJanusSession createSession(@NonNull String streamRoomId) {
        return Optional.ofNullable(
                sessions.putIfAbsent(streamRoomId,new RoomJanusSession(streamRoomId,pcFactory))
        ).orElse(Objects.requireNonNull(sessions.get(streamRoomId)));
    }

    //TODO: Maybe should return null, and creating session should be move in other method
    //TODO: When user call e.g. StreamApi.dropBrokenFrames and want access room session without creating a new one
    @Nullable
    public RoomJanusSession getSession(@NonNull String streamRoomId) {
        return sessions.get(streamRoomId);
    }


    @Nullable
    public RoomJanusSession getSession(@NonNull StreamHandle handle) {
        String streamRoomId = sessionHandles.get(handle.getValue());
        return sessions.get(streamRoomId);
    }

    public void createHandleToRoom(
            StreamHandle handle,
            @NonNull String roomID
    ){
        sessionHandles.put(handle.getValue(),roomID);
    }

    public void leaveStreamRoom(@NonNull String streamRoomId){
        RoomJanusSession session = sessions.remove(streamRoomId);
        if(session == null) return;
        JanusConnection subscriber = session.getSubscriber();
        if (subscriber != null) {
            subscriber.peerConnection.dispose();
        }
        JanusConnection publisher = session.getPublisher();
        if(publisher != null){
            publisher.peerConnection.dispose();
        }
    }


//    /**
//     * @throws NullPointerException if session not found
//     */
//    @Nullable
//    public JanusConnection getConnection(@NonNull String streamRoomId, @NonNull ConnectionType connectionType) throws NullPointerException {
//        RoomJanusSession session = getSession(streamRoomId);
//        switch (connectionType) {
//            case Publisher:
//                return session.getPublisher();
//            case Subscriber:
//                return session.getSubscriber();
//            default:
//                return null;
//        }
//    }
}

// Old implementation
//public class PeerConnectionManager {
//
//    private Map<String, Map<ConnectionType, JanusConnection>> connections = new HashMap<>();
//
//    public PeerConnectionManager() {
//    }
//
//    public PeerConnectionManager(
//            Function<String, PeerConnection> peerConnectionCreator,
//            Function<Long, String> onTrickle
//    ) {
//
//    }
//    public void initialize (String streamRoomId, ConnectionType connectionType, Long sessionId){
//        if(connections.containsKey(streamRoomId)) {
//            Map<ConnectionType, JanusConnection> roomConnections = connections.get(streamRoomId);
//
//            assert roomConnections != null;
//            if(roomConnections.getOrDefault(connectionType, null) == null){
//                // throw exception
//            }
//        }
//
/// /        PeerConnection pc = createPeerConnection(streamRoomId);
/// /        setOnIceCandidate
//
//    }
//
//    public void initialize (String streamRoomId, ConnectionType connectionType){
//        initialize(streamRoomId, connectionType, -1L);
//    }
//
//   public  void updateSessionForConnection(String streamRoomId, ConnectionType connectionType, long sessionId){
//
//    }
//
/// /    boolean hasConnection(String streamRoomId, ConnectionType connectionType){
/// /
/// /    }
/// /
//    public JanusConnection getConnectionWithSession(String streamRoomId, ConnectionType connectionType){
//        return connections.get(streamRoomId).get(connectionType);
////        return new JanusConnection(null, 0, false);  // todo
//    }
//
//    private PeerConnection createPeerConnection(String streamRoomId){
//
//        return null;        // todo
//    }
//}


