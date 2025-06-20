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

package com.simplito.java.privmx_endpoint.modules.event;

import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Objects;

/**
 * Manages PrivMX Bridge custom events.
 *
 * @category event
 */
public class EventApi implements AutoCloseable {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    private final Long api;

    /**
     * Initialize Event module on passed active connection.
     *
     * @param connection active connection to PrivMX Bridge
     * @throws IllegalStateException when passed {@link Connection} is not connected.
     */
    public EventApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    /**
     * Emits the custom event on the given Context and channel.
     *
     * @param contextId   ID of the Context
     * @param users       list of {@link UserWithPubKey} objects which defines the recipients of the event
     * @param channelName name of the Channel
     * @param eventData   event's data
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void emitEvent(String contextId, List<UserWithPubKey> users, String channelName, byte[] eventData) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribe for the custom events on the given channel.
     *
     * @param contextId   ID of the Context
     * @param channelName name of the Channel
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void subscribeForCustomEvents(String contextId, String channelName) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribe from the custom events on the given channel.
     *
     * @param contextId   ID of the Context
     * @param channelName name of the Channel
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFromCustomEvents(String contextId, String channelName) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Frees memory.
     *
     * @throws Exception when instance is currently closed.
     */
    @Override
    public void close() throws Exception {
        deinit();
    }
}