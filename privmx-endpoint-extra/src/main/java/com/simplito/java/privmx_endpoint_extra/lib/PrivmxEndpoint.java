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
import com.simplito.java.privmx_endpoint.model.EventSelector;
import com.simplito.java.privmx_endpoint.model.PKIVerificationOptions;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.EventEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.EventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Extends {@link BasicPrivmxEndpoint} with event callbacks dispatcher.
 *
 * @category core
 */
public class PrivmxEndpoint extends BasicPrivmxEndpoint implements AutoCloseable {
    private final EventCallback<List<EventSelector>> onRemove = (selectors) -> {
        try {
            unsubscribeMany(selectors);
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

    public void unregisterCallbacks(Object context) {
        eventDispatcher.unbind(context);
    }

    public void unregisterCallbacks(List<String> subscriptionIds) {
        eventDispatcher.unbind(subscriptionIds);
    }

    public void unregisterAll() {
        eventDispatcher.unbindAll();
    }

    public void handleEvent(Event<?> event) {
        eventDispatcher.emit(event);
    }

    public List<String> registerMany(ScopeCallback registerScope) throws InstantiationException, IllegalAccessException {
        ManyScope manyScope = new ManyScope();
        registerScope.execute(manyScope);
        return manyScope.subscribeAll();
    }

    public interface ScopeCallback {
        void execute(Scope scope) throws InstantiationException, IllegalAccessException;
    }

    private void unsubscribeMany(List<EventSelector> selectors) {
        // EventEventSelectorType
        eventApi.unsubscribeFrom(selectors.stream().filter(it -> it.eventSelectorType instanceof EventEventSelectorType).map(it -> it.subscriptionId).collect(Collectors.toList()));
        // ThreadEventType
        threadApi.unsubscribeFrom(selectors.stream().filter(it -> it.eventType instanceof ThreadEventType).map(it -> it.subscriptionId).collect(Collectors.toList()));
        // StoreEventType
        storeApi.unsubscribeFrom(selectors.stream().filter(it -> it.eventType instanceof StoreEventType).map(it -> it.subscriptionId).collect(Collectors.toList()));
        // InboxEventType
        inboxApi.unsubscribeFrom(selectors.stream().filter(it -> it.eventType instanceof InboxEventType).map(it -> it.subscriptionId).collect(Collectors.toList()));
        // KvdbEventType
        kvdbApi.unsubscribeFrom(selectors.stream().filter(it -> it.eventType instanceof KvdbEventType).map(it -> it.subscriptionId).collect(Collectors.toList()));
    }
    public interface Scope {
        <T> void registerCallback(Object callbackId, EventSelector eventSelector, EventCallback<T> callback);

        <T> void registerCallback(Object callbackId, EventType eventType, EventSelectorType eventSelectorType, String selectorId, EventCallback<T> callback);

        <T> void registerCustomCallback(Object callbackId, EventEventSelectorType eventSelectorType, String selectorId, String channelName, EventCallback<T> callback);
    }

    private class ManyScope implements Scope {
        private final ArrayList<EventSelectorExtra<?>> AllSelectors = new ArrayList<>();

        @Override
        public <T> void registerCallback(Object callbackId, EventSelector eventSelector, EventCallback<T> callback) {
            EventSelectorExtra<?> eventSelectorExtra = new EventSelectorExtra<T>(callbackId, eventSelector, callback);

            String alreadyRegistered = eventDispatcher.registerCallback(eventSelector, callbackId, callback);
            if (alreadyRegistered == null) {
                if (eventSelector.eventType instanceof ThreadEventType) {
                    eventSelectorExtra.query = threadApi.buildSubscriptionQuery(
                            (ThreadEventType) eventSelectorExtra.eventSelector.eventType,
                            (ThreadEventSelectorType) eventSelectorExtra.eventSelector.eventSelectorType,
                            eventSelectorExtra.eventSelector.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof StoreEventType) {
                    eventSelectorExtra.query = storeApi.buildSubscriptionQuery(
                            (StoreEventType) eventSelectorExtra.eventSelector.eventType,
                            (StoreEventSelectorType) eventSelectorExtra.eventSelector.eventSelectorType,
                            eventSelectorExtra.eventSelector.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof InboxEventType) {
                    eventSelectorExtra.query = inboxApi.buildSubscriptionQuery(
                            (InboxEventType) eventSelectorExtra.eventSelector.eventType,
                            (InboxEventSelectorType) eventSelectorExtra.eventSelector.eventSelectorType,
                            eventSelectorExtra.eventSelector.eventSelectorId
                    );
                } else if (eventSelector.eventType instanceof KvdbEventType) {
                    eventSelectorExtra.query = kvdbApi.buildSubscriptionQuery(
                            (KvdbEventType) eventSelectorExtra.eventSelector.eventType,
                            (KvdbEventSelectorType) eventSelectorExtra.eventSelector.eventSelectorType,
                            eventSelectorExtra.eventSelector.eventSelectorId
                    );
                } else {
                    // todo
                }
            } else {
                eventSelectorExtra.query = alreadyRegistered;
                System.out.println("Already subscribed");
            }

            AllSelectors.add(eventSelectorExtra);
        }

        @Override
        public <T> void registerCallback(Object callbackId, EventType eventType, EventSelectorType eventSelectorType, String selectorId, EventCallback<T> callback) {
            registerCallback(callbackId, new EventSelector(eventType, eventSelectorType, selectorId), callback);
        }

        @Override
        public <T> void registerCustomCallback(Object callbackId, EventEventSelectorType eventSelectorType, String selectorId, String channelName, EventCallback<T> callback) {
            EventSelectorExtra<?> eventSelectorExtra = new EventSelectorExtra<T>(callbackId, new EventSelector(null, eventSelectorType, selectorId), channelName, callback);
            AllSelectors.add(eventSelectorExtra);
        }

        private void subscribeFor(List<EventSelectorExtra<?>> list) {
            EventType eventType = list.get(0).eventSelector.eventType;
            EventSelectorType eventSelectorType = list.get(0).eventSelector.eventSelectorType;
            List<String> queries = list.stream().map((eventSelectorExtra) -> eventSelectorExtra.query).collect(Collectors.toList());
            List<String> ids = new ArrayList<>();

            if (eventSelectorType instanceof EventEventSelectorType) {
                ids = eventApi.subscribeFor(queries);
            } else if (eventType instanceof ThreadEventType) {
                ids = threadApi.subscribeFor(queries);
            } else if (eventType instanceof StoreEventType) {
                ids = threadApi.subscribeFor(queries);
            } else if (eventType instanceof InboxEventType) {
                ids = inboxApi.subscribeFor(queries);
            } else if (eventType instanceof KvdbEventType) {
                ids = kvdbApi.subscribeFor(queries);
            }

            if (ids.size() == list.size()) {
                for (int i = 0; i < ids.size(); i++) {
                    list.get(i).eventSelector.subscriptionId = ids.get(i);
                    eventDispatcher.assignSelector(list.get(i).eventSelector);
                }
            }

            int index = 0;
            for (EventSelectorExtra<?> eventSelectorExtra : AllSelectors) {
                if (eventType.getClass().isInstance(eventSelectorExtra.eventSelector.eventType)) {
                    eventSelectorExtra.eventSelector.subscriptionId = ids.get(index++);
                }
            }
        }

        private List<String> subscribeAll() {
            subscribeFor(AllSelectors.stream().filter(it -> it.eventSelector.eventType instanceof ThreadEventType).collect(Collectors.toList()));
            subscribeFor(AllSelectors.stream().filter(it -> it.eventSelector.eventType instanceof StoreEventType).collect(Collectors.toList()));
            subscribeFor(AllSelectors.stream().filter(it -> it.eventSelector.eventType instanceof InboxEventType).collect(Collectors.toList()));
            subscribeFor(AllSelectors.stream().filter(it -> it.eventSelector.eventType instanceof KvdbEventType).collect(Collectors.toList()));

            return AllSelectors.stream().map(eventSelectorExtra -> eventSelectorExtra.eventSelector.subscriptionId).collect(Collectors.toList());
        }
    }
}
