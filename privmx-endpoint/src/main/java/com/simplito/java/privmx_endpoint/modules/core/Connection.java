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

package com.simplito.java.privmx_endpoint.modules.core;

import com.simplito.java.privmx_endpoint.model.Context;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;

/**
 * Manages a connection between the Endpoint and the Bridge server.
 *
 * @category core
 */
public class Connection implements AutoCloseable {
    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("privmx-endpoint-java");
    }

    private final Long api;
    private final Long connectionId;

    private Connection(Long api, Long connectionId) {
        this.api = api;
        this.connectionId = connectionId;
    }

    private native void deinit() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Allows to set path to the SSL certificate file.
     *
     * @param certsPath path to file
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     */
    public static native void setCertsPath(String certsPath) throws PrivmxException, NativeException;

    /**
     * Connects to the PrivMX Bridge server.
     *
     * @param userPrivKey user's private key
     * @param solutionId  ID of the Solution
     * @param platformUrl Platform's Endpoint URL
     * @return Connection object
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     * @event type: libConnected
     * channel: -
     * payload: {@link Void}
     */
    @Deprecated
    public static Connection platformConnect(String userPrivKey, String solutionId, String platformUrl) throws PrivmxException, NativeException{
        return connect(userPrivKey,solutionId,platformUrl);
    }

    /**
     * Connects to the PrivMX Bridge server.
     *
     * @param userPrivKey user's private key
     * @param solutionId  ID of the Solution
     * @param bridgeUrl Bridge's Endpoint URL
     * @return Connection object
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     * @event type: libConnected
     * channel: -
     * payload: {@link Void}
     */
    public static native Connection connect(String userPrivKey, String solutionId, String bridgeUrl) throws PrivmxException, NativeException;

    /**
     * Connects to the PrivMX Bridge server as a guest user.
     *
     * @param solutionId  ID of the Solution
     * @param platformUrl Platform's Endpoint URL
     * @return Connection object
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     * @event type: libConnected
     * channel: -
     * payload: {@link Void}
     */
    @Deprecated
    public static Connection platformConnectPublic(String solutionId, String platformUrl) throws PrivmxException, NativeException{
        return connectPublic(solutionId,platformUrl);
    }

    /**
     * Connects to the PrivMX Bridge server as a guest user.
     *
     * @param solutionId  ID of the Solution
     * @param bridgeUrl Bridge's Endpoint URL
     * @return Connection object
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     * @event type: libConnected
     * channel: -
     * payload: {@link Void}
     */
    public static native Connection connectPublic(String solutionId, String bridgeUrl) throws PrivmxException, NativeException;

    /**
     * Disconnects from the PrivMX Bridge server.
     *
     * @throws IllegalStateException thrown when instance is not connected or closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: libDisconnected
     * channel: -
     * payload: {@link Void}
     * @event type: libPlatformDisconnected
     * channel: -
     * payload: {@link Void}
     */
    public native void disconnect() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Gets a list of Contexts available for the user.
     *
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of Contexts
     * @throws IllegalStateException thrown when instance is not connected.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Context> listContexts(long skip, long limit, String sortOrder) throws IllegalStateException, PrivmxException, NativeException {
        return listContexts(skip, limit, sortOrder, null);
    }

    /**
     * Gets a list of Contexts available for the user.
     *
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @return list of Contexts
     * @throws IllegalStateException thrown when instance is not connected.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native PagingList<Context> listContexts(long skip, long limit, String sortOrder, String lastId) throws IllegalStateException, PrivmxException, NativeException;


    /**
     * Gets the ID of the current connection.
     *
     * @return ID of the connection
     */
    public Long getConnectionId() {
        return this.connectionId;
    }


    /**
     * If there is an active connnection then it
     * disconnects from PrivMX Bridge and frees memory making this instance not reusable.
     */
    @Override
    public void close() {
        if (api != null) {
            disconnect();
        }
    }
}
