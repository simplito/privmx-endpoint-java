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
import com.simplito.java.privmx_endpoint.model.EventSelector;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CustomEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Implements a list of registered event callbacks.
 *
 * @category core
 */
public class EventDispatcher {

    private final Map<String, List<Pair>> map = new HashMap<>();
    private final Map<EventSelector, List<Pair>> map2 = new HashMap<>();
    private final Map<EventSelector, List<Pair>> map3 = new HashMap<>();
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

    public String registerCallback(EventSelector eventSelector, Object callbackId, EventCallback<?> callback) {
        boolean needSubscribe = eventHasNoCallbacks(eventSelector);
        getCallbacks(eventSelector).add(new Pair(callbackId, callback));

        if (!needSubscribe) return null;
        return getSubscriptionId(eventSelector);
    }

    private String getSubscriptionId(EventSelector eventSelector) {
        for (EventSelector key : map2.keySet()) {
            if (key.equals(eventSelector)) {
                return key.subscriptionId;
            }
        }
        return null;
    }

    /**
     * Emits specified event. It should only be called by event loops.
     *
     * @param <T>   type of event data
     * @param event event data to emit
     */
    public <T> void emit(Event<T> event) {
        List<Pair> callbacks = getCallbacks(event.subscriptions);
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

    private boolean eventHasNoCallbacks(EventSelector eventSelector) {
        synchronized (map2) {
            List<Pair> callbacks = map2.get(eventSelector);
            return callbacks == null || callbacks.isEmpty();
        }
    }

    /**
     * Removes all callbacks registered by {@link #(String, String, Object, EventCallback)}. It's identified by given Context.
     */
//    public void unbind(Object context) {
//        synchronized (map) {
//            List<EventSelector> selectorsToUnsubscribe = new ArrayList<>();
//
//            map2.forEach((selector, pairs) -> {
//                pairs.removeIf(it -> it.context == context);
//                if (pairs.isEmpty()) {
//                    selectorsToUnsubscribe.add(selector);
//                    map2.remove(selector);
//                }
//            });
//
//            if (!selectorsToUnsubscribe.isEmpty()) onRemoveEntryKey.call(selectorsToUnsubscribe);
//        }
//    }
    public void unbind(List<Object> callbackIds) {
        synchronized (map2) {
            Map<String, List<String>> selectorsToUnsubscribe = new HashMap<>();
            List<EventSelector> emptySelectors = new ArrayList<>();

            map2.forEach((key, callbacks) -> {
                if(callbackIds.isEmpty()) return;

                List<Pair> pairsOfCallbacks = callbacks.stream().filter(p -> callbackIds.contains(p.context)).collect(Collectors.toList());
                callbacks.removeAll(pairsOfCallbacks);
                callbackIds.removeAll(pairsOfCallbacks);

                if (callbacks.isEmpty()) {
                    emptySelectors.add(key);

                    if (key.eventSelectorType instanceof CustomEventSelectorType) {
                        selectorsToUnsubscribe.putIfAbsent("custom", new ArrayList<>()).add(key.subscriptionId);
                    } else if (key.eventType instanceof ThreadEventType) {
                        selectorsToUnsubscribe.putIfAbsent("thread", new ArrayList<>()).add(key.subscriptionId);
                    } else if (key.eventType instanceof StoreEventType) {
                        selectorsToUnsubscribe.putIfAbsent("store", new ArrayList<>()).add(key.subscriptionId);
                    } else if (key.eventType instanceof InboxEventType) {
                        selectorsToUnsubscribe.putIfAbsent("inbox", new ArrayList<>()).add(key.subscriptionId);
                    } else if (key.eventType instanceof KvdbEventType) {
                        selectorsToUnsubscribe.putIfAbsent("kvdb", new ArrayList<>()).add(key.subscriptionId);
                    }
                }
            });

            emptySelectors.forEach(map2::remove);
            onRemoveEntryKey.call(selectorsToUnsubscribe);
        }
    }

    /**
     * Removes all callbacks.
     */
    public void unbindAll() {
        map2.forEach((k, v) ->{
            List<Object> callbacksToUnbind = v.stream().map(it -> it.context).collect(Collectors.toList());
            unbind(callbacksToUnbind);
        });
    }

    private List<Pair> getCallbacks(EventSelector eventSelector) {
        synchronized (map2) {
            if (!map2.containsKey(eventSelector)) {
                map2.put(eventSelector, new Vector<>());
            }
            return map2.get(eventSelector);
        }
    }

    private List<Pair> getCallbacks(List<String> subscriptionIds) {
        synchronized (map2) {
            List<Pair> pairs = new ArrayList<>();

            for (Map.Entry<EventSelector, List<Pair>> entry : map2.entrySet()) {
                String subId = entry.getKey().subscriptionId;
                if (subId != null && subscriptionIds.contains(subId)) {
                    pairs.addAll(entry.getValue());
                }
            }
            return pairs;
        }
    }

    public void assignSubscriptionId(EventSelector eventSelector) {
        Optional<EventSelector> es = map2.keySet().stream().filter(key -> key.equals(eventSelector)).findFirst();
        es.ifPresent(it -> it.subscriptionId = eventSelector.subscriptionId);
    }

    private static class Pair {
        private final Object context;
        private final EventCallback<?> callback;

        private Pair(Object context, EventCallback<?> callback) {
            this.context = context;
            this.callback = callback;
        }
    }
}
