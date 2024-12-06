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
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("privmx-endpoint-java");
    }

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
     * @param users       vector of {@link UserWithPubKey} structs which indicates who will have access to the created Inbox
     * @param managers    vector of {@link UserWithPubKey} structs which indicates who will have access (and management rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @return ID of the created Inbox
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public String createInbox(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta) {
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
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public String createInbox(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, FilesConfig filesConfig) {
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
     * @event type: inboxCreated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public native String createInbox(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, FilesConfig filesConfig, ContainerPolicyWithoutItem policies);

    /**
     * Updates an existing Inbox.
     *
     * @param inboxId     ID of the Inbox to update
     * @param users       vector of UserWithPubKey structs which indicates who will have access to the created Inbox
     * @param managers    vector of UserWithPubKey structs which indicates who will have access (and have manage rights) to
     *                    the created Inbox
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param filesConfig struct to override default files configuration
     * @param version     current version of the updated Inbox
     * @param force       force update (without checking version)
     * @event type: inboxUpdated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public void updateInbox(String inboxId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, FilesConfig filesConfig, long version, boolean force) {
        updateInbox(inboxId, users, managers, publicMeta, privateMeta, filesConfig, version, force, true);
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
     * @event type: inboxUpdated
     * channel: inbox
     * payload: {@link Inbox}
     */
    public void updateInbox(String inboxId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, FilesConfig filesConfig, long version, boolean force, boolean forceGenerateNewKey) {
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
    );

    /**
     * Gets a single Inbox by given Inbox ID.
     *
     * @param inboxId ID of the Inbox to get
     * @return Information about the Inbox
     */
    public native Inbox getInbox(String inboxId);

    /**
     * Gets a list of Inboxes in given Context.
     *
     * @param contextId ID of the Context to get Inboxes from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of Inboxes
     */
    public PagingList<Inbox> listInboxes(String contextId, long skip, long limit, String sortOrder) {
        return listInboxes(contextId, skip, limit, sortOrder, null);
    }

    /**
     * Gets s list of Inboxes in given Context.
     *
     * @param contextId ID of the Context to get Inboxes from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of Inboxes
     */
    public native PagingList<Inbox> listInboxes(String contextId, long skip, long limit, String sortOrder, String lastId);


    /**
     * Gets public data of given Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId ID of the Inbox to get
     * @return Public accessible information about the Inbox
     */
    public native InboxPublicView getInboxPublicView(String inboxId);

    /**
     * Deletes an Inbox by given Inbox ID.
     *
     * @param inboxId ID of the Inbox to delete
     * @event type: inboxDeleted
     * channel: inbox
     * payload: {@link InboxDeletedEventData}
     */
    public native void deleteInbox(String inboxId);

    /**
     * Prepares a request to send data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId ID of the Inbox to which the request applies
     * @param data    entry data to send
     * @return Inbox handle
     */
    public Long /*inboxHandle*/ prepareEntry(String inboxId, byte[] data) {
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
     */
    public Long /*inboxHandle*/ prepareEntry(String inboxId, byte[] data, List<Long> inboxFileHandles) {
        return prepareEntry(inboxId, data, inboxFileHandles, null);
    }

    /**
     * Prepares a request to send data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxId          ID of the Inbox to which the request applies
     * @param data             entry data to send
     * @param inboxFileHandles optional list of file handles that will be sent with the request
     * @param userPrivKey      optional sender's private key which can be used later to encrypt data for that sender
     * @return Inbox handle
     */
    public native Long /*inboxHandle*/ prepareEntry(String inboxId, byte[] data, List<Long> inboxFileHandles, String userPrivKey);

    /**
     * Sends data to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param inboxHandle ID of the Inbox to which the request applies
     * @event type: inboxEntryCreated
     * channel: inbox/&lt;inboxId&gt;/entries
     * payload: {@link InboxEntry}
     */
    public native void sendEntry(long inboxHandle);

    /**
     * Gets an entry from an Inbox.
     *
     * @param inboxEntryId ID of an entry to read from the Inbox
     * @return Data of the selected entry stored in the Inbox
     */
    public native InboxEntry readEntry(String inboxEntryId);

    /**
     * Gets list of entries in given Inbox.
     *
     * @param inboxId   ID of the Inbox
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of entries
     */
    public PagingList<InboxEntry> listEntries(String inboxId, long skip, long limit, String sortOrder) {
        return listEntries(inboxId, skip, limit, sortOrder, null);
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
     */
    public native PagingList<InboxEntry> listEntries(String inboxId, long skip, long limit, String sortOrder, String lastId);

    /**
     * Deletes an entry from an Inbox.
     *
     * @param inboxEntryId ID of an entry to delete
     * @event type: inboxEntryDeleted
     * channel: inbox/&lt;inboxId&gt;/entries
     * payload: {@link InboxEntryDeletedEventData}
     */
    public native void deleteEntry(String inboxEntryId);

    /**
     * Creates a file handle to send a file to an Inbox.
     * You do not have to be logged in to call this function.
     *
     * @param publicMeta  public file's metadata
     * @param privateMeta private file's metadata
     * @param fileSize    size of the file to send
     * @return File handle
     */
    public native Long /*inboxFileHandle*/ createFileHandle(byte[] publicMeta, byte[] privateMeta, long fileSize);

    /**
     * Sends a file's data chunk to an Inbox.
     * To send the entire file - divide it into pieces of the desired size and call the function for each fragment.
     * You do not have to be logged in to call this function.
     *
     * @param inboxHandle     ID of the Inbox to which the request applies
     * @param inboxFileHandle handle to the file where the uploaded chunk belongs
     * @param dataChunk       file chunk to send
     */
    public native void writeToFile(long inboxHandle, long inboxFileHandle, byte[] dataChunk);


    /**
     * Opens a file to read.
     *
     * @param fileId ID of the file to read
     * @return Handle to read file data
     */
    public native Long openFile(String fileId);

    /**
     * Reads file data.
     *
     * @param fileHandle handle to the file
     * @param length     size of data to read
     * @return File data chunk
     */
    public native byte[] readFromFile(long fileHandle, long length);

    /**
     * Moves file's read cursor.
     *
     * @param fileHandle handle to the file
     * @param position   sets new cursor position
     */
    public native void seekInFile(long fileHandle, long position);

    /**
     * Closes a file by given handle.
     *
     * @param fileHandle handle to the file
     * @return ID of closed file
     */
    public native String closeFile(long fileHandle);

    /**
     * Subscribes for the Inbox module main events.
     */
    public native void subscribeForInboxEvents();

    /**
     * Subscribes for the Inbox module main events.
     */
    public native void unsubscribeFromInboxEvents();

    /**
     * Subscribes for events in given Inbox.
     *
     * @param inboxId ID of the Inbox to subscribe
     */
    public native void subscribeForEntryEvents(String inboxId);

    /**
     * Unsubscribes from events in given Inbox.
     *
     * @param inboxId ID of the Inbox to unsubscribe
     */
    public native void unsubscribeFromEntryEvents(String inboxId);

    @Override
    public void close() throws Exception {
        deinit();
    }
}
