//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.streams;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.event.EventApi;
import com.simplito.java.privmx_endpoint.streams.model.Settings;
import com.simplito.java.privmx_endpoint.streams.model.StreamEncryptionMode;
import com.simplito.java.privmx_endpoint.streams.model.StreamHandle;
import com.simplito.java.privmx_endpoint.streams.model.StreamInfo;
import com.simplito.java.privmx_endpoint.streams.model.StreamPublishResult;
import com.simplito.java.privmx_endpoint.streams.model.StreamRoom;
import com.simplito.java.privmx_endpoint.streams.model.StreamSubscription;
import com.simplito.java.privmx_endpoint.streams.model.TurnCredentials;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class StreamApiLow implements AutoCloseable {
    static {
//        System.loadLibrary("crypto");
//        System.loadLibrary("ssl");
//        System.loadLibrary("privmx-endpoint-java");
//        System.loadLibrary("privmx-endpoint-streams-android");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    private StreamApiLow(Long api) {
        this.api = api;
    }

//    private native Long init(Connection connection, EventApi eventApi) throws IllegalStateException;

    private static native StreamApiLow create(
            Connection connection,
            EventApi eventApi,
            StreamEncryptionMode streamEncryptionMode
    );

    public StreamApiLow(
            Connection connection,
            EventApi eventApi,
            StreamEncryptionMode streamEncryptionMode
    ) throws IllegalStateException {
        Objects.requireNonNull(connection);
        EventApi tmpEventApi = eventApi == null ? new EventApi(connection) : null;
        create(
                connection,
                Optional.ofNullable(eventApi).orElse(tmpEventApi),
                streamEncryptionMode
        );
        try {
            if (eventApi != null) tmpEventApi.close();
        } catch (Exception ignore) {
        }
    }

    public StreamApiLow(
            Connection connection,
            EventApi eventApi
    ) throws IllegalStateException {
        this(connection, eventApi, StreamEncryptionMode.SINGLE_KEY);
    }

    public StreamApiLow(
            Connection connection
    ) throws IllegalStateException {
        this(connection, null, StreamEncryptionMode.SINGLE_KEY);
    }

    public native List<TurnCredentials> getTurnCredentials();

    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta
    ) {
        return this.createStreamRoom(contextId, users, managers, publicMeta, privateMeta, null);
    }

    public native String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    );

    public native String createStreamRoomEx(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            String type,
            ContainerPolicy policies
    );

    public String createStreamRoomEx(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            String type
    ) {
        return this.createStreamRoomEx(contextId, users, managers, publicMeta, privateMeta, type, null);
    }

    //TODO: write methods with default values for force and forceGenerateNewKey parameters
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
        this.updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
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
            ContainerPolicy policies
    );

    public native PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String sortBy,
            String queryAsJson
    );

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

    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) {
        return listStreamRooms(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder
    ) {
        return listStreamRooms(contextId, skip, limit, sortOrder, null, null, null);
    }

    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit
    ) {
        return listStreamRooms(contextId, skip, limit, "desc", null, null, null);
    }

    public native PagingList<StreamRoom> listStreamRoomsEx(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String type,
            String lastId,
            String sortBy,
            String queryAsJson
    );

    public PagingList<StreamRoom> listStreamRoomsEx(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String type,
            String lastId,
            String sortBy
    ) {
        return listStreamRoomsEx(contextId, skip, limit, sortOrder, type, lastId, sortBy, null);
    }

    public PagingList<StreamRoom> listStreamRoomsEx(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String type,
            String lastId
    ) {
        return listStreamRoomsEx(contextId, skip, limit, sortOrder, type, lastId, null, null);
    }

    public PagingList<StreamRoom> listStreamRoomsEx(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String type
    ) {
        return listStreamRoomsEx(contextId, skip, limit, sortOrder, type, null, null, null);
    }

    public native StreamRoom getStreamRoom(String streamRoomId);

    public native StreamRoom getStreamRoomEx(String streamRoomId, String type);

    public native void deleteStreamRoom(String streamRoomId);

    // Stream
    public native List<StreamInfo> listStreams(String streamRoomId);

    public native void joinStreamRoom(
            String streamRoomId,
            WebRTCInterface webRtc
    );

    public native void leaveStreamRoom(String streamRoomId);

    public native StreamHandle createStream(
            String streamRoomId
    );

    public native StreamPublishResult publishStream(StreamHandle streamHandle);

    // todo
//    public native StreamPublishResult updateStream(StreamHandle streamHandle);

    public native void unpublishStream(StreamHandle streamHandle);

    public native void subscribeToRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptions, Settings options);

    public native void modifyRemoteStreamsSubscriptions(String streamRoomId, List<StreamSubscription> subscriptionsToAdd, List<StreamSubscription> subscriptionsToRemove, Settings options);

    public native void unsubscribeFromRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptionsToRemove);

    public native void trickle(long sessionId, String candidateAsJson);


    // todo
//    public native void acceptOfferOnReconfigure(
//            long sessionId,
//            SdpWithTypeModel sdp
//    );

    public native List<String> subscribeFor(List<String> subscriptionQueries);

    public native void unsubscribeFrom(List<String> subscriptionIds);

//    private native String buildSubscriptionQuery(long eventType, long selectorType, String selectorId);
//
//    public String buildSubscriptionQuery(
//            StreamEventType eventType,
//            StreamEventSelectorType selectorType,
//            String selectorId
//    ) {
//        return buildSubscriptionQuery(
//                eventType.ordinal(),
//                selectorType.ordinal(),
//                selectorId
//        );
//    }


    public native void keyManagement(String streamRoomId, boolean disable);

    private native void deinit() throws IllegalStateException;

    @Override
    public void close() throws Exception {
        deinit();
    }
}
