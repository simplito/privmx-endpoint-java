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

package com.simplito.java.privmx_endpoint_extra.events;


import com.simplito.java.privmx_endpoint.model.Event;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CustomEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.CoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Implements a list of registered event callbacks.
 *
 * @category core
 */
public class EventDispatcher {

    private final Map<EventRegistrationInfo, List<Pair>> callbackMap = new HashMap<>();
    private final EventCallback<Map<SubscriptionModule, List<String>>> onRemoveSubscriptionEntry;

    /**
     * Creates instance of {@code EventDispatcher}.
     *
     * @param onRemoveSubscriptionEntry callback triggered when all events
     *                                  from channel entry have been removed
     *                                  (it can also unsubscribe from the channel)
     */
    public EventDispatcher(EventCallback<Map<SubscriptionModule, List<String>>> onRemoveSubscriptionEntry) {
        this.onRemoveSubscriptionEntry = onRemoveSubscriptionEntry;
    }

    /**
     * Registers a new callback for a specific event type.
     *
     * @param callbackRegistration the registration containing the event type, group, and callback
     * @return the registration info for the given event type
     */
    public EventRegistrationInfo registerCallback(CallbackRegistration<?> callbackRegistration) {
        EventRegistrationInfo info = getRegistrationInfo(callbackRegistration.eventType);
        getCallbackList(info).add(new Pair(callbackRegistration.callbackGroup, callbackRegistration.callback));
        return info;
    }


    /**
     * Emits specified event. It should only be called by event loops.
     *
     * @param <T>   type of event data
     * @param event event data to emit
     */
    public <T> void emit(Event<T> event) {
        List<Pair> callbacks = EventType.isLibEvent(event) ? getCallbacksByType(event.type) : getCallbacks(event.subscriptions);
        for (Pair p : callbacks) {
            try {
                EventCallback<T> e = (EventCallback<T>) p.callback;
                try {
                    e.call(event.data);
                } catch (Exception ignored) {
                }
            } catch (ClassCastException e) {
                System.out.println("Cannot process event: issue with cast event data");
            }
        }
    }

    /**
     * Removes all callbacks registered by {@link #registerCallback(CallbackRegistration)}. It's identified by given {@code callbackGroups}.
     *
     * @param callbackGroups one or more callback group identifiers used to select callbacks to unbind
     */
    public void unbind(Object... callbackGroups) {
        List<Object> callbackGroupsList = Arrays.asList(callbackGroups);
        if (callbackGroupsList.isEmpty()) return;
        synchronized (callbackMap) {
            Map<SubscriptionModule, List<String>> selectorsToUnsubscribe = new HashMap<>();
            Iterator<Map.Entry<EventRegistrationInfo, List<Pair>>> mapIterator = callbackMap.entrySet().iterator();

            while (mapIterator.hasNext()) {
                Map.Entry<EventRegistrationInfo, List<Pair>> entry = mapIterator.next();
                EventRegistrationInfo key = entry.getKey();
                List<Pair> callbacks = entry.getValue();

                List<Pair> pairsOfCallbacks = callbacks.stream().filter(p -> callbackGroupsList.contains(p.context)).collect(Collectors.toList());
                callbacks.removeAll(pairsOfCallbacks);
                callbackGroupsList.removeAll(pairsOfCallbacks);

                if (callbacks.isEmpty()) {
                    SubscriptionModule module = getModuleFromEventRegistrationInfo(key);
                    if (module != null) {
                        List<String> selectors = selectorsToUnsubscribe.getOrDefault(module, new ArrayList<>());
                        selectors.add(key.subscriptionID);
                        selectorsToUnsubscribe.put(module, selectors);
                    }

                    mapIterator.remove();
                }
            }
            onRemoveSubscriptionEntry.call(selectorsToUnsubscribe);
        }
    }

    /**
     * Removes all callbacks.
     */
    public void unbindAll() {
        synchronized (callbackMap) {
            Map<SubscriptionModule, List<String>> selectorsToUnsubscribe = new HashMap<>();
            callbackMap.keySet().forEach(it -> {
                SubscriptionModule module = getModuleFromEventRegistrationInfo(it);
                if (it.subscriptionID != null && module != null) {
                    List<String> subscriptionsList = selectorsToUnsubscribe.getOrDefault(module, new ArrayList<>());
                    subscriptionsList.add(it.subscriptionID);
                    selectorsToUnsubscribe.put(module, subscriptionsList);
                }
            });
            onRemoveSubscriptionEntry.call(selectorsToUnsubscribe);
        }
    }

    /**
     * Get reference to a list that can be used to add or remove callbacks.
     */
    private List<Pair> getCallbackList(EventRegistrationInfo eventRegistrationInfo) {
        synchronized (callbackMap) {
            if (!callbackMap.containsKey(eventRegistrationInfo)) {
                callbackMap.put(eventRegistrationInfo, new Vector<>());
            }
            return callbackMap.get(eventRegistrationInfo);
        }
    }

    /**
     * Get list of all callbacks identified by this subscriptionIds.
     */
    private List<Pair> getCallbacks(List<String> subscriptionIds) {
        synchronized (callbackMap) {
            return callbackMap.entrySet()
                    .stream()
                    .filter(it ->
                        it.getKey().subscriptionID != null && subscriptionIds.contains(it.getKey().subscriptionID)
                    )
                    .flatMap(it -> it.getValue().stream())
                    .collect(Collectors.toList());
        }
    }

    private List<Pair> getCallbacksByType(String eventType) {
        synchronized (callbackMap) {
            return callbackMap.entrySet()
                    .stream()
                    .filter(it ->
                            it.getKey().eventType.eventName.equals(eventType)
                    ).flatMap(it ->
                            it.getValue().stream()
                    )
                    .collect(Collectors.toList());
        }
    }

    private EventRegistrationInfo getRegistrationInfo(EventType<?> eventType) {
        synchronized (callbackMap) {
            return callbackMap.keySet()
                    .stream()
                    .filter(it -> it.eventType.equals(eventType))
                    .findFirst()
                    .orElse(new EventRegistrationInfo(null, eventType));
        }
    }

    /**
     * Removes all events, excluding internal library events, that do not have {@code subscriptionId}.
     */
    public void removeNotSubscribedEvents() {
        synchronized (callbackMap) {
            Iterator<Map.Entry<EventRegistrationInfo, List<EventDispatcher.Pair>>> entrySetIterator = callbackMap.entrySet().iterator();
            while (entrySetIterator.hasNext()) {
                EventRegistrationInfo eventRegistrationInfo = entrySetIterator.next().getKey();
                if (eventRegistrationInfo.subscriptionID == null && !eventRegistrationInfo.eventType.isLibEvent()) {
                    entrySetIterator.remove();
                }
            }
        }
    }

    private static SubscriptionModule getModuleFromEventRegistrationInfo(EventRegistrationInfo key) {
        SubscriptionModule module = null;
        if (key.eventType.eventSelectorType instanceof CustomEventSelectorType) {
            module = SubscriptionModule.CUSTOM_EVENT;
        } else if (key.eventType.libEventType instanceof ThreadEventType) {
            module = SubscriptionModule.THREAD;
        } else if (key.eventType.libEventType instanceof StoreEventType) {
            module = SubscriptionModule.STORE;
        } else if (key.eventType.libEventType instanceof InboxEventType) {
            module = SubscriptionModule.INBOX;
        } else if (key.eventType.libEventType instanceof KvdbEventType) {
            module = SubscriptionModule.KVDB;
        } else if (key.eventType.libEventType instanceof CoreEventType) {
            module = SubscriptionModule.CORE;
        }
        return module;
    }


    /**
     * Available subscription modules for event handling.
     */
    public enum SubscriptionModule {
        /**
         * Thread module case.
         */
        THREAD,
        /**
         * Store module case.
         */
        STORE,
        /**
         * Inbox module case.
         */
        INBOX,
        /**
         * Custom Event module case.
         */
        CUSTOM_EVENT,
        /**
         * KVDB module case.
         */
        KVDB,
        /**
         * CoreModules
         */
        CORE
    }

    private static class Pair {
        private final Object context;
        private final EventCallback<?> callback;

        private Pair(Object context, EventCallback<?> callback) {
            this.context = context;
            this.callback = callback;
        }
    }

    /**
     * Holds essential information about a single event registration.
     */
    public static class EventRegistrationInfo {

        /**
         * Unique identifier for the event subscription
         */
        public String subscriptionID;
        /**
         * Type of registered event
         */
        public EventType<?> eventType;

        private EventRegistrationInfo(String subscriptionID, EventType<?> eventType) {
            this.subscriptionID = subscriptionID;
            this.eventType = eventType;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof EventRegistrationInfo)) return false;
            EventRegistrationInfo that = (EventRegistrationInfo) o;
            return Objects.equals(subscriptionID, that.subscriptionID) &&
                    Objects.equals(eventType, that.eventType);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subscriptionID, eventType);
        }
    }
}
