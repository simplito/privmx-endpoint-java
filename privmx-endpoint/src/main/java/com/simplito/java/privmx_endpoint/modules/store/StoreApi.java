//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.modules.store;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.Store;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.StoreDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.StoreFileDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.StoreStatsChangedEventData;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Objects;

/**
 * Manages PrivMX Bridge Stores and Files.
 *
 * @category store
 */
public class StoreApi implements AutoCloseable {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    /**
     * Initialize Store module on passed active connection.
     *
     * @param connection active connection to PrivMX Bridge
     * @throws IllegalStateException when passed {@link Connection} is not connected.
     */
    public StoreApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    /**
     * Creates a new Store in given Context.
     *
     * @param contextId   ID of the Context to create the Store in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created Store
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the
     *                    created Store
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @return Created Store ID
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeCreated
     * channel: store
     * payload: {@link Store}
     */
    public String createStore(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta) throws PrivmxException, NativeException, IllegalStateException {
        return createStore(contextId, users, managers, publicMeta, privateMeta, null);
    }

    /**
     * Creates a new Store in given Context.
     *
     * @param contextId   ID of the Context to create the Store in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created Store
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the
     *                    created Store
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param policies    additional container access policies
     * @return Created Store ID
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeCreated
     * channel: store
     * payload: {@link Store}
     */
    public native String createStore(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, ContainerPolicy policies) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates an existing Store.
     *
     * @param storeId     ID of the Store to update
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the updated Store
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the
     *                    updated Store
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param version     current version of the updated Store
     * @param force       force update (without checking version)
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeUpdated
     * channel: store
     * payload: {@link Store}
     */
    public void updateStore(String storeId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, long version, boolean force) throws PrivmxException, NativeException, IllegalStateException {
        updateStore(storeId, users, managers, publicMeta, privateMeta, version, force, false);
    }

    /**
     * Updates an existing Store.
     *
     * @param storeId             ID of the Store to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the updated Store
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the
     *                            updated Store
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated Store
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Store
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeUpdated
     * channel: store
     * payload: {@link Store}
     */
    public void updateStore(String storeId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, long version, boolean force, boolean forceGenerateNewKey) throws PrivmxException, NativeException, IllegalStateException {
        this.updateStore(storeId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
    }

    /**
     * Updates an existing Store.
     *
     * @param storeId             ID of the Store to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the updated Store
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to the
     *                            updated Store
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated Store
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Store
     * @param policies            additional container access policies
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeUpdated
     * channel: store
     * payload: {@link Store}
     */
    public native void updateStore(String storeId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, long version, boolean force, boolean forceGenerateNewKey, ContainerPolicy policies) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a single Store by given Store ID.
     *
     * @param storeId ID of the Store to get
     * @return Information about the Store
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Store getStore(String storeId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of Stores in given Context.
     *
     * @param contextId ID of the Context to get the Stores from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of Stores
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Store> listStores(String contextId, long skip, long limit, String sortOrder) throws PrivmxException, NativeException, IllegalStateException {
        return listStores(contextId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of Stores in given Context.
     *
     * @param contextId ID of the Context to get the Stores from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of Stores
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Store> listStores(String contextId, long skip, long limit, String sortOrder, String lastId) throws PrivmxException, NativeException, IllegalStateException {
        return listStores(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of Stores in given Context.
     *
     * @param contextId   ID of the Context to get the Stores from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of Stores
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Store> listStores(String contextId, long skip, long limit, String sortOrder, String lastId, String queryAsJson) throws PrivmxException, NativeException, IllegalStateException {
        return listStores(contextId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of Stores in given Context.
     *
     * @param contextId   ID of the Context to get the Stores from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field name to sort elements by
     * @return list of Stores
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native PagingList<Store> listStores(String contextId, long skip, long limit, String sortOrder, String lastId, String queryAsJson, String sortBy) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes a Store by given Store ID.
     *
     * @param storeId ID of the Store to delete
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeDeleted
     * channel: store
     * payload: {@link StoreDeletedEventData}
     */
    public native void deleteStore(String storeId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Creates a new file in a Store.
     *
     * @param storeId     ID of the Store to create the file in
     * @param publicMeta  public file metadata
     * @param privateMeta private file metadata
     * @param size        size of the file
     * @return Handle to write data
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Long createFile(String storeId, byte[] publicMeta, byte[] privateMeta, long size) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates an existing file in a Store.
     *
     * @param fileId      ID of the file to update
     * @param publicMeta  public file metadata
     * @param privateMeta private file metadata
     * @param size        size of the file
     * @return Handle to write file data
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Long updateFile(String fileId, byte[] publicMeta, byte[] privateMeta, long size) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates metadata of an existing file in a Store.
     *
     * @param fileId      ID of the file to update
     * @param publicMeta  public file metadata
     * @param privateMeta private file metadata
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeFileUpdated
     * channel: store/&lt;storeId&gt;/files
     * payload: {@link File}
     */
    public native void updateFileMeta(String fileId, byte[] publicMeta, byte[] privateMeta) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Writes a file data.
     *
     * @param fileHandle handle to write file data
     * @param dataChunk  file data chunk
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void writeToFile(long fileHandle, byte[] dataChunk) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes a file by given ID.
     *
     * @param fileId ID of the file to delete
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeFileDeleted
     * channel: store/&lt;storeId&gt;/files
     * payload: {@link StoreFileDeletedEventData}
     */
    public native void deleteFile(String fileId) throws PrivmxException, NativeException, IllegalStateException;


    /**
     * Gets a single file by the given file ID.
     *
     * @param fileId ID of the file to get
     * @return Information about the file
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native File getFile(String fileId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of files in given Store.
     *
     * @param storeId   ID of the Store to get files from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of files
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<File> listFiles(String storeId, long skip, long limit, String sortOrder) throws PrivmxException, NativeException, IllegalStateException {
        return listFiles(storeId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of files in given Store.
     *
     * @param storeId   ID of the Store to get files from
     * @param skip      number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of files
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<File> listFiles(String storeId, long skip, long limit, String sortOrder, String lastId) throws PrivmxException, NativeException, IllegalStateException {
        return listFiles(storeId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of files in given Store.
     *
     * @param storeId     ID of the Store to get files from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of files
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<File> listFiles(String storeId, long skip, long limit, String sortOrder, String lastId, String queryAsJson) throws PrivmxException, NativeException, IllegalStateException {
        return listFiles(storeId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of files in given Store.
     *
     * @param storeId     ID of the Store to get files from
     * @param skip        number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field name to sort elements by
     * @return list of files
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native PagingList<File> listFiles(String storeId, long skip, long limit, String sortOrder, String lastId, String queryAsJson, String sortBy) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Opens a file to read.
     *
     * @param fileId ID of the file to read
     * @return Handle to read file data
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Long openFile(String fileId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Reads file data.
     *
     * @param fileHandle handle to read file data
     * @param length     size of data to read
     * @return File data chunk
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native byte[] readFromFile(long fileHandle, long length) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Moves read cursor.
     *
     * @param fileHandle handle to read/write file data
     * @param position   new cursor position
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void seekInFile(long fileHandle, long position) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Closes the file handle.
     *
     * @param fileHandle handle to read/write file data
     * @return ID of closed file
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: storeStatsChanged
     * channel: store
     * payload: {@link StoreStatsChangedEventData}
     * @event type: storeFileCreated
     * channel: store/&lt;storeId&gt;/files
     * payload: {@link File}
     * @event type: storeFileUpdated
     * channel: store/&lt;storeId&gt;/files
     * payload: {@link File}
     */
    public native String closeFile(long fileHandle) throws PrivmxException, NativeException, IllegalStateException;


    /**
     * Subscribes for the Store module main events.
     *
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void subscribeForStoreEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribes from the Store module main events.
     *
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFromStoreEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for events in given Store.
     *
     * @param storeId ID of the Store to subscribe
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void subscribeForFileEvents(String storeId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribes from events in given Store.
     *
     * @param storeId ID of the {@code Store} to unsubscribe
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFromFileEvents(String storeId) throws PrivmxException, NativeException, IllegalStateException;

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