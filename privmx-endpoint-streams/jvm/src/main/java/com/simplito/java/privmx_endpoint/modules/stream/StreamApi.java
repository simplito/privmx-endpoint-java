package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.LibLoader;
import com.simplito.java.privmx_endpoint.model.MediaDevice;
import com.simplito.java.privmx_endpoint.model.RemoteStreamId;
import com.simplito.java.privmx_endpoint.model.Stream;
import com.simplito.java.privmx_endpoint.model.StreamSettings;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StreamEventType;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.stream.StreamHandle;
import com.simplito.java.privmx_endpoint.model.stream.StreamRoom;
import com.simplito.java.privmx_endpoint.model.stream.StreamSubscription;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.event.EventApi;

import java.util.List;
import java.util.Objects;

public class StreamApi implements AutoCloseable {
    static {
        LibLoader.loadPrivmxLibraries();
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    private native Long init(Connection connection, EventApi eventApi) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    public StreamApi(Connection connection, EventApi eventApi) throws IllegalStateException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(eventApi);
        this.api = init(connection, eventApi);
    }

    public native String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    );

    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta
    ) {
        return createStreamRoom(contextId, users, managers, publicMeta, privateMeta, null);
    }

    public native void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey,
            ContainerPolicy policy
    );

    public void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey
    ) {
        updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
    }

    public void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force
    ) {
        updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, force, false, null);
    }

    public void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version
    ) {
        updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, false, false, null);
    }

    public native PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    );

    public native StreamRoom getStreamRoom(String streamRoomId);

    public native void deleteStreamRoom(String streamRoomId);

    public native void joinRoom(String streamRoomId); // required before createStream and openStream

    public native void leaveRoom(String streamRoomId);

    public native List<String> subscribeFor(List<String> subscriptionQueries);

    public String buildSubscriptionQuery(StreamEventType eventType, StreamEventSelectorType selectorType, String selectorId) {
        return buildSubscriptionQuery(
                eventType.ordinal(),
                selectorType.ordinal(),
                selectorId
        );
    }

    private native String buildSubscriptionQuery(long eventType, long selectorType, String selectorId);

    public native void unsubscribeFrom(List<String> subscriptionIds);

    public native List<Stream> listStreams(String streamRoomId);

    public native StreamHandle createStream(String streamRoomId);

    public native List<MediaDevice> getMediaDevices();

    public native void addTrack(StreamHandle streamHandle, MediaDevice track);

    public native void removeTrack(StreamHandle streamHandle, MediaDevice track);

    public native RemoteStreamId publishStream(StreamHandle streamHandle);

    public native void unpublishStream(StreamHandle streamHandle);

    public native void subscribeToRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptions, StreamSettings options);

    public native void modifyRemoteStreamsSubscriptions(String streamRoomId, List<StreamSubscription> subscriptionsToAdd, List<StreamSubscription> subscriptionsToRemove, StreamSettings options);

    public native void unsubscribeFromRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptionsToRemove);

    public native void dropBrokenFrames(String streamRoomId, boolean enable);

    @Override
    public void close() throws Exception {
        deinit();
    }
}