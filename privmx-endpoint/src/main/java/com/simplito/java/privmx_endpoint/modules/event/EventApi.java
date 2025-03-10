package com.simplito.java.privmx_endpoint.modules.event;

import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Objects;

public class EventApi implements AutoCloseable {
    private final Long api;

    public EventApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    public native void emitEvent(String contextId, String channelName, byte[] eventData, List<UserWithPubKey> users);

    public native void subscribeForCustomEvents(String contextId, String channelName);

    public native void unsubscribeFromCustomEvents(String contextId, String channelName);

    /**
     * Frees memory.
     *
     * @throws Exception when instance is currently closed.
     */
    @Override
    public void close() throws Exception {
        deinit();
    }
}
