//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.modules.kvdb;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.Kvdb;
import com.simplito.java.privmx_endpoint.model.KvdbEntry;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Manages PrivMX Bridge KVDBs and their entries.
 *
 * @category kvdb
 */
public class KvdbApi implements AutoCloseable {
    private final Long api;

    /**
     * Creates an instance of {@code KvdbApi}.
     *
     * @param connection instance of 'Connection'
     * @throws IllegalStateException when given {@link Connection} is not connected
     */
    public KvdbApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    /**
     * Creates a new KVDB in given Context.
     *
     * @param contextId   ID of the Context to create the KVDB in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created KVDB
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the created KVDB
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param policies    KVDB's policies
     * @return ID of the created KVDB
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native String createKvdb(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Creates a new KVDB in given Context.
     *
     * @param contextId   ID of the Context to create the KVDB in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created KVDB
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the created KVDB
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @return ID of the created KVDB
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public String createKvdb(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta
    ) throws PrivmxException, NativeException, IllegalStateException {
        return createKvdb(contextId, users, managers, publicMeta, privateMeta, null);
    }

    /**
     * Updates an existing KVDB.
     *
     * @param kvdbId              ID of the KVDB to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the created KVDB
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the created KVDB
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated KVDB
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the KVDB
     * @param policies            KVDB's policies
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void updateKvdb(
            String kvdbId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey,
            ContainerPolicy policies
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates an existing KVDB.
     *
     * @param kvdbId              ID of the KVDB to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the created KVDB
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the created KVDB
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated KVDB
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the KVDB
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public void updateKvdb(
            String kvdbId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey
    ) throws PrivmxException, NativeException, IllegalStateException {
        updateKvdb(kvdbId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
    }

    /**
     * Updates an existing KVDB.
     *
     * @param kvdbId      ID of the KVDB to update
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created KVDB
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the created KVDB
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param version     current version of the updated KVDB
     * @param force       force update (without checking version)
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public void updateKvdb(String kvdbId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, long version, boolean force) throws PrivmxException, NativeException, IllegalStateException {
        updateKvdb(kvdbId, users, managers, publicMeta, privateMeta, version, force, false, null);
    }

    /**
     * Deletes a KVDB by given KVDB ID.
     *
     * @param kvdbId ID of the KVDB to delete
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void deleteKvdb(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a KVDB by given KVDB ID.
     *
     * @param kvdbId ID of KVDB to get
     * @return object containing info about the KVDB
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Kvdb getKvdb(String kvdbId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of KVDBs in given Context.
     *
     * @param contextId   ID of the Context to get the KVDBs from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field by elements are sorted in result
     * @return list of KVDBs
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native PagingList<Kvdb> listKvdbs(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of KVDBs in given Context.
     *
     * @param contextId   ID of the Context to get the KVDBs from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of KVDBs
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Kvdb> listKvdbs(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listKvdbs(contextId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of KVDBs in given Context.
     *
     * @param contextId ID of the Context to get the KVDBs from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of KVDBs
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Kvdb> listKvdbs(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listKvdbs(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of KVDBs in given Context.
     *
     * @param contextId ID of the Context to get the KVDBs from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of KVDBs
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Kvdb> listKvdbs(
            String contextId,
            long skip,
            long limit,
            String sortOrder
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listKvdbs(contextId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a KVDB entry by given KVDB entry key and KVDB ID.
     *
     * @param kvdbId KVDB ID of the KVDB entry to get
     * @param key    key of the KVDB entry to get
     * @return object containing the KVDB entry
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native KvdbEntry getEntry(
            String kvdbId,
            String key
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Check whether the KVDB entry exists.
     *
     * @param kvdbId KVDB ID of the KVDB entry to check
     * @param key    key of the KVDB entry to check
     * @return 'true' if the KVDB has an entry with given key, 'false' otherwise
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native boolean hasEntry(
            String kvdbId,
            String key
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of KVDB entries keys from a KVDB.
     *
     * @param kvdbId      ID of the KVDB to list KVDB entries from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field by elements are sorted in result
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native PagingList<String> listEntriesKeys(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of KVDB entries keys from a KVDB.
     *
     * @param kvdbId      ID of the KVDB to list KVDB entries from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<String> listEntriesKeys(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntriesKeys(kvdbId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of KVDB entries keys from a KVDB.
     *
     * @param kvdbId    ID of the KVDB to list KVDB entries from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<String> listEntriesKeys(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntriesKeys(kvdbId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of KVDB entries keys from a KVDB.
     *
     * @param kvdbId    ID of the KVDB to list KVDB entries from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<String> listEntriesKeys(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntriesKeys(kvdbId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of KVDB entries from a KVDB.
     *
     * @param kvdbId      ID of the KVDB to list KVDB entries from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field by elements are sorted in result
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native PagingList<KvdbEntry> listEntries(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of KVDB entries from a KVDB.
     *
     * @param kvdbId      ID of the KVDB to list KVDB entries from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<KvdbEntry> listEntries(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(kvdbId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of KVDB entries from a KVDB.
     *
     * @param kvdbId    ID of the KVDB to list KVDB entries from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<KvdbEntry> listEntries(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(kvdbId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of KVDB entries from a KVDB.
     *
     * @param kvdbId    ID of the KVDB to list KVDB entries from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of KVDB entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<KvdbEntry> listEntries(
            String kvdbId,
            long skip,
            long limit,
            String sortOrder
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(kvdbId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Sets a KVDB entry in the given KVDB.
     *
     * @param kvdbId      ID of the KVDB to set the entry to
     * @param key         KVDB entry key
     * @param publicMeta  public KVDB entry metadata
     * @param privateMeta private KVDB entry metadata
     * @param data        content of the KVDB entry
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void setEntry(
            String kvdbId,
            String key,
            byte[] publicMeta,
            byte[] privateMeta,
            byte[] data,
            long version
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Sets a KVDB entry in the given KVDB.
     *
     * @param kvdbId      ID of the KVDB to set the entry to
     * @param key         KVDB entry key
     * @param publicMeta  public KVDB entry metadata
     * @param privateMeta private KVDB entry metadata
     * @param data        content of the KVDB entry
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public void setEntry(
            String kvdbId,
            String key,
            byte[] publicMeta,
            byte[] privateMeta,
            byte[] data
    ) throws PrivmxException, NativeException, IllegalStateException {
        setEntry(kvdbId, key, publicMeta, privateMeta, data, 0L);
    }

    /**
     * Deletes a KVDB entry by given KVDB entry ID.
     *
     * @param kvdbId KVDB ID of the KVDB entry to delete
     * @param key    key of the KVDB entry to delete
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void deleteEntry(
            String kvdbId,
            String key
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes KVDB entries by given KVDB IDs and the list of entry keys.
     *
     * @param kvdbId ID of the KVDB database to delete from
     * @param keys   vector of the keys of the KVDB entries to delete
     * @return map with the statuses of deletion for every key
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Map<String, Boolean> deleteEntries(
            String kvdbId,
            List<String> keys
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribe for the KVDB events on the given subscription query.
     *
     * @param subscriptionQueries list of queries
     * @return list of subscriptionIds in matching order to subscriptionQueries
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native List<String> subscribeFor(List<String> subscriptionQueries) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribe form events with the given subscriptionId.
     *
     * @param subscriptionIds list of subscriptionId
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFrom(List<String> subscriptionIds) throws PrivmxException, NativeException, IllegalStateException;

    private native String buildSubscriptionQuery(long eventType, long selectorType, String selectorId) throws PrivmxException, NativeException, IllegalStateException;


    /**
     * Generate subscription Query for the KVDB events.
     *
     * @param eventType    type of event you listen for
     * @param selectorType scope on which you listen for events
     * @param selectorId   ID of the selector
     * @return // todo - add return description
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public String buildSubscriptionQuery(KvdbEventType eventType, KvdbEventSelectorType selectorType, String selectorId) throws PrivmxException, NativeException, IllegalStateException {
        return buildSubscriptionQuery((long) eventType.ordinal(), (long) selectorType.ordinal(), selectorId);
    }

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