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

import androidx.arch.core.util.Function;

import com.simplito.java.privmx_endpoint.model.Event;
import com.simplito.java.privmx_endpoint.model.EventSelector;
import com.simplito.java.privmx_endpoint.model.PKIVerificationOptions;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CustomEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.crypto.CryptoApi;
import com.simplito.java.privmx_endpoint_extra.events.EventCallback;
import com.simplito.java.privmx_endpoint_extra.events.EventDispatcher;
import com.simplito.java.privmx_endpoint_extra.events.EventSelectorExtra;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extends {@link BasicPrivmxEndpoint} with event callbacks dispatcher.
 *
 * @category core
 */
public class PrivmxEndpoint extends BasicPrivmxEndpoint implements AutoCloseable {
    private final EventCallback<Map<String, List<String>>> onRemove = (map) -> {
        try {
            map.forEach(this::unsubscribeMany);
        } catch (Exception e) {
            System.out.println("todo");
        }
    };
    private final EventDispatcher eventDispatcher = new EventDispatcher(onRemove);

    /**
     * Calls {@link BasicPrivmxEndpoint#BasicPrivmxEndpoint(Set, String, String, String, PKIVerificationOptions)}.
     *
     * @param enableModule        set of modules to initialize; should contain {@link Modules#THREAD }
     *                            to enable Thread module or {@link Modules#STORE } to enable Store module
     * @param bridgeUrl           Bridge Server URL
     * @param solutionId          {@code SolutionId} of the current project
     * @param userPrivateKey      user private key used to authorize; generated from:
     *                            {@link CryptoApi#generatePrivateKey} or
     *                            {@link CryptoApi#derivePrivateKey}
     * @param verificationOptions PrivMX Bridge server instance verification options using a PKI server
     * @throws IllegalStateException thrown if there is an exception during init modules
     * @throws PrivmxException       thrown if there is a problem during login
     * @throws NativeException       thrown if there is an <strong>unknown</strong> problem during login
     */
    public PrivmxEndpoint(Set<Modules> enableModule, String userPrivateKey, String solutionId, String bridgeUrl, PKIVerificationOptions verificationOptions) throws IllegalStateException, PrivmxException, NativeException {
        super(enableModule, userPrivateKey, solutionId, bridgeUrl, verificationOptions);
    }

    /**
     * Calls {@link BasicPrivmxEndpoint#BasicPrivmxEndpoint(Set, String, String, String)}.
     *
     * @param enableModule   set of modules to initialize; should contain {@link Modules#THREAD }
     *                       to enable Thread module or {@link Modules#STORE } to enable Store module
     * @param bridgeUrl      Bridge Server URL
     * @param solutionId     {@code SolutionId} of the current project
     * @param userPrivateKey user private key used to authorize; generated from:
     *                       {@link CryptoApi#generatePrivateKey} or
     *                       {@link CryptoApi#derivePrivateKey}
     * @throws IllegalStateException thrown if there is an exception during init modules
     * @throws PrivmxException       thrown if there is a problem during login
     * @throws NativeException       thrown if there is an <strong>unknown</strong> problem during login
     */
    public PrivmxEndpoint(Set<Modules> enableModule, String userPrivateKey, String solutionId, String bridgeUrl) throws IllegalStateException, PrivmxException, NativeException {
        this(enableModule, userPrivateKey, solutionId, bridgeUrl, null);
    }

//    public void unregisterCallbacks(Object context) {
//        eventDispatcher.unbind(context);
//    }

    public void unregisterCallbacks(List<Object> callbacksId) {
        eventDispatcher.unbind(callbacksId);
    }

    public void unregisterAll() {
        eventDispatcher.unbindAll();
    }

    public void handleEvent(Event<?> event) {
        eventDispatcher.emit(event);
    }

    public void registerMany(ScopeCallback registerScope) throws InstantiationException, IllegalAccessException {
        ManyScope manyScope = new ManyScope();
        registerScope.execute(manyScope);
        manyScope.subscribeAll();
    }

