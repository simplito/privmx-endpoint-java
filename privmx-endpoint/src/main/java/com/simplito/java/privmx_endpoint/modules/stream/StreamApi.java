package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.LibLoader;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
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

    public native String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    );

    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta
    ) {
        return createStreamRoom(contextId, users, managers, publicMeta, privateMeta, null);
    }

    @Override
    public void close() throws Exception {
        deinit();
    }
}