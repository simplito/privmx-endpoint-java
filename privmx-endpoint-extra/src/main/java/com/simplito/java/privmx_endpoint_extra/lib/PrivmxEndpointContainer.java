//
// PrivMX Endpoint Java Extra.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.lib;

import com.simplito.java.privmx_endpoint.model.Event;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.core.EventQueue;
import com.simplito.java.privmx_endpoint.modules.crypto.CryptoApi;
import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Manages certificates, Platform sessions, and active connections.
 * Implements event loop that can be started using {@link #startListening()}.
 * Contains instance of {@link CryptoApi}.
 *
 * @category core
 */
public class PrivmxEndpointContainer implements AutoCloseable {
    private static final String TAG = "[PrivmxEndpointContainer]";
    private final Map<Long, PrivmxEndpoint> privmxEndpoints = new HashMap<>();
    private boolean isInitialized = false;

    /**
     * Instance of {@link CryptoApi}.
     */
    public final CryptoApi cryptoApi = new CryptoApi();
    //Event loop
    private final ExecutorService eventExecutor = Executors.newSingleThreadExecutor();
    private Future<?> currentTask;

    /**
     * Creates instance of {@code PrivmxEndpointContainer}.
     */
    public PrivmxEndpointContainer(){}

    /**
     * Returns initialization state.
     * @return {@code true} if path to certificate is set successfully
     */
    public boolean initialized() {
        return isInitialized;
    }

    private boolean eventLoopStarted = false;

    /**
     * Stops event loop.
     */
    public void stopListening() {
        EventQueue.emitBreakEvent();
    }

    /**
     * Returns connection matching given {@code connectionId}.
     * @param connectionId Id of connection
     * @return Active connection
     * @throws IllegalStateException if certificate is not set successfully
     */
    public PrivmxEndpoint getEndpoint(Long connectionId) throws IllegalStateException {
        if (!isInitialized) {
            throw new IllegalStateException();
        }
        return privmxEndpoints.get(connectionId);
    }

    /**
     * Returns set of all active connection's IDs.
     * @return set of all active connection's IDs
     * @throws IllegalStateException if certificate is not set successfully
     */
    public Set<Long> getEndpointIDs() throws IllegalStateException {
        if (!isInitialized) {
            throw new IllegalStateException();
        }
        return privmxEndpoints.keySet();
    }

    /**
     * Sets path to the certificate used to create a secure connection to PrivMX Bridge.
     * It checks whether a .pem file with certificate exists in {@code certsPath} and uses it if it does.
     * If it does not, it installs the default PrivMX certificate.
     *
     * @param certsPath path to file with .pem certificate
     * @throws PrivmxException if there is an error while setting {@code certsPath}
     * @throws NativeException if there is an unknown error during set {@code certsPath}
     */
    public void setCertsPath(String certsPath) throws IllegalArgumentException, PrivmxException, NativeException {
        if(!new java.io.File(certsPath).exists()) throw new IllegalArgumentException("Certs file does not exists");
        Connection.setCertsPath(certsPath);
        isInitialized = true;
    }

    /**
     * Creates a new connection.
     *
     * @param enableModule   set of modules to initialize
     * @param bridgeUrl      Bridge's Endpoint URL
     * @param solutionId     {@code SolutionId} of the current project
     * @param userPrivateKey user private key used to authorize; generated from:
     *                       {@link CryptoApi#generatePrivateKey} or
     *                       {@link CryptoApi#derivePrivateKey}
     * @return Created connection
     * @throws IllegalStateException when certPath is not set up
     * @throws PrivmxException       if there is a problem during login
     * @throws NativeException       if there is an unknown problem during login
     */
    public PrivmxEndpoint connect(
            Set<Modules> enableModule,
            String userPrivateKey,
            String solutionId,
            String bridgeUrl
    ) throws IllegalStateException, PrivmxException, NativeException {
        if (!isInitialized) throw new IllegalStateException("Certs path is not set");
        PrivmxEndpoint privmxEndpoint = new PrivmxEndpoint(
                enableModule,
                userPrivateKey,
                solutionId,
                bridgeUrl
        );
        synchronized (privmxEndpoints) {
            privmxEndpoints.put(privmxEndpoint.connection.getConnectionId(), privmxEndpoint);
        }
        return privmxEndpoint;
    }

    /**
     * Disconnects connection matching given {@code connectionId} and removes it from the container.
     * This method is recommended for disconnecting connections by their ID from the container.
     * @param connectionId ID of the connection
     */
    public void disconnect(Long connectionId){
        if (!isInitialized) throw new IllegalStateException("Certs path is not set");
        PrivmxEndpoint endpoint = privmxEndpoints.get(connectionId);
        if ( endpoint == null) throw new IllegalStateException("No connection with specified id");
        try{
            endpoint.close();
        }catch (Exception ignored){}
    }

    /**
     * Disconnects all connections and removes them from the container.
     */
    public void disconnectAll(){
        if (!isInitialized) throw new IllegalStateException("Certs path is not set");
        synchronized (privmxEndpoints){
            privmxEndpoints.values().forEach(endpoint -> {
                try{
                    endpoint.close();
                }catch (Exception ignored){}
            });
            privmxEndpoints.clear();
        }
    }

    /**
     * Starts event handling Thread.
     */
    public void startListening() {
        eventLoopStarted = true;
        if (currentTask == null) waitForNextEvent();
    }

    private void waitForNextEvent() {
        synchronized (this) {
            if (currentTask == null && eventLoopStarted && !eventExecutor.isShutdown()) {
                currentTask = eventExecutor.submit(() -> {
                    try {
                        Event<?> result = EventQueue.waitEvent();
                        currentTask = null;
                        onNewEvent(result);
                    } catch (IllegalStateException e) {
                        currentTask = null;
                    } catch (Exception e) {
                        System.out.println("Catch event exception: " + e.getMessage());
                        currentTask = null;
                        waitForNextEvent();
                    }
                });
            }
        }
    }

    private void onNewEvent(Event<?> event) {
        if(event.type.equals("libPlatformDisconnected")){
            waitForNextEvent();
            return;
        }
        if(event.connectionId != null && event.connectionId != -1) {
            synchronized (privmxEndpoints) {
                PrivmxEndpoint endpoint = privmxEndpoints.get(event.connectionId);
                if (endpoint != null) {
                    endpoint.handleEvent(event);
                    if (event.type.equals(EventType.DisconnectedEvent.eventType)) {
                        try {
                            PrivmxEndpoint closedConnection = privmxEndpoints.remove(event.connectionId);
                            closedConnection.close();
                        } catch (Exception ignore) {
                        }
                    }
                }
            }
        }

        if (event.type.equals(EventType.LibBreakEvent.eventType)) {
            eventLoopStarted = false;
            return;
        }
        waitForNextEvent();
    }

    /**
     * Closes event loop.
     */
    @Override
    public void close() throws Exception {
        stopListening();
        synchronized (privmxEndpoints) {
            privmxEndpoints.forEach((key, value) -> {
                try {
                    value.close();
                } catch (Exception ignore) {
                }
            });
            privmxEndpoints.clear();
        }
        eventExecutor.shutdown();
    }

}
