//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.Stream;
import com.simplito.java.privmx_endpoint.model.StreamRoom;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

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

    public static native StreamApiLow create(Connection connection);

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
            ContainerPolicy policies);

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
            ContainerPolicy policies);

    public native PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId);

    public native StreamRoom getStreamRoom(String streamRoomId);

    public native void deleteStreamRoom(String streamRoomId);

    // Stream
    public native Long createStream(
            String streamRoomId,
            long localStreamId,
            WebRTCInterface webRtc);

    public native void publishStream(long localStreamId);

    public native Long joinStream(
            String streamRoomId,
            List<Long> streamsId,
            long localStreamId,
            WebRTCInterface webRtc);

    public native List<Stream> listStreams(String streamRoomId);

    public native void unpublishStream(long localStreamId);

    public native void leaveStream(long localStreamId);

    private native void deinit() throws IllegalStateException;

    @Override
    public void close() throws Exception {
        deinit();
    }
}
