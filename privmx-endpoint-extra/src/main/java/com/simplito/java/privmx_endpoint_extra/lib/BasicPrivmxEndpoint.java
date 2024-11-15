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

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.crypto.CryptoApi;
import com.simplito.java.privmx_endpoint.modules.inbox.InboxApi;
import com.simplito.java.privmx_endpoint.modules.store.StoreApi;
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.Set;

/**
 * A collection of all PrivMX Endpoint modules. It represents a single connection to PrivMX Bridge.
 *
 * @category core
 */
public class BasicPrivmxEndpoint implements AutoCloseable {

    /**
     * Reference to Store module.
     */
    public final StoreApi storeApi;

    /**
     * Reference to Thread module.
     */
    public final ThreadApi threadApi;

    /**
     * Reference to Inbox module.
     */
    public final InboxApi inboxApi;

    /**
     * Reference to Connection module.
     */
    public final Connection connection;


    /**
     * Initializes modules and connects to PrivMX Bridge server using given parameters.
     *
     * @param enableModule   set of modules to initialize; should contain {@link Modules#THREAD }
     *                       to enable Thread module or {@link Modules#STORE } to enable Store module
     * @param bridgeUrl      Bridge's Endpoint URL
     * @param solutionId     {@code SolutionId} of the current project
     * @param userPrivateKey user private key used to authorize; generated from:
     *                       {@link CryptoApi#generatePrivateKey} or
     *                       {@link CryptoApi#derivePrivateKey}
     * @throws IllegalStateException thrown if there is an exception during init modules
     * @throws PrivmxException       thrown if there is a problem during login
     * @throws NativeException       thrown if there is an <strong>unknown</strong> problem during login
     */
    public BasicPrivmxEndpoint(
            Set<Modules> enableModule,
            String userPrivateKey,
            String solutionId,
            String bridgeUrl
    ) throws IllegalStateException, PrivmxException, NativeException {
        connection = Connection.connect(userPrivateKey, solutionId, bridgeUrl);
        storeApi = enableModule.contains(Modules.STORE) ? new StoreApi(connection) : null;
        threadApi = enableModule.contains(Modules.THREAD) ? new ThreadApi(connection) : null;
        inboxApi = enableModule.contains(Modules.INBOX) ? new InboxApi(
                connection,
                threadApi,
                storeApi
        ) : null;
    }

    /**
     * Disconnects from PrivMX Bridge and frees memory.
     *
     * @throws Exception when instance is currently closed
     */
    @Override
    public void close() throws Exception {
        if (threadApi != null) threadApi.close();
        if (storeApi != null) storeApi.close();
        if (inboxApi != null) inboxApi.close();
        if (connection != null) connection.close();
    }
}
