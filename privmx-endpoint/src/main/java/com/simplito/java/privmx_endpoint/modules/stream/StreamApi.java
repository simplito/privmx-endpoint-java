package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.LibLoader;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StreamEventType;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.event.EventApi;

import java.util.List;
import java.util.Objects;

public class StreamApi implements AutoCloseable {
    static {
        LibLoader.loadPrivmxLibraries();
    }
    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    private native Long init(Connection connection, EventApi eventApi) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    public StreamApi(Connection connection, EventApi eventApi) throws IllegalStateException {
        Objects.requireNonNull(connection);
        Objects.requireNonNull(eventApi);
        this.api = init(connection, eventApi);
    }

    public native List<String> subscribeFor(List<String> subscriptionQueries);

    @Override
    public void close() throws Exception {
        deinit();
    }
}