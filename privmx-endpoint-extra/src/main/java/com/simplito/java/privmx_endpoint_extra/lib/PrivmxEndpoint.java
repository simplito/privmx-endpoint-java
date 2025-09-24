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
import com.simplito.java.privmx_endpoint.model.PKIVerificationOptions;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CustomEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.CoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.crypto.CryptoApi;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventCallback;
import com.simplito.java.privmx_endpoint_extra.events.EventDispatcher;
import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Extends {@link BasicPrivmxEndpoint} with event callbacks dispatcher.
 *
 * @category core
 */
public class PrivmxEndpoint extends BasicPrivmxEndpoint implements AutoCloseable {
    private final EventCallback<Map<EventDispatcher.SubscriptionModule, List<String>>> onRemove = (map) -> {
        try {
            map.forEach(this::unsubscribeMany);
        } catch (Exception ignore) {
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

    /**
     * Unregisters all event callbacks associated with the specified callback groups.
     */
    public void unregisterCallbacks(Object... callbackGroups) {
        eventDispatcher.unbind(callbackGroups);
    }

    /**
     * Unregisters all registered callbacks.
     */
    public void unregisterAll() {
        eventDispatcher.unbindAll();
    }

    /**
     * Handles event and invokes all related callbacks. It should only be called by event loops.
     */
    public void handleEvent(Event<?> event) {
        eventDispatcher.emit(event);
    }

    public <T> RegistrationResult registerCallback(
            Object callbackGroup,
            EventType<T> eventType,
            EventCallback<T> callback
    ) {
        return registerManyCallbacks(new CallbackRegistration<>(callbackGroup, eventType, callback)).get(0);
    }

    public List<RegistrationResult> registerManyCallbacks(
            CallbackRegistration<?>... registrations
    ) {
        List<CallbackRegistrationWithResult> results = Arrays.stream(registrations).map(it -> new CallbackRegistrationWithResult(it, null)).collect(Collectors.toList());
        final Map<EventDispatcher.SubscriptionModule, EventsToSubscribe> eventsToSubscribeByModule = new HashMap<>();

        for (CallbackRegistrationWithResult result : results) {
            CallbackRegistration<?> registration = result.registration;
            EventDispatcher.SubscriptionModule module = null;
            String query = null;
            EventType<?> eventType = registration.eventType;

            EventDispatcher.EventRegistrationInfo registrationInfo = eventDispatcher.registerCallback(registration);

            if (registrationInfo.subscriptionID != null || eventType.isLibEvent()) {
                result.result = new RegistrationResult(null);
            } else {
                if (eventType.channelName != null && eventType.eventSelectorType instanceof CustomEventSelectorType) {
                    module = EventDispatcher.SubscriptionModule.CUSTOM_EVENT;
                    query = eventApi.buildSubscriptionQuery(
                            eventType.channelName,
                            (CustomEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventType.libEventType instanceof ThreadEventType) {
                    module = EventDispatcher.SubscriptionModule.THREAD;
                    query = threadApi.buildSubscriptionQuery(
                            (ThreadEventType) eventType.libEventType,
                            (ThreadEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventType.libEventType instanceof StoreEventType) {
                    module = EventDispatcher.SubscriptionModule.STORE;
                    query = storeApi.buildSubscriptionQuery(
                            (StoreEventType) eventType.libEventType,
                            (StoreEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventType.libEventType instanceof InboxEventType) {
                    module = EventDispatcher.SubscriptionModule.INBOX;
                    query = inboxApi.buildSubscriptionQuery(
                            (InboxEventType) eventType.libEventType,
                            (InboxEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventType.libEventType instanceof KvdbEventType) {
                    module = EventDispatcher.SubscriptionModule.INBOX;
                    query = kvdbApi.buildSubscriptionQuery(
                            (KvdbEventType) eventType.libEventType,
                            (KvdbEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                } else if (eventType.libEventType instanceof CoreEventType) {
                    module = EventDispatcher.SubscriptionModule.CORE;
                    query = connection.buildSubscriptionQuery(
                            (CoreEventType) eventType.libEventType,
                            (CoreEventSelectorType) eventType.eventSelectorType,
                            eventType.eventSelectorId
                    );
                }
                EventsToSubscribe eventsToSubscribe = eventsToSubscribeByModule.getOrDefault(
                        module,
                        new EventsToSubscribe()
                );
                eventsToSubscribe.add(query, result, registrationInfo);
                eventsToSubscribeByModule.put(module, eventsToSubscribe);
            }
        }
        subscribeAll(eventsToSubscribeByModule);
        return results.stream().map(it -> it.result).collect(Collectors.toList());
    }

    private void unsubscribeMany(EventDispatcher.SubscriptionModule module, List<String> subscriptionIds) throws IllegalStateException, NativeException, PrivmxException {
        switch (module) {
            case CUSTOM_EVENT:
                if (eventApi == null)
                    throw new IllegalStateException("eventApi is not initialized");
                eventApi.unsubscribeFrom(subscriptionIds);
                break;
            case THREAD:
                if (threadApi == null)
                    throw new IllegalStateException("threadApi is not initialized");
                threadApi.unsubscribeFrom(subscriptionIds);
                break;
            case STORE:
                if (storeApi == null)
                    throw new IllegalStateException("storeApi is not initialized");
                storeApi.unsubscribeFrom(subscriptionIds);
                break;
            case INBOX:
                if (inboxApi == null)
                    throw new IllegalStateException("inboxApi is not initialized");
                inboxApi.unsubscribeFrom(subscriptionIds);
                break;
            case KVDB:
                if (kvdbApi == null) throw new IllegalStateException("kvdbApi is not initialized");
                kvdbApi.unsubscribeFrom(subscriptionIds);
                break;
            case CORE:
                if (connection == null) throw new IllegalStateException("Connection is not initialized");
                connection.unsubscribeFrom(subscriptionIds);
                break;
        }
    }

    private void subscribeFor(Map<String, List<EventToSubscribe>> queriesAndCallbacks, Function<List<String>, List<String>> subscribeMethod) throws IllegalStateException, PrivmxException, NativeException, NullPointerException {
        List<String> queries = new ArrayList<>(queriesAndCallbacks.keySet());
        List<String> ids = subscribeMethod.apply(queries);

        if (ids.size() == queries.size()) {
            for (int i = 0; i < ids.size(); i++) {
                String query = queries.get(i);
                final String id = ids.get(i);
                queriesAndCallbacks.get(query).forEach(subscribedEvent -> {
                    subscribedEvent.eventRegistrationInfo.subscriptionID = id;
                    subscribedEvent.callbackRegistrationWithResult.result = new RegistrationResult(null);
                });
            }
        }
    }

    private void subscribeAll(Map<EventDispatcher.SubscriptionModule, EventsToSubscribe> eventsToSubscribeByModule) {
        eventsToSubscribeByModule.forEach((key, value) -> {
            try {
                switch (key) {
                    case CUSTOM_EVENT:
                        if (eventApi == null) {
                            throw new IllegalStateException("eventApi is not initialized");
                        }
                        subscribeFor(value.queriesMap, eventApi::subscribeFor);
                        break;
                    case THREAD:
                        if (threadApi == null) {
                            throw new IllegalStateException("threadApi is not initialized");
                        }
                        subscribeFor(value.queriesMap, threadApi::subscribeFor);
                        break;
                    case STORE:
                        if (storeApi == null) {
                            throw new IllegalStateException("storeApi is not initialized");
                        }
                        subscribeFor(value.queriesMap, storeApi::subscribeFor);
                        break;
                    case INBOX:
                        if (inboxApi == null) {
                            throw new IllegalStateException("inboxApi is not initialized");
                        }
                        subscribeFor(value.queriesMap, inboxApi::subscribeFor);
                        break;
                    case KVDB:
                        if (kvdbApi == null) {
                            throw new IllegalStateException("kvdbApi is not initialized");
                        }
                        subscribeFor(value.queriesMap, kvdbApi::subscribeFor);
                        break;
                    case CORE:
                        if (connection == null) {
                            throw new IllegalStateException("Connection is not initialized");
                        }
                        subscribeFor(value.queriesMap, connection::subscribeFor);
                        break;
                }
            } catch (IllegalStateException | NativeException | PrivmxException e) {
                List<EventToSubscribe> callbacks = value.queriesMap.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
                callbacks.forEach(it -> {
                    it.callbackRegistrationWithResult.result = new RegistrationResult(e);
                });
            }
        });
        eventDispatcher.removeNotSubscribedEvents();
    }

    private static class CallbackRegistrationWithResult {
        public final CallbackRegistration<?> registration;
        public RegistrationResult result;

        private CallbackRegistrationWithResult(
                CallbackRegistration<?> registration,
                RegistrationResult result
        ) {
            this.registration = registration;
            this.result = result;
        }
    }

    public static class RegistrationResult {
        private final Throwable exception;

        private RegistrationResult(Throwable exception) {
            this.exception = exception;
        }

        public boolean isError() {
            return exception != null;
        }

        public Throwable getError() {
            return this.exception;
        }
    }

    private static class EventToSubscribe {
        private final PrivmxEndpoint.CallbackRegistrationWithResult callbackRegistrationWithResult;
        private final EventDispatcher.EventRegistrationInfo eventRegistrationInfo;

        private EventToSubscribe(PrivmxEndpoint.CallbackRegistrationWithResult callbackRegistrationWithResult, EventDispatcher.EventRegistrationInfo eventRegistrationInfo) {
            this.callbackRegistrationWithResult = callbackRegistrationWithResult;
            this.eventRegistrationInfo = eventRegistrationInfo;
        }
    }

    private static class EventsToSubscribe {
        private final Map<String, List<EventToSubscribe>> queriesMap = new HashMap<>();

        private void add(String query, PrivmxEndpoint.CallbackRegistrationWithResult callbackRegistrationWithResult, EventDispatcher.EventRegistrationInfo eventRegistrationInfo) {
            List<EventToSubscribe> listToAdd = queriesMap.getOrDefault(query, new ArrayList<>());
            listToAdd.add(new EventToSubscribe(callbackRegistrationWithResult, eventRegistrationInfo));
            queriesMap.put(query, listToAdd);
        }
    }
}