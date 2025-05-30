package com.simplito.java.privmx_endpoint.modules.kvdb;

import com.simplito.java.privmx_endpoint.model.KvdbEntry;
import com.simplito.java.privmx_endpoint.model.KvdbEntryPagingQuery;
import com.simplito.java.privmx_endpoint.model.KvdbKeysPagingQuery;
import com.simplito.java.privmx_endpoint.model.PagingQuery;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.Kvdb;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Objects;

public class KvdbApi implements AutoCloseable {
    private final Long api;

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    public KvdbApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    public native String createKvdb(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, ContainerPolicy policies) throws PrivmxException, NativeException, IllegalStateException;

    public String createKvdb(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta) {
        return createKvdb(contextId, users, managers, publicMeta, privateMeta, null);
    }

    public native void updateKvdb(String kvdbId,
                                  List<UserWithPubKey> users,
                                  List<UserWithPubKey> managers,
                                  byte[] publicMeta,
                                  byte[] privateMeta,
                                  long version,
                                  boolean force,
                                  boolean forceGenerateNewKey,
                                  ContainerPolicy policies
    ) throws PrivmxException, NativeException, IllegalStateException;

    public void updateKvdb(String kvdbId,
                           List<UserWithPubKey> users,
                           List<UserWithPubKey> managers,
                           byte[] publicMeta,
                           byte[] privateMeta,
                           long version,
                           boolean force,
                           boolean forceGenerateNewKey) {
        updateKvdb(kvdbId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
    }

    public native void deleteKvdb(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    public native Kvdb getKvdb(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    public native PagingList<Kvdb> listKvdbs(String contextId, PagingQuery pagingQuery) throws PrivmxException, NativeException, IllegalStateException;

    public native KvdbEntry getEntry(String kvdbId, String key) throws PrivmxException, NativeException, IllegalStateException;

    public native PagingList<String> listEntriesKeys(String kvdbId, KvdbKeysPagingQuery pagingQuery) throws PrivmxException, NativeException, IllegalStateException;

    public native PagingList<KvdbEntry> listEntries(String kvdbId, KvdbEntryPagingQuery pagingQuery) throws PrivmxException, NativeException, IllegalStateException;

    public native void setEntry(String kvdbId, String key, byte[] publicMeta, byte[] privateMeta, byte[] data, long version) throws PrivmxException, NativeException, IllegalStateException;

    public native void deleteEntry(String kvdbId, String key) throws PrivmxException, NativeException, IllegalStateException;

    public native void deleteEntries(String kvdbId, List<String> keys) throws PrivmxException, NativeException, IllegalStateException;

    public native void subscribeForKvdbEvents() throws PrivmxException, NativeException, IllegalStateException;

    public native void unsubscribeFromKvdbEvents() throws PrivmxException, NativeException, IllegalStateException;

    public native void subscribeForEntryEvents(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    public native void unsubscribeFromKvdbEvents(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    @Override
    public void close() throws Exception {
        deinit();
    }
}