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
import com.simplito.java.privmx_endpoint.modules.crypto.CryptoApi;
import com.simplito.java.privmx_endpoint_extra.events.EventCallback;
import com.simplito.java.privmx_endpoint_extra.events.EventDispatcher;
import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extends {@link BasicPrivmxEndpoint} with event callbacks dispatcher.
 *
 * @category core
 */
public class PrivmxEndpoint extends BasicPrivmxEndpoint implements AutoCloseable {
    private final EventCallback<String> onRemoveChannel = (channel) -> {
        try {
            unsubscribeChannel(channel);
        } catch (Exception e) {
            System.out.println("Cannot unsubscribe channel");
        }
    };
    private final EventDispatcher eventDispatcher = new EventDispatcher(onRemoveChannel);

    /**
     * Calls {@link BasicPrivmxEndpoint#BasicPrivmxEndpoint(Set, String, String, String)}.
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
    public PrivmxEndpoint(
            Set<Modules> enableModule,
            String userPrivateKey,
            String solutionId,
            String bridgeUrl
    ) throws IllegalStateException, PrivmxException, NativeException {
        super(enableModule, userPrivateKey, solutionId, bridgeUrl);
    }

    /**
     * Registers callbacks with the specified type.
     *
     * @param context   an object that identifies callbacks in the list
     * @param eventType type of event to listen to
     * @param callback  a block of code to execute when event was handled
     * @param <T>       type of data passed to callback
     * @throws RuntimeException thrown when method encounters an exception during subscribing on channel.
     */
    public final <T> void registerCallback(Object context, EventType<T> eventType, EventCallback<T> callback) throws RuntimeException {
        if (eventDispatcher.register(eventType.channel, eventType.eventType, context, callback)) {
            try {
                subscribeChannel(eventType.channel);
            } catch (Exception e) {
                throw new RuntimeException("Cannot subscribe event channel for this event (detail message: " + e.getMessage() + ")");
            }
        }
    }

    /**
     * Unregisters all callbacks registered by {@link #registerCallback(Object, EventType, EventCallback)} and identified with given Context.
     *
     * @param context an object that identifies callbacks in the list.
     */
    public void unregisterCallbacks(Object context) {
        eventDispatcher.unbind(context);
    }

    /**
     * Unregisters all callbacks registered by {@link #registerCallback(Object, EventType, EventCallback)}.
     */
    public void unregisterAll() {
        eventDispatcher.unbindAll();
    }

    /**
     * Handles event and invokes all related callbacks. It should only be called by event loops.
     *
     * @param event event to handle
     */
    public void handleEvent(Event<?> event) {
        eventDispatcher.emit(event);
    }

    private void subscribeChannel(String channelStr) {
        Channel channel = Channel.fromString(channelStr);
        if (channel == null) {
            System.out.println("Cannot subscribe on events channel (pattern not found)");
            return;
        }
        if (channel.module.startsWith("thread") && threadApi != null) {
            if (channel.type != null && channel.type.equals("messages")) {
                if (channel.instanceId != null) {
                    threadApi.subscribeForMessageEvents(channel.instanceId);
                } else {
                    System.out.println("No threadId to subscribeChannel: " + channelStr);
                }
                return;
            }
            threadApi.subscribeForThreadEvents();
            return;
        }

        if (channel.module.startsWith("store") && storeApi != null) {
            if (channel.type != null && channel.type.equals("files")) {
                if (channel.instanceId != null) {
                    storeApi.subscribeForFileEvents(channel.instanceId);
                } else {
                    System.out.println("No storeId to subscribeChannel: " + channelStr);
                }
                return;
            }
            storeApi.subscribeForStoreEvents();
            return;
        }

        if (channel.module.startsWith("inbox") && inboxApi != null) {
            if (channel.type != null && channel.type.equals("entries")) {
                if (channel.instanceId != null) {
                    inboxApi.subscribeForEntryEvents(channel.instanceId);
                } else {
                    System.out.println("No inboxId to subscribeChannel: " + channelStr);
                }
                return;
            }
            inboxApi.subscribeForInboxEvents();
        }

        if (channel.module.startsWith("kvdb") && kvdbApi != null) {
            if (channel.type != null && channel.type.equals("entries")) {
                if (channel.instanceId != null) {
                    kvdbApi.subscribeForEntryEvents(channel.instanceId);
                } else {
                    System.out.println("No kvdbId to subscribeChannel: " + channelStr);
                }
                return;
            }
            kvdbApi.subscribeForKvdbEvents();
        }
    }

    private void unsubscribeChannel(String channelStr) {
        Channel channel = Channel.fromString(channelStr);
        if (channel == null) {
            System.out.println("Cannot unsubscribe on events channel (pattern not found)");
            return;
        }
        if (channel.module.startsWith("thread") && threadApi != null) {
            if (channel.type != null && channel.type.equals("messages")) {
                if (channel.instanceId != null) {
                    threadApi.unsubscribeFromMessageEvents(channel.instanceId);
                } else {
                    System.out.println("No threadId to unsubscribeChannel: " + channelStr);
                }
                return;
            }
            threadApi.unsubscribeFromThreadEvents();
            return;
        }

        if (channel.module.startsWith("store") && storeApi != null) {
            if (channel.type != null && channel.type.equals("files")) {
                if (channel.instanceId != null) {
                    storeApi.unsubscribeFromFileEvents(channel.instanceId);
                } else {
                    System.out.println("No storeId to unsubscribeChannel: " + channelStr);
                }
                return;
            }
            storeApi.unsubscribeFromStoreEvents();
            return;
        }

        if (channel.module.startsWith("inbox") && inboxApi != null) {
            if (channel.type != null && channel.type.equals("entries")) {
                if (channel.instanceId != null) {
                    inboxApi.unsubscribeFromEntryEvents(channel.instanceId);
                } else {
                    System.out.println("No inboxId to unsubscribeChannel: " + channelStr);
                }
                return;
            }
            inboxApi.unsubscribeFromInboxEvents();
        }

        if (channel.module.startsWith("kvdb") && kvdbApi != null) {
            if (channel.type != null && channel.type.equals("entries")) {
                if (channel.instanceId != null) {
                    kvdbApi.unsubscribeFromKvdbEvents(channel.instanceId);
                } else {
                    System.out.println("No kvdbId to unsubscribeChannel: " + channelStr);
                }
                return;
            }
            kvdbApi.unsubscribeFromKvdbEvents();
        }
    }

    private static class Channel {
        private final String module;
        private final String instanceId;
        private final String type;

        private Channel(
                String module,
                String instanceID,
                String type
        ) {
            this.module = module;
            this.instanceId = instanceID;
            this.type = type;
        }

        private static Channel fromString(String channel) {
            Matcher matcher = Pattern
                    .compile("(?<module>(?:(?!/).)*)(/(?<instanceId>(?:(?!/).)*)/(?<type>(?:(?!/).)*))?")
                    .matcher(channel);
            if (!matcher.find()) {
                return null;
            }
            String module = matcher.group("module");
            String instanceId = matcher.group("instanceId");
            String type = matcher.group("type");
            return new Channel(
                    module,
                    instanceId,
                    type
            );
        }
    }
}
