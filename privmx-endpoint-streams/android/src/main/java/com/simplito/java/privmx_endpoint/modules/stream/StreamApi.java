package com.simplito.java.privmx_endpoint.modules.stream;


import android.content.Context;

import androidx.annotation.NonNull;

import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
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

/**
 * High-level API for managing PrivMX Stream Rooms and WebRTC media sessions on Android.
 * {@code StreamApi} is a high-level wrapper over {@link StreamApiLow} and WebRTC, providing
 * a simplified interface for working with Stream Rooms.
 */
public class StreamApi implements AutoCloseable {
    private final StreamApiLow api;
    private final PeerConnectionManager pcManager;

    /**
     * Factory which provides helpers for creating WebRTC media sources and tracks.
     */
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

    /**
     * Creates a {@code StreamApi} instance.
     *
     * @param appContext  Android application context
     * @param rootEglBase {@link EglBase} context used for hardware-accelerated video encoding and decoding
     * @param api         initialised {@link StreamApiLow} instance
     */
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

    /**
     * Creates a new Stream Room within the specified Context.
     *
     * @param contextId   ID of the Context to create the Stream Room in
     * @param users       list of {@link UserWithPubKey} indicating which users will have
     *                    access to the created Stream Room
     * @param managers    list of {@link UserWithPubKey} indicating which users will have
     *                    access and management rights to the created Stream Room
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param policies    additional container access policies, or {@code null} to use default settings
     * @return ID of the created Stream Room
     */
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

    /**
     * Updates an existing Stream Room.
     *
     * @param streamRoomId        ID of the Stream Room to update
     * @param users               list of {@link UserWithPubKey} indicating which users will have
     *                            access to the created Stream Room
     * @param managers            list of {@link UserWithPubKey} indicating which users will have
     *                            access and management rights to the created Stream Room
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated Stream Room
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate the encryption key for the StreamRoom
     * @param policies            additional container access policies, or {@code null} to keep
     *                            current/defaults
     */
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

