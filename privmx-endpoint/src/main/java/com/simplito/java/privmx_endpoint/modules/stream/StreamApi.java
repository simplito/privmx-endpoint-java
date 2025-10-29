package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.LibLoader;
import com.simplito.java.privmx_endpoint.model.streams.Stream;
import com.simplito.java.privmx_endpoint.model.streams.StreamHandle;
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

    public native List<Stream> listStreams(String streamRoomId);

    public native StreamHandle createStream(String streamRoomId);

    public native List<MediaDevice> getMediaDevices();

    @Override
    public void close() throws Exception {
        deinit();
    }
}