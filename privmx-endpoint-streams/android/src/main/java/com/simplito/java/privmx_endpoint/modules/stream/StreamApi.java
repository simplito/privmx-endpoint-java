package com.simplito.java.privmx_endpoint.modules.stream;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.Settings;
import com.simplito.java.privmx_endpoint.model.StreamHandle;
import com.simplito.java.privmx_endpoint.model.StreamInfo;
import com.simplito.java.privmx_endpoint.model.StreamPublishResult;
import com.simplito.java.privmx_endpoint.model.StreamRoom;
import com.simplito.java.privmx_endpoint.model.StreamSubscription;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StreamEventType;

import org.webrtc.AudioTrack;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.List;

//TODO: Good to remove context from StreamApi
public class StreamApi {
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final String AUDIO_TRACK_ID = "ARDAMSa0";
    public static final String VIDEO_TRACK_TYPE = "video";
    private static final String TAG = "StreamApi";

    private final Context appContext;
    private final EglBase rootEglBase;
    private final StreamApiLow api;
    private final PeerConnectionManager pcManager;

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
            Context appContext,
            EglBase rootEglBase,
            StreamApiLow api
    ) {
        this(appContext, rootEglBase, api, null);
    }

    public StreamApi(
            @NonNull Context appContext,
            @NonNull EglBase rootEglBase,
            @NonNull StreamApiLow api,
            @Nullable PeerConnectionFactory pcFactory
    ) {
        this.appContext = appContext;
        this.rootEglBase = rootEglBase;
        this.api = api;
        PeerConnectionFactory factory = pcFactory;
        if (factory == null) {
            //TODO: What should be passed to the options parameter
            factory = DefaultPeerConnectionFactory(appContext, rootEglBase, new PeerConnectionFactory.Options());
        }
        pcManager = new PeerConnectionManager(
                factory,
                (sessionId, rtcConfiguration)->{
                    if(sessionId != null) {
                        this.api.trickle(sessionId, rtcConfiguration);
                    }
                });
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
            String streamRoomId,
            TrackObserver trackObserver
    ) {
        RoomJanusSession session = pcManager.createSession(streamRoomId,trackObserver);
        api.joinStreamRoom(streamRoomId, session.webrtc);
    }

    public void leaveStreamRoom(String streamRoomId) {
        pcManager.leaveStreamRoom(streamRoomId);
        api.leaveStreamRoom(streamRoomId);
    }

    public StreamHandle createStream(String streamRoomId) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if(session == null) throw new IllegalStateException("Session to this room is not exists. Call joinStreamRoom first");
        try {
            session.createPublisher();
        }catch (IllegalStateException e){
            throw new IllegalStateException("Publisher is now active, try use modifyRemoteStreamsSubscriptions");
        }

        StreamHandle handle = api.createStream(streamRoomId);
        pcManager.createHandleToRoom(handle, streamRoomId);
        return handle;
    }

    public TrackFactory getTrackFactory(StreamHandle streamHandle){
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream not exists. Create stream first.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        return new TrackFactory(publisher);
    }

    /**
     * @param streamHandle
     * @param track
     * @throws IllegalStateException if call addTrack before call createStream
     */
    public void addTrack(
            StreamHandle streamHandle,
            MediaStreamTrack track
    ) throws IllegalStateException {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream not exists. Create stream first.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        switch (track.kind()){
            case MediaStreamTrack.VIDEO_TRACK_KIND: {
                publisher.addVideoTrack((VideoTrack) track);
                break;
            }
            case MediaStreamTrack.AUDIO_TRACK_KIND:{
                publisher.addAudioTrack((AudioTrack) track);
                break;
            }
        }
    }

    /**
     * @param streamHandle
     * @param track
     * @throws IllegalStateException when Stream with this StreamHandle doesn't exist.
     */
    public void removeTrack(
            StreamHandle streamHandle,
            MediaStreamTrack track
    ) throws IllegalStateException {
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

    public StreamPublishResult publishStream(StreamHandle streamHandle) {
        return api.publishStream(streamHandle);
    }

    public StreamPublishResult updateStream(StreamHandle streamHandle) {
        return api.updateStream(streamHandle);
    }

    public void unpublishStream(StreamHandle streamHandle) {
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
        }catch (IllegalStateException e){
            throw new IllegalStateException("Subscriber is now active, try use modifyRemoteStreamsSubscriptions");
        }
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
        if(session != null){
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
}