    private void unsubscribeMany(String container, List<String> subscriptionIds) {
        switch (container) {
            case "custom":
                eventApi.unsubscribeFrom(subscriptionIds);
                break;
            case "thread":
                threadApi.unsubscribeFrom(subscriptionIds);
                break;
            case "store":
                storeApi.unsubscribeFrom(subscriptionIds);
                break;
            case "inbox":
                inboxApi.unsubscribeFrom(subscriptionIds);
                break;
            case "kvdb":
                kvdbApi.unsubscribeFrom(subscriptionIds);
                break;
        }
    }

    public interface ScopeCallback {
        void execute(Scope scope) throws InstantiationException, IllegalAccessException;
    }

    public interface Scope {
        <T> void registerCallback(Object callbackId, com.simplito.java.privmx_endpoint_extra.events.EventType<T> eventType, EventCallback<T> callback);
    }

    private class ManyScope implements Scope {
        private final Map<String/*containerName(thread)*/, Map<String/*query*/, List<EventSelectorExtra<?>>>> all = new HashMap<>();

        @Override
        public <T> void registerCallback(Object callbackId, com.simplito.java.privmx_endpoint_extra.events.EventType<T> eventType, EventCallback<T> callback) {
            EventSelector eventSelector = new EventSelector(eventType.eventType, eventType.eventSelectorType, eventType.eventSelectorId);
            EventSelectorExtra<?> eventSelectorExtra = new EventSelectorExtra<T>(callbackId, eventSelector, callback);
            String containerName = "";
            String query = "";

            String alreadyRegistered = eventDispatcher.registerCallback(eventSelector, callbackId, callback);
            if (alreadyRegistered == null) {
                if (eventSelector.eventSelectorType instanceof CustomEventSelectorType) {
                    eventSelectorExtra.channelName = eventType.channelName;
                    containerName = "custom";
                    query = eventApi.buildSubscriptionQuery(
                            eventType.channelName,
                            (CustomEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof ThreadEventType) {
                    containerName = "thread";
                    query = threadApi.buildSubscriptionQuery(
                            (ThreadEventType) eventType.eventType,
                            (ThreadEventSelectorType) eventType.eventSelectorType,
                            eventSelectorExtra.eventSelector.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof StoreEventType) {
                    containerName = "store";
                    query = storeApi.buildSubscriptionQuery(
                            (StoreEventType) eventSelectorExtra.eventSelector.eventType,
                            (StoreEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof InboxEventType) {
                    containerName = "inbox";
                    query = inboxApi.buildSubscriptionQuery(
                            (InboxEventType) eventType.eventType,
                            (InboxEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof KvdbEventType) {
                    containerName = "kvdb";
                    query = kvdbApi.buildSubscriptionQuery(
                            (KvdbEventType) eventType.eventType,
                            (KvdbEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                }

                all.putIfAbsent(containerName, new HashMap<>());
                all.get(containerName).putIfAbsent(query, new ArrayList<>());
                all.get(containerName).get(query).add(eventSelectorExtra);

            } else {
                // todo - is it necessary
                eventSelectorExtra.eventSelector.subscriptionId = alreadyRegistered;
                System.out.println("Already subscribed");
            }
        }

        private void subscribeFor(Map<String, List<EventSelectorExtra<?>>> queriesAndCallbacks, Function<List<String>, List<String>> subscribeMethod) {
            List<String> queries = queriesAndCallbacks.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
            List<String> ids = subscribeMethod.apply(queries);
            if (ids.size() == queries.size()) {
                for (int i = 0; i < ids.size(); i++) {
                    String query = queries.get(i);
                    final String id = ids.get(i);
                    queriesAndCallbacks.get(query).forEach(it -> {
                        it.eventSelector.subscriptionId = id;
                        eventDispatcher.assignSubscriptionId(it.eventSelector);
                    });
                }
            }
        }

        private void subscribeAll() {
            all.forEach((key, value) -> {
                switch (key) {
                    case "custom":
                        subscribeFor(value, eventApi::subscribeFor);
                        break;
                    case "thread":
                        subscribeFor(value, threadApi::subscribeFor);
                        break;
                    case "store":
                        subscribeFor(value, storeApi::subscribeFor);
                        break;
                    case "inbox":
                        subscribeFor(value, inboxApi::subscribeFor);
                        break;
                    case "kvdb":
                        subscribeFor(value, kvdbApi::subscribeFor);
                        break;
                }
            });
        }
    }
}