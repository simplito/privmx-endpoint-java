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

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * Implements a list of registered event callbacks.
 *
 * @category core
 */
public class EventDispatcher {

    private final Map<String, List<Pair>> map = new HashMap<>();
    private final EventCallback<String> onRemoveEntryKey;

    /**
     * Creates instance of {@code EventDispatcher}.
     *
     * @param onRemoveEntryKey callback triggered when all events
     *                         from channel entry have been removed
     *                         (it can also unsubscribe from the channel)
     */
    public EventDispatcher(EventCallback<String> onRemoveEntryKey) {
        this.onRemoveEntryKey = onRemoveEntryKey;
    }

    private String getFormattedType(String channel, String type) {
        return channel + "_" + type;
    }

    /**
     * Registers new event callback.
     *
     * @param channel  channel of registered event
     * @param type     type of registered event
     * @param context  ID of registered callback
     * @param callback block of code to call when the specified event has been caught
     * @return {@code true} if the channel is not already subscribed
     */
    public boolean register(String channel, String type, Object context, EventCallback<?> callback) {
        boolean needSubscribe = channelHasNoCallbacks(channel);
        getCallbacks(getFormattedType(channel, type)).add(new Pair(context, callback));
        return needSubscribe;
    }

    /**
     * Emits specified event. It should only be called by event loops.
     *
     * @param <T>   type of event data
     * @param event event data to emit
     */
    public <T> void emit(Event<T> event) {
        List<Pair> callbacks = getCallbacks(getFormattedType(event.channel, event.type));
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

    private boolean channelHasNoCallbacks(String channel) {
        synchronized (map) {
            return Optional.ofNullable(
                            map
                                    .entrySet()
                                    .stream()
                                    .collect(
                                            Collectors.groupingBy(
                                                    entry -> entry.getKey().split("_")[0],
                                                    Collectors.mapping(Map.Entry::getValue, Collectors.toList())
                                            )
                                    ).get(channel)
                    ).orElse(List.of())
                    .stream()
                    .flatMap(List::stream)
                    .count() == 0;
        }
    }

    /**
     * Removes all callbacks registered by {@link #register(String, String, Object, EventCallback)}. It's identified by given Context.
     *
     * @param context callback identifier
     */
    public void unbind(Object context) {
        synchronized (map) {
            map.entrySet()
                    .stream()
                    .map(entry ->
                            new AbstractMap.SimpleImmutableEntry<>(entry.getKey().split("_")[0], entry.getValue())
                    )
                    .filter(entry -> !entry.getValue().isEmpty())
                    .forEach(entry -> {
                        List<Pair> list = entry.getValue();
                        list.removeIf(pair -> pair.context == context);
                        if (channelHasNoCallbacks(entry.getKey())) {
                            onRemoveEntryKey.call(entry.getKey());
                        }
                    });
        }
    }

    /**
     * Removes all callbacks.
     */
    public void unbindAll() {
        map.keySet()
                .stream()
                .map(it -> it.split("_")[0])
                .collect(Collectors.toSet())
                .forEach(onRemoveEntryKey::call);
        map.clear();
    }

    private List<Pair> getCallbacks(String type) {
        synchronized (map) {
            if (!map.containsKey(type)) {
                map.put(type, new Vector<>());
            }
            return map.get(type);
        }
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
