package com.simplito.java.privmx_endpoint.modules.stream;


import android.content.Context;

import androidx.annotation.NonNull;

import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.stream.Settings;
import com.simplito.java.privmx_endpoint.model.stream.StreamHandle;
import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;
import com.simplito.java.privmx_endpoint.model.stream.StreamPublishResult;
import com.simplito.java.privmx_endpoint.model.stream.StreamRoom;
import com.simplito.java.privmx_endpoint.model.stream.StreamSubscription;
import com.simplito.java.privmx_endpoint.model.stream.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.stream.events.eventTypes.StreamEventType;

import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StreamApi implements AutoCloseable{
    private final StreamApiLow api;
    private final PeerConnectionManager pcManager;
    public final TrackFactory trackFactory;

    private static PeerConnectionFactory DefaultPeerConnectionFactory(
            Context appContext,
            EglBase eglBase,
            PeerConnectionFactory.Options options
    ) {
        AudioDeviceModule adm = JavaAudioDeviceModule
                .builder(appContext)
                .createAudioDeviceModule();

        boolean enableH264HighProfile = false;

        VideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(),
                true,  /* enableIntelVp8Encoder */
                enableH264HighProfile
        );

        VideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(
                eglBase.getEglBaseContext()
        );

        PeerConnectionFactory factory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(decoderFactory)
                .setVideoEncoderFactory(encoderFactory)
                .setOptions(options)
                .setAudioDeviceModule(adm)
                .createPeerConnectionFactory();

        adm.release();
        return factory;
    }

    public StreamApi(
            @NonNull Context appContext,
            @NonNull EglBase rootEglBase,
            @NonNull StreamApiLow api
    ) {
        this.api = api;
        PeerConnectionFactory factory = DefaultPeerConnectionFactory(appContext, rootEglBase, new PeerConnectionFactory.Options());
        pcManager = new PeerConnectionManager(
                factory,
                (sessionId, rtcConfiguration) -> {
                    if (sessionId != null) {
                        this.api.trickle(sessionId, rtcConfiguration);
                    }
                },
                (s,s2)->{
                    this.api.setNewOfferOnReconfigure(s,s2);
                }

        );
        trackFactory = new TrackFactory(pcManager);
    }

    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    ) {
        return api.createStreamRoom(contextId, users, managers, publicMeta, privateMeta, policies);
    }

    public void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey,
            ContainerPolicy policies
    ) {
        api.updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, policies);
    }

    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String sortBy
    ) {
        return api.listStreamRooms(contextId, skip, limit, sortOrder, lastId, sortBy);
    }

    public StreamRoom getStreamRoom(String streamRoomId) {
        return api.getStreamRoom(streamRoomId);
    }

    public void deleteStreamRoom(String streamRoomId) {
        api.deleteStreamRoom(streamRoomId);
    }

    public List<StreamInfo> listStreams(String streamRoomId) {
        return api.listStreams(streamRoomId);
    }

    public void joinStreamRoom(
            String streamRoomId
    ) {
        RoomJanusSession session = pcManager.createSession(streamRoomId);
        api.joinStreamRoom(streamRoomId, session.webrtc);
    }

    public void leaveStreamRoom(String streamRoomId) {
        pcManager.leaveStreamRoom(streamRoomId);
        api.leaveStreamRoom(streamRoomId);
    }

    public StreamHandle createStream(String streamRoomId) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session == null)
            throw new IllegalStateException("Session to this room is not exists. Call joinStreamRoom first");
        try {
            session.createPublisher();
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Publisher is now active, try use modifyRemoteStreamsSubscriptions");
        }

        StreamHandle handle = api.createStream(streamRoomId);
        pcManager.createHandleToRoom(handle, streamRoomId);
        return handle;
    }

    /**
     * @param streamHandle
     * @param track
     * @throws IllegalStateException if call addTrack before call createStream
     */
    public void addTrack(
            @NonNull StreamHandle streamHandle,
            MediaStreamTrack track
    ) throws IllegalStateException {
        Objects.requireNonNull(streamHandle);
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream not exists. Create stream first.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        switch (track.kind()) {
            case MediaStreamTrack.VIDEO_TRACK_KIND: {
                publisher.addVideoTrack((VideoTrack) track);
                break;
            }
            case MediaStreamTrack.AUDIO_TRACK_KIND: {
                publisher.addAudioTrack((AudioTrack) track);
                break;
            }
        }
    }

    public void setTrackObserver(
            @NonNull String roomId,
            TrackObserver observer,
            String streamId
    ) {
        Objects.requireNonNull(roomId);
        RoomJanusSession session = pcManager.getSession(roomId);
        if (session == null)
            throw new IllegalStateException("Session to this room is not exists. Call joinStreamRoom first.");
        session.setTrackObserver(streamId, observer);
    }

    public void setConnectionStateObserver(
            @NonNull String roomId,
            Consumer<PeerConnection.IceConnectionState> observer
    ) {
        Objects.requireNonNull(roomId);
        RoomJanusSession session = pcManager.getSession(roomId);
        if (session == null)
            throw new IllegalStateException("Session to this room is not exists. Call joinStreamRoom first.");
        session.setOnConnectionChange(observer);
    }

    public void setTrackObserver(
            String roomId,
            TrackObserver observer
    ) {
        setTrackObserver(roomId, observer, null);
    }

    /**
     * @param streamHandle
     * @param track
     * @throws IllegalStateException when Stream with this StreamHandle doesn't exist.
     */
    public void removeTrack(
            @NonNull StreamHandle streamHandle,
            @NonNull MediaStreamTrack track
    ) throws IllegalStateException {
        Objects.requireNonNull(streamHandle);
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream with this StreamHandle doesn't exist.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        if (track instanceof AudioTrack) {
            publisher.removeAudioTrack(track.id());
        } else if (track instanceof VideoTrack) {
            publisher.removeVideoTrack(track.id());
        }
    }

    public StreamPublishResult publishStream(@NonNull StreamHandle streamHandle) {
        Objects.requireNonNull(streamHandle);
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream with this StreamHandle doesn't exist.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        publisher.setRTCConfiguration(getRTCConfiguration());
        return api.publishStream(streamHandle);
    }

    public StreamPublishResult updateStream(@NonNull StreamHandle streamHandle) {
        Objects.requireNonNull(streamHandle);
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream with this StreamHandle doesn't exist.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        publisher.setRTCConfiguration(getRTCConfiguration());
        return api.updateStream(streamHandle);
    }

    public void unpublishStream(@NonNull StreamHandle streamHandle) {
        Objects.requireNonNull(streamHandle);
        api.unpublishStream(streamHandle);
    }

    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions
    ) {
        subscribeToRemoteStreams(streamRoomId, subscriptions, new Settings());
    }

    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions,
            Settings options
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session == null)
            throw new IllegalStateException("No active session to this Stream Room. Join stream room first");
        try {
            session.createSubscriber();
        } catch (IllegalStateException ignored) {
        }
        if (session.getSubscriber() == null)
            throw new IllegalStateException("This streamRoom has not created companion subscriber.");
        session.getSubscriber().setRTCConfiguration(getRTCConfiguration());
        api.subscribeToRemoteStreams(streamRoomId, subscriptions, options);
    }

    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove
    ) {
        modifyRemoteStreamsSubscriptions(
                streamRoomId,
                subscriptionsToAdd,
                subscriptionsToRemove,
                new Settings()
        );
    }

    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove,
            Settings options
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session == null)
            throw new IllegalStateException("No active session to this Stream Room. Join stream room first");
        if (session.getSubscriber() == null)
            throw new IllegalStateException("This streamRoom has not created companion subscriber.");
        session.getSubscriber().setRTCConfiguration(getRTCConfiguration());
        api.modifyRemoteStreamsSubscriptions(
                streamRoomId,
                subscriptionsToAdd,
                subscriptionsToRemove,
                options
        );
    }

    public void unsubscribeFromRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToRemove
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session == null)
            throw new IllegalStateException("No active session to this Stream Room. Join stream room first");
        if (session.getSubscriber() == null)
            throw new IllegalStateException("This streamRoom has not created companion subscriber.");
        session.getSubscriber().setRTCConfiguration(getRTCConfiguration());
        api.unsubscribeFromRemoteStreams(
                streamRoomId,
                subscriptionsToRemove
        );
    }

    public void dropBrokenFrames(
            String streamRoomId,
            boolean enable
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session != null) {
            PmxFrameCryptor.PmxFrameCryptorOptions options = new PmxFrameCryptor.PmxFrameCryptorOptions();
            options.dropFrameIfCryptionFailed = enable;
            session.setFrameCryptorOptions(options);
        }
    }

    public List<String> subscribeFor(List<String> subscriptionQueries) {
        return api.subscribeFor(subscriptionQueries);
    }

    public void unsubscribeFrom(List<String> subscriptionIds) {
        api.unsubscribeFrom(subscriptionIds);
    }

    public String buildSubscriptionQuery(
            StreamEventType eventType,
            StreamEventSelectorType selectorType,
            String selectorId
    ) {
        return api.buildSubscriptionQuery(
                eventType,
                selectorType,
                selectorId
        );
    }

    @Override
    public void close() throws Exception {
        pcManager.getRoomIds().forEach(this::leaveStreamRoom);
        pcManager.close();
        api.close();
    }

    private List<PeerConnection.IceServer> getRTCConfiguration() {
        return api.getTurnCredentials().stream().map(item ->
                PeerConnection.IceServer.builder(item.url)
                        .setUsername(item.username)
                        .setPassword(item.password)
                        .createIceServer()
        ).collect(Collectors.toList());
    }
}
