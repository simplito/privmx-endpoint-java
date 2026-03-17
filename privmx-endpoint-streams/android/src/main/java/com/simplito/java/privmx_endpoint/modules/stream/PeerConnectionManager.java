package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.stream.StreamHandle;

import org.webrtc.PeerConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;

class PeerConnectionManager implements AutoCloseable {
    private final Map<String, RoomJanusSession> sessions = new HashMap<>();
    private final Map<Long, String> sessionHandles = new HashMap<>();
    protected final PeerConnectionFactory pcFactory;
    private final BiConsumer<Long,String> onTrickle;

    PeerConnectionManager(
            PeerConnectionFactory pcFactory,
            BiConsumer<Long,String> onTrickle
    ) {
        this.pcFactory = pcFactory;
        this.onTrickle = onTrickle;
    }

    @NonNull
    public RoomJanusSession createSession(@NonNull String streamRoomId) {
        return Optional.ofNullable(
                sessions.putIfAbsent(streamRoomId, new RoomJanusSession(streamRoomId, pcFactory, onTrickle))
        ).orElse(Objects.requireNonNull(sessions.get(streamRoomId)));
    }

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
    ) {
        sessionHandles.put(handle.getValue(), roomID);
    }

    public void leaveStreamRoom(@NonNull String streamRoomId) {
        RoomJanusSession session = sessions.remove(streamRoomId);
        if (session != null) {
            session.close();
        }
    }

    @Override
    public void close() throws Exception {
        sessions.values().forEach(RoomJanusSession::close);
        sessions.clear();
        sessionHandles.clear();
        pcFactory.dispose();
    }
}

