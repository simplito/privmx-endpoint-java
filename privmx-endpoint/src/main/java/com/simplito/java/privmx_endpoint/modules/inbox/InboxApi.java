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

package com.simplito.java.privmx_endpoint.modules.inbox;

import com.simplito.java.privmx_endpoint.model.ContainerPolicyWithoutItem;
import com.simplito.java.privmx_endpoint.model.FilesConfig;
import com.simplito.java.privmx_endpoint.model.Inbox;
import com.simplito.java.privmx_endpoint.model.InboxEntry;
import com.simplito.java.privmx_endpoint.model.InboxPublicView;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.InboxDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.InboxEntryDeletedEventData;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;
import com.simplito.java.privmx_endpoint.modules.store.StoreApi;
import com.simplito.java.privmx_endpoint.modules.thread.ThreadApi;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Manages PrivMX Bridge Inboxes and Entries.
 *
 * @category inbox
 */
public class InboxApi implements AutoCloseable {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    /**
     * Creates Inbox from existing {@link Connection}.
     *
     * @param connection current connection
     * @throws IllegalStateException when one of the passed parameters is closed.
     */
    public InboxApi(
            Connection connection
    ) throws IllegalStateException {
        this(connection, null, null);
    }


    /**
     * Creates Inbox from existing {@link Connection}, {@link ThreadApi}, {@link StoreApi}.
     *
     * @param connection active connection to PrivMX Bridge
     * @param threadApi  instance of {@link ThreadApi} created on passed Connection
     * @param storeApi   instance of {@link StoreApi} created on passed Connection
     * @throws IllegalStateException when one of the passed parameters is closed.
     */
    public InboxApi(
            Connection connection,
            ThreadApi threadApi,
            StoreApi storeApi
    ) throws IllegalStateException {
        Objects.requireNonNull(connection);
        StoreApi tmpStoreApi = storeApi == null ? new StoreApi(connection) : null;
        ThreadApi tmpThreadApi = threadApi == null ? new ThreadApi(connection) : null;
        this.api = init(
                connection,
                Optional.ofNullable(threadApi).orElse(tmpThreadApi),
                Optional.ofNullable(storeApi).orElse(tmpStoreApi)
        );
        try {
            if (tmpThreadApi != null) tmpThreadApi.close();
            if (tmpStoreApi != null) tmpStoreApi.close();
        } catch (Exception ignore) {
        }
    }

    private native Long init(
            Connection connection,
            ThreadApi threadApi,
            StoreApi storeApi
    ) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;


    /**
     * Creates a new Inbox.
     *
     * @param contextId   ID of the Context of the new Inbox
     * @param users       list of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers    list of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @return ID of the created Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public String createInbox(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta
    ) throws PrivmxException, NativeException, IllegalStateException {
        return createInbox(contextId, users, managers, publicMeta, privateMeta, null);
    }

    /**
     * Creates a new Inbox.
     *
     * @param contextId   ID of the Context of the new Inbox
     * @param users       list of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers    list of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param filesConfig overrides default file configuration
     * @return ID of the created Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public String createInbox(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig
    ) throws PrivmxException, NativeException, IllegalStateException {
        return createInbox(contextId, users, managers, publicMeta, privateMeta, filesConfig, null);
    }

    /**
     * Creates a new Inbox.
     *
     * @param contextId   ID of the Context of the new Inbox
     * @param users       list of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers    list of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param filesConfig overrides default file configuration
     * @param policies    additional container access policies
     * @return ID of the created Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public native String createInbox(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            ContainerPolicyWithoutItem policies
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates an existing Inbox.
     *
     * @param inboxId     ID of the Inbox to update
     * @param users       list of UserWithPubKey structs which indicates who will have access to the created Inbox
     * @param managers    list of UserWithPubKey structs which indicates who will have access (and have manage rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param filesConfig struct to override default files configuration
     * @param version     current version of the updated Inbox
     * @param force       force update (without checking version)
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxUpdated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public void updateInbox(
            String inboxId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            long version,
            boolean force
    ) throws PrivmxException, NativeException, IllegalStateException {
        updateInbox(inboxId, users, managers, publicMeta, privateMeta, filesConfig, version, force, false);
    }

    /**
     * Updates an existing Inbox.
     *
     * @param inboxId             ID of the Inbox to update
     * @param users               list of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers            list of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                            the created Inbox
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param filesConfig         overrides default file configuration
     * @param version             current version of the updated Inbox
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxUpdated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public void updateInbox(
            String inboxId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            long version,
            boolean force,
            boolean forceGenerateNewKey
    ) throws PrivmxException, NativeException, IllegalStateException {
        this.updateInbox(inboxId, users, managers, publicMeta, privateMeta, filesConfig, version, force, forceGenerateNewKey, null);
    }

    /**
     * Updates an existing Inbox.
     *
     * @param inboxId             ID of the Inbox to update
     * @param users               list of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers            list of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                            the created Inbox
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param filesConfig         overrides default file configuration
     * @param version             current version of the updated Inbox
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Inbox
     * @param policies            additional container access policies
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxUpdated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public native void updateInbox(
            String inboxId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            FilesConfig filesConfig,
            long version,
            boolean force,
            boolean forceGenerateNewKey,
            ContainerPolicyWithoutItem policies
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a single Inbox by given Inbox ID.
     *
     * @param inboxId ID of the Inbox to get
     * @return Information about the Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Inbox getInbox(String inboxId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of Inboxes in given Context.
     *
     * @param contextId ID of the Context to get Inboxes from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of Inboxes
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Inbox> listInboxes(
            String contextId,
            long skip,
            long limit,
            String sortOrder
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listInboxes(contextId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of Inboxes in given Context.
     *
     * @param contextId ID of the Context to get Inboxes from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of Inboxes
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Inbox> listInboxes(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listInboxes(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of Inboxes in given Context.
     *
     * @param contextId   ID of the Context to get Inboxes from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of Inboxes
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<Inbox> listInboxes(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listInboxes(contextId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets s list of Inboxes in given Context.
     *
     * @param contextId   ID of the Context to get Inboxes from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field name to sort elements by
     * @return list of Inboxes
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native PagingList<Inbox> listInboxes(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets public data of given Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId ID of the Inbox to get
     * @return Public accessible information about the Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native InboxPublicView getInboxPublicView(String inboxId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes an Inbox by given Inbox ID.
     *
     * @param inboxId ID of the Inbox to delete
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxDeleted
     * channel: inbox
     * payload: {@link InboxDeletedEventData}
     */
    public native void deleteInbox(String inboxId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Prepares a request to send data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId ID of the Inbox to which the request applies
     * @param data    entry data to send
     * @return Inbox handle
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public Long prepareEntry(String inboxId, byte[] data) throws PrivmxException, NativeException, IllegalStateException {
        return prepareEntry(inboxId, data, Collections.emptyList(), null);
    }

    /**
     * Prepares a request to send data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId          ID of the Inbox to which the request applies
     * @param data             entry data to send
     * @param inboxFileHandles optional list of file handles that will be sent with the request
     * @return Inbox handle
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public Long prepareEntry(
            String inboxId,
            byte[] data,
            List<Long> inboxFileHandles
    ) throws PrivmxException, NativeException, IllegalStateException {
        return prepareEntry(inboxId, data, inboxFileHandles, null);
    }

    /**
     * Prepares a request to send data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId          ID of the Inbox to which the request applies
     * @param data             entry data to send
     * @param inboxFileHandles optional list of file handles that will be sent with the request
     * @param userPrivKey      sender can optionally provide a private key, which will be used: <br>
     *                         1) to sign the sent data, <br>
     *                         2) to derivation of the public key, which will then be transferred
     *                         along with the sent data and can be used in the future for further
     *                         secure communication with the sender
     * @return Inbox handle
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Long prepareEntry(
            String inboxId,
            byte[] data,
            List<Long> inboxFileHandles,
            String userPrivKey
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Sends data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxHandle ID of the Inbox to which the request applies
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxEntryCreated
     * channel: inbox/&lt;inboxId&gt;/entries
     * payload: {@link InboxEntry}
     */
    public native void sendEntry(long inboxHandle) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets an entry from an Inbox.
     *
     * @param inboxEntryId ID of an entry to read from the Inbox
     * @return Data of the selected entry stored in the Inbox
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native InboxEntry readEntry(String inboxEntryId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets list of entries in given Inbox.
     *
     * @param inboxId   ID of the Inbox
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<InboxEntry> listEntries(
            String inboxId,
            long skip,
            long limit,
            String sortOrder
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(inboxId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets list of entries of given Inbox.
     *
     * @param inboxId   ID of the Inbox
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<InboxEntry> listEntries(
            String inboxId,
            long skip,
            long limit,
            String sortOrder,
            String lastId
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(inboxId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets list of entries of given Inbox.
     *
     * @param inboxId     ID of the Inbox
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public PagingList<InboxEntry> listEntries(
            String inboxId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson
    ) throws PrivmxException, NativeException, IllegalStateException {
        return listEntries(inboxId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }


    /**
     * Gets list of entries of given Inbox.
     *
     * @param inboxId     ID of the Inbox
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field name to sort elements by
     * @return list of entries
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native PagingList<InboxEntry> listEntries(
            String inboxId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String queryAsJson,
            String sortBy
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes an entry from an Inbox.
     *
     * @param inboxEntryId ID of an entry to delete
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     * @event type: inboxEntryDeleted
     * channel: inbox/&lt;inboxId&gt;/entries
     * payload: {@link InboxEntryDeletedEventData}
     */
    public native void deleteEntry(String inboxEntryId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Creates a file handle to send a file to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param publicMeta  public file's metadata
     * @param privateMeta private file's metadata
     * @param fileSize    size of the file to send
     * @return File handle
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Long /*inboxFileHandle*/ createFileHandle(
            byte[] publicMeta,
            byte[] privateMeta,
            long fileSize
    ) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Sends a file's data chunk to an Inbox.
     * To send the entire file - divide it into pieces of the desired size and call the function for each fragment.
     * You do not have to be logged in to call this function.
     *
     * @param inboxHandle     handle to the prepared Inbox entry
     * @param inboxFileHandle handle to the file where the uploaded chunk belongs
     * @param dataChunk       file chunk to send
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void writeToFile(
            long inboxHandle,
            long inboxFileHandle,
            byte[] dataChunk
    ) throws PrivmxException, NativeException, IllegalStateException;


    /**
     * Opens a file to read.
     *
     * @param fileId ID of the file to read
     * @return Handle to read file data
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native Long openFile(String fileId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Reads file data.
     *
     * @param fileHandle handle to the file
     * @param length     size of data to read
     * @return File data chunk
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native byte[] readFromFile(long fileHandle, long length) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Moves file's read cursor.
     *
     * @param fileHandle handle to the file
     * @param position   sets new cursor position
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void seekInFile(long fileHandle, long position) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Closes a file by given handle.
     *
     * @param fileHandle handle to the file
     * @return ID of closed file
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native String closeFile(long fileHandle) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for the Inbox module main events.
     *
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void subscribeForInboxEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for the Inbox module main events.
     *
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void unsubscribeFromInboxEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for events in given Inbox.
     *
     * @param inboxId ID of the Inbox to subscribe
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void subscribeForEntryEvents(String inboxId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribes from events in given Inbox.
     *
     * @param inboxId ID of the Inbox to unsubscribe
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @throws IllegalStateException thrown when instance is closed.
     */
    public native void unsubscribeFromEntryEvents(String inboxId) throws PrivmxException, NativeException, IllegalStateException;

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