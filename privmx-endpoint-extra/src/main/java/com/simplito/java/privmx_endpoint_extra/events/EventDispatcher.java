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

    private final Map<EventRegistrationInfo, List<Pair>> map2 = new HashMap<>();
    private final EventCallback <Map<String, List<String>>> onRemoveEntryKey;

    /**
     * Creates instance of {@code EventDispatcher}.
     *
     * @param onRemoveEntryKey callback triggered when all events
     *                         from channel entry have been removed
     *                         (it can also unsubscribe from the channel)
     */
    public EventDispatcher(EventCallback<Map<String, List<String>>> onRemoveEntryKey) {
        this.onRemoveEntryKey = onRemoveEntryKey;
    }

    public EventRegistrationInfo registerCallback(
            CallbackRegistration callbackRegistration
    ) {
        EventRegistrationInfo info = new EventRegistrationInfo(null,callbackRegistration.eventType);
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
        List<Pair> callbacks = event.type.startsWith("lib") ? getCallbacksByType(event.type): getCallbacks(event.subscriptions);
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

    private boolean eventHasNoCallbacks(EventRegistrationInfo eventInfo) {
        synchronized (map2) {
            List<Pair> callbacks = map2.get(eventInfo);
            return callbacks == null || callbacks.isEmpty();
        }
    }

    /**
     * Removes all callbacks registered by {@link #registerCallback(CallbackRegistration)}. It's identified by given {@code callbackGroups}.
     */
    public void unbind(Object... callbackGroups) {
        List<Object> callbackGroupsList = Arrays.asList(callbackGroups);
        if(callbackGroupsList.isEmpty()) return;
        synchronized (map2) {
            Map<String, List<String>> selectorsToUnsubscribe = new HashMap<>();
            Iterator<Map.Entry<EventRegistrationInfo,List<Pair>>> mapIterator = map2.entrySet().iterator();

            while(mapIterator.hasNext()){
                Map.Entry<EventRegistrationInfo,List<Pair>> entry = mapIterator.next();
                EventRegistrationInfo key = entry.getKey();
                List<Pair> callbacks = entry.getValue();

                List<Pair> pairsOfCallbacks = callbacks.stream().filter(p -> callbackGroupsList.contains(p.context)).collect(Collectors.toList());
                callbacks.removeAll(pairsOfCallbacks);
                callbackGroupsList.removeAll(pairsOfCallbacks);

                if (callbacks.isEmpty()) {
                    String module = getModuleFromEventRegistrationInfo(key);
                    if(module != null){
                        List<String> selectors = selectorsToUnsubscribe.getOrDefault(module, new ArrayList<>());
                        selectors.add(key.subscriptionID);
                        selectorsToUnsubscribe.put(module,selectors);
                    }

                    mapIterator.remove();
                }
            }
            onRemoveEntryKey.call(selectorsToUnsubscribe);
        }
    }

    /**
     * Removes all callbacks.
     */
    public void unbindAll() {
        synchronized (map2){
            List<Object> callbacksToUnbind = map2.entrySet()
                    .stream()
                    .flatMap(it->it.getValue().stream())
                    .map(it->it.context)
                    .collect(Collectors.toList());
            unbind(callbacksToUnbind);
        }
    }

    /**
     * Get reference to list for adding or removing callbacks.
     */
    private List<Pair> getCallbackList(EventRegistrationInfo eventRegistrationInfo) {
        synchronized (map2) {
            if (!map2.containsKey(eventRegistrationInfo)) {
                map2.put(eventRegistrationInfo, new Vector<>());
            }
            return map2.get(eventRegistrationInfo);
        }
    }

    /**
     * Get list of all callbacks identified by this subscriptionIds.
     */
    private List<Pair> getCallbacks(List<String> subscriptionIds) {
        synchronized (map2) {
            List<Pair> pairs = new ArrayList<>();

            for (Map.Entry<EventRegistrationInfo, List<Pair>> entry : map2.entrySet()) {
                String subId = entry.getKey().subscriptionID;
                if (subId != null && subscriptionIds.contains(subId)) {
                    pairs.addAll(entry.getValue());
                }
            }
            return pairs;
        }
    }

    private List<Pair> getCallbacksByType(String eventType){
        synchronized (map2){
            return map2.entrySet().stream().filter(it->it.getKey().eventType.eventName.equals(eventType)).flatMap(it->it.getValue().stream()).collect(Collectors.toList());
        }
    }

    public void removeNotSubscribedEvents(){
        synchronized (map2) {
            Iterator<Map.Entry<EventRegistrationInfo, List<EventDispatcher.Pair>>> entrySetIterator = map2.entrySet().iterator();
            while (entrySetIterator.hasNext()){
                EventRegistrationInfo eventRegistrationInfo = entrySetIterator.next().getKey();
                if (eventRegistrationInfo.subscriptionID == null && !eventRegistrationInfo.eventType.eventName.startsWith("lib")) {
                    entrySetIterator.remove();
                }
            }
        }
    }

    private static String getModuleFromEventRegistrationInfo(EventRegistrationInfo key) {
        String module = null;
        if (key.eventType.eventSelectorType instanceof CustomEventSelectorType) {
            module = "custom";
        } else if (key.eventType.libEventType instanceof ThreadEventType) {
            module = "thread";
        } else if (key.eventType.libEventType instanceof StoreEventType) {
            module = "store";
        } else if (key.eventType.libEventType instanceof InboxEventType) {
            module = "inbox";
        } else if (key.eventType.libEventType instanceof KvdbEventType) {
            module = "kvdb";
        }
        return module;
    }

    private static class Pair {
        private final Object context;
        private final EventCallback<?> callback;

        private Pair(Object context, EventCallback<?> callback) {
            this.context = context;
            this.callback = callback;
        }
    }

    public static class EventRegistrationInfo{
        public String subscriptionID;
        public EventType<?> eventType;
        public EventRegistrationInfo(String subscriptionID, EventType<?> eventType){
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
            return Objects.hash(subscriptionID,eventType);
        }
    }
}