    /**
     * Gets a list of Stream Rooms in given Context.
     *
     * @param contextId   ID of the Context to get Stream Rooms from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ({@code "asc"} for ascending,
     *                    {@code "desc"} for descending)
     * @param lastId      ID of the element from which query results should start
     * @param sortBy      field name to sort elements by
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of Stream Rooms
     */
    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String sortBy,
            String queryAsJson
    ) {
        return api.listStreamRooms(contextId, skip, limit, sortOrder, lastId, sortBy, queryAsJson);
    }

    /**
     * Gets a list of Stream Rooms in given Context.
     *
     * @param contextId   ID of the Context to get Stream Rooms from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ({@code "asc"} for ascending,
     *                    {@code "desc"} for descending)
     * @param lastId      ID of the element from which query results should start
     * @param sortBy      field name to sort elements by
     * @return list of Stream Rooms
     */
    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String sortBy
    ) {
        return listStreamRooms(contextId, skip, limit, sortOrder, lastId, sortBy, null);
    }

    /**
     * Gets a list of Stream Rooms in given Context.
     *
     * @param contextId   ID of the Context to get Stream Rooms from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ({@code "asc"} for ascending,
     *                    {@code "desc"} for descending)
     * @param lastId      ID of the element from which query results should start
     * @return list of Stream Rooms
     */
    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) {
        return listStreamRooms(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of Stream Rooms in given Context.
     *
     * @param contextId   ID of the Context to get Stream Rooms from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ({@code "asc"} for ascending,
     *                    {@code "desc"} for descending)
     * @return list of Stream Rooms
     */
    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder
    ) {
        return listStreamRooms(contextId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a single StreamRoom identified by given StreamRoom ID.
     *
     * @param streamRoomId ID of the Stream Room to get
     * @return {@link StreamRoom} containing information about the room
     */
    public StreamRoom getStreamRoom(String streamRoomId) {
        return api.getStreamRoom(streamRoomId);
    }

    /**
     * Deletes a StreamRoom identified by given StreamRoom ID.
     *
     * @param streamRoomId ID of the Stream Room to delete
     */
    public void deleteStreamRoom(String streamRoomId) {
        api.deleteStreamRoom(streamRoomId);
    }

    /**
     * Gets a list of currently published streams in given Stream Room.
     *
     * @param streamRoomId ID of the Stream Room to list streams from
     * @return list of {@link StreamInfo} describing currently published streams
     */
    public List<StreamInfo> listStreams(String streamRoomId) {
        return api.listStreams(streamRoomId);
    }

    /**
     * Joins a StreamRoom and prepares the session for WebRTC communication.
     * Must be called before {@link #createStream(String)},
     * {@link #publishStream(StreamHandle)}, and any remote stream subscription calls
     * for the given room.
     *
     * @param streamRoomId ID of the Stream Room to join
     */
    public void joinStreamRoom(
            String streamRoomId
    ) {
        RoomJanusSession session = pcManager.createSession(streamRoomId);
        api.joinStreamRoom(streamRoomId, session.webrtc);
    }

    /**
     * Leaves a Stream Room and releases the associated WebRTC session.
     *
     * @param streamRoomId ID of the Stream Room to leave
     */
    public void leaveStreamRoom(String streamRoomId) {
        pcManager.leaveStreamRoom(streamRoomId);
        api.leaveStreamRoom(streamRoomId);
    }

    /**
     * Creates a local stream handle for publishing media in given StreamRoom.
     * {@link #joinStreamRoom(String)} must be called before this method.
     *
     * @param streamRoomId ID of the StreamRoom to create the stream in
     * @return handle to the local stream instance
     */
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
     * Adds a local media track to a Stream handle.
     * The track is staged locally and becomes visible to others after publishStream/updateStream.
     *
     * @param streamHandle handle returned by {@link #createStream(String)}
     * @param track        {@link VideoTrack} or {@link AudioTrack} to add
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

    /**
     * Registers a {@link TrackObserver} to receive callbacks when a remote media track
     * become available for a specified stream in the given StreamRoom.
     * Use this method to observe tracks only from a selected remote stream.
     *
     * @param roomId   ID of the StreamRoom
     * @param observer observer implementation receiving track callbacks
     * @param streamId ID of a specific remote stream to observe, or {@code null} for all streams in the given StreamRoom.
     * @throws IllegalStateException thrown when no active session exists for the given room.
     */
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

    /**
     * Registers a {@link TrackObserver} to receive callbacks when a remote media track
     * become available for all streams in the given StreamRoom.
     *
     * @param roomId   ID of the StreamRoom
     * @param observer observer implementation receiving track callbacks
     * @throws IllegalStateException thrown when no active session exists for the given room.
     */
    public void setTrackObserver(
            @NonNull String roomId,
            TrackObserver observer
    ) {
        setTrackObserver(roomId, observer, null);
    }

    /**
     * Registers an observer to receive ICE connection state changes for the given Stream Room.
     *
     * @param roomId   ID of the Stream Room
     * @param observer callback receiving {@link PeerConnection.IceConnectionState} values
     * @throws IllegalStateException thrown when no active session exists for the given room.
     */
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


    /**
     * Removes a media track from a stream.
     * After removing tracks, call {@link #updateStream(StreamHandle)} to propagate
     * the change to other participants.
     *
     * @param streamHandle handle returned by {@link #createStream(String)}
     * @param track        {@link VideoTrack} or {@link AudioTrack} to remove
     * @throws IllegalStateException thrown when Stream with this StreamHandle doesn't exist.
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

    /**
     * Publishes the stream (with currently added tracks) to the server,
     * making it visible to other participants in the room.
     *
     * @param streamHandle handle returned by {@link #createStream(String)}
     * @return result of the publish operation containing stream information
     * @throws IllegalStateException thrown when no stream exists for the given handle.
     */
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

    /**
     * Updates a published stream after track changes.
     * Call this after {@link #addTrack(StreamHandle, MediaStreamTrack)} or
     * {@link #removeTrack(StreamHandle, MediaStreamTrack)} on an already-published stream
     * to propagate the changes to other participants.
     *
     * @param streamHandle handle returned by {@link #createStream(String)}
     * @return result of the update operation containing updated stream information
     * @throws IllegalStateException thrown when no stream exists for the given handle.
     */
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

    /**
     * Stops publishing the stream.
     *
     * @param streamHandle handle returned by {@link #createStream(String)}
     * @throws IllegalStateException thrown when instance is closed.
     */
    public void unpublishStream(@NonNull StreamHandle streamHandle) {
        Objects.requireNonNull(streamHandle);
        api.unpublishStream(streamHandle);
    }

    /**
     * Subscribes to selected remote streams in a StreamRoom.
     * Individual tracks within a stream can also be selected for subscription.
     * {@link #joinStreamRoom(String)} must be called before this method.
     *
     * @param streamRoomId  ID of the StreamRoom
     * @param subscriptions list of {@link StreamSubscription} describing the remote
     *                      streams to subscribe to
     * @throws IllegalStateException thrown when no active session exists for the given room.
     */
    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions
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
        api.subscribeToRemoteStreams(streamRoomId, subscriptions);
    }


    /**
     * Modifies the current list of remote stream subscriptions in a Stream Room.
     * Allows atomically adding and removing remote stream subscriptions in a single call,
     * avoiding the need to fully unsubscribe and resubscribe.
     *
     * @param streamRoomId          ID of the Stream Room
     * @param subscriptionsToAdd    list of {@link StreamSubscription} to add
     * @param subscriptionsToRemove list of {@link StreamSubscription} to remove
     * @throws IllegalStateException thrown when no active session or subscriber exists
     *                               for the given room.
     */
    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove
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
                subscriptionsToRemove
        );
    }

    /**
     * Unsubscribes from selected remote streams in a Stream Room.
     *
     * @param streamRoomId          ID of the Stream Room
     * @param subscriptionsToRemove list of {@link StreamSubscription} to remove
     * @throws IllegalStateException thrown when no active session or subscriber exists
     *                               for the given room.
     */
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

    /**
     * Controls whether encrypted media frames that cannot be decrypted should be dropped.
     *
     * @param streamRoomId ID of the StreamRoom
     * @param enable       if {@code true}, broken frames will be dropped
     */
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

    /**
     * Subscribes to events for the Stream Room as well as its individual streams,
     * based on the provided subscription queries.
     *
     * @param subscriptionQueries list of queries built with
     *                            {@link #buildSubscriptionQuery(StreamEventType, StreamEventSelectorType, String)}
     * @return list of subscription IDs in matching order to {@code subscriptionQueries}
     * @throws IllegalStateException thrown when instance is closed.
     */
    public List<String> subscribeFor(List<String> subscriptionQueries) {
        return api.subscribeFor(subscriptionQueries);
    }

    /**
     * Unsubscribes from events with the given subscription IDs.
     *
     * @param subscriptionIds list of subscription IDs returned by {@link #subscribeFor(List)}
     * @throws IllegalStateException thrown when instance is closed.
     */
    public void unsubscribeFrom(List<String> subscriptionIds) {
        api.unsubscribeFrom(subscriptionIds);
    }

    /**
     * Generates a subscription query string for events for the Stream Room
     * as well as its individual streams.
     * The returned query should be passed to {@link #subscribeFor(List)} to start
     * receiving the requested events.
     *
     * @param eventType    type of event to listen for
     * @param selectorType scope at which events are observed
     * @param selectorId   ID of the selected entity
     * @return query string used for event subscription
     * @throws IllegalStateException thrown when instance is closed
     */
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

    /**
     * Releases all resources associated with this instance.
     * Leaves all active StreamRooms and releases allocated resources.
     *
     * @throws Exception thrown if an error occurs during cleanup
     */
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
