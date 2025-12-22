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

package com.simplito.java.privmx_endpoint_streams.modules;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StreamEventType;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.event.EventApi;
import com.simplito.java.privmx_endpoint_streams.model.SdpWithTypeModel;
import com.simplito.java.privmx_endpoint_streams.model.StreamHandle;
import com.simplito.java.privmx_endpoint_streams.model.StreamInfo;
import com.simplito.java.privmx_endpoint_streams.model.StreamPublishResult;
import com.simplito.java.privmx_endpoint_streams.model.StreamRoom;
import com.simplito.java.privmx_endpoint_streams.model.StreamSettings;
import com.simplito.java.privmx_endpoint_streams.model.StreamSubscription;
import com.simplito.java.privmx_endpoint_streams.model.TurnCredentials;


import java.util.List;

public class StreamApiLow implements AutoCloseable {
    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("privmx-endpoint-java");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    private StreamApiLow(Long api) {
        this.api = api;
    }

    public static native StreamApiLow create(
            Connection connection,
            EventApi eventApi
    );

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
            String sortBy
    );

    public native StreamRoom getStreamRoom(String streamRoomId);

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

    public native StreamPublishResult updateStream(StreamHandle streamHandle);

    public native void unpublishStream(StreamHandle streamHandle);

    public native void subscribeToRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptions, StreamSettings options);

    public native void modifyRemoteStreamsSubscriptions(String streamRoomId, List<StreamSubscription> subscriptionsToAdd, List<StreamSubscription> subscriptionsToRemove, StreamSettings options);

    public native void unsubscribeFromRemoteStreams(String streamRoomId, List<StreamSubscription> subscriptionsToRemove);

    public native void trickle(long sessionId, String candidateAsJson);


    public native void acceptOfferOnReconfigure(
            long sessionId,
            SdpWithTypeModel sdp
    );

    public native List<String> subscribeFor(List<String> subscriptionQueries);

    public native void unsubscribeFrom(List<String> subscriptionIds);

    public native String buildSubscriptionQuery(StreamEventType eventType, StreamEventSelectorType selectorType, String selectorId);


    public native void keyManagement(String streamRoomId, boolean disable);

//    public native void reconfigureStream(long localStreamId, String optionsJSON);

    private native void deinit() throws IllegalStateException;

    @Override
    public void close() throws Exception {
        deinit();
    }
}
