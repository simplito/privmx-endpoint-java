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

package com.simplito.java.privmx_endpoint.modules.thread;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.Message;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.Thread;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.events.ThreadDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.ThreadDeletedMessageEventData;
import com.simplito.java.privmx_endpoint.model.events.ThreadStatsEventData;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;
import com.simplito.java.privmx_endpoint.modules.core.Connection;

import java.util.List;
import java.util.Objects;

/**
 * Manages Threads and messages.
 *
 * @category thread
 */
public class ThreadApi implements AutoCloseable {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    /**
     * Initialize Thread module on given active connection.
     *
     * @param connection active connection to PrivMX Bridge
     * @throws IllegalStateException when given {@link Connection} is not connected
     */
    public ThreadApi(Connection connection) throws IllegalStateException {
        Objects.requireNonNull(connection);
        this.api = init(connection);
    }

    private native Long init(Connection connection) throws IllegalStateException;

    private native void deinit() throws IllegalStateException;

    /**
     * Creates a new Thread in given Context.
     *
     * @param contextId   ID of the Context to create the Thread in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created Thread
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to
     *                    the created Thread
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @return ID of the created Thread
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadCreated
     * channel: thread
     * payload: {@link Thread}
     */
    public String createThread(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta) throws PrivmxException, NativeException, IllegalStateException {
        return createThread(contextId, users, managers, publicMeta, privateMeta, null);
    }

    /**
     * Creates a new Thread in given Context.
     *
     * @param contextId   ID of the Context to create the Thread in
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the created Thread
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to
     *                    the created Thread
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param policies    additional container access policies
     * @return ID of the created Thread
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadCreated
     * channel: thread
     * payload: {@link Thread}
     */
    public native String createThread(String contextId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, ContainerPolicy policies) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates an existing Thread.
     *
     * @param threadId    ID of the Thread to update
     * @param users       list of {@link UserWithPubKey} which indicates who will have access to the updated Thread
     * @param managers    list of {@link UserWithPubKey} which indicates who will have access (and management rights) to
     *                    the updated Thread
     * @param publicMeta  public (unencrypted) metadata
     * @param privateMeta private (encrypted) metadata
     * @param version     current version of the updated Thread
     * @param force       force update (without checking version)
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadUpdated
     * channel: thread
     * payload: {@link Thread}
     */
    public void updateThread(String threadId, List<UserWithPubKey> users, List<UserWithPubKey> managers, byte[] publicMeta, byte[] privateMeta, long version, boolean force) throws PrivmxException, NativeException, IllegalStateException {
        updateThread(threadId, users, managers, publicMeta, privateMeta, version, force, false);
    }

    /**
     * Updates an existing Thread.
     *
     * @param threadId            ID of the Thread to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the updated Thread
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to
     *                            the updated Thread
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated Thread
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Thread
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadUpdated
     * channel: thread
     * payload: {@link Thread}
     */
    public void updateThread(
            String threadId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey
    ) throws PrivmxException, NativeException, IllegalStateException {
        updateThread(threadId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, null);
    }

    /**
     * Updates an existing Thread.
     *
     * @param threadId            ID of the Thread to update
     * @param users               list of {@link UserWithPubKey} which indicates who will have access to the updated Thread
     * @param managers            list of {@link UserWithPubKey} which indicates who will have access (and management rights) to
     *                            the updated Thread
     * @param publicMeta          public (unencrypted) metadata
     * @param privateMeta         private (encrypted) metadata
     * @param version             current version of the updated Thread
     * @param force               force update (without checking version)
     * @param forceGenerateNewKey force to regenerate a key for the Thread
     * @param policies            additional container access policies
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadUpdated
     * channel: thread
     * payload: {@link Thread}
     */
    public native void updateThread(
            String threadId,
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
     * Gets a Thread by given Thread ID.
     *
     * @param threadId ID of Thread to get
     * @return Information about the Thread
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Thread getThread(String threadId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of Threads in given Context.
     *
     * @param contextId ID of the Context to get the Threads from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of Threads
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception
     * @throws NativeException       thrown when method encounters an unknown exception
     */
    public PagingList<Thread> listThreads(String contextId, long skip, long limit, String sortOrder) throws PrivmxException, NativeException, IllegalStateException {
        return listThreads(contextId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of Threads in given Context.
     *
     * @param contextId ID of the Context to get the Threads from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of Threads
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception
     * @throws NativeException       thrown when method encounters an unknown exception
     */
    public PagingList<Thread> listThreads(String contextId, long skip, long limit, String sortOrder, String lastId) throws PrivmxException, NativeException, IllegalStateException {
        return listThreads(contextId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of Threads in given Context.
     *
     * @param contextId   ID of the Context to get the Threads from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of Threads
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception
     * @throws NativeException       thrown when method encounters an unknown exception
     */
    public PagingList<Thread> listThreads(String contextId, long skip, long limit, String sortOrder, String lastId, String queryAsJson) throws PrivmxException, NativeException, IllegalStateException {
        return listThreads(contextId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of Threads in given Context.
     *
     * @param contextId   ID of the Context to get the Threads from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field by elements are sorted in result
     * @return list of Threads
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception
     * @throws NativeException       thrown when method encounters an unknown exception
     */
    public native PagingList<Thread> listThreads(String contextId, long skip, long limit, String sortOrder, String lastId, String queryAsJson, String sortBy) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Deletes a Thread by given Thread ID.
     *
     * @param threadId ID of the Thread to delete
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadDeleted
     * channel: thread
     * payload: {@link ThreadDeletedEventData}
     */
    public native void deleteThread(String threadId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Sends a message in a Thread.
     *
     * @param threadId    ID of the Thread to send message to
     * @param publicMeta  public message metadata
     * @param privateMeta private message metadata
     * @param data        content of the message
     * @return ID of the new message
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadNewMessage
     * channel: thread/&lt;threadId&gt;/messages
     * payload: {@link Message}
     * @event type: threadStats
     * channel: thread
     * payload: {@link ThreadStatsEventData}
     */
    public native String sendMessage(String threadId, byte[] publicMeta, byte[] privateMeta, byte[] data) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a message by given message ID.
     *
     * @param messageId ID of the message to get
     * @return Message with matching id
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native Message getMessage(String messageId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Gets a list of messages from a Thread.
     *
     * @param threadId  ID of the Thread to list messages from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @return list of messages
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Message> listMessages(String threadId, long skip, long limit, String sortOrder) throws PrivmxException, NativeException, IllegalStateException {
        return listMessages(threadId, skip, limit, sortOrder, null, null, null);
    }

    /**
     * Gets a list of messages from a Thread.
     *
     * @param threadId  ID of the Thread to list messages from
     * @param skip      skip number of elements to skip from result
     * @param limit     limit of elements to return for query
     * @param sortOrder order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId    ID of the element from which query results should start
     * @return list of messages
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Message> listMessages(String threadId, long skip, long limit, String sortOrder, String lastId) throws PrivmxException, NativeException, IllegalStateException {
        return listMessages(threadId, skip, limit, sortOrder, lastId, null, null);
    }

    /**
     * Gets a list of messages from a Thread.
     *
     * @param threadId    ID of the Thread to list messages from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @return list of messages
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public PagingList<Message> listMessages(String threadId, long skip, long limit, String sortOrder, String lastId, String queryAsJson) throws PrivmxException, NativeException, IllegalStateException {
        return listMessages(threadId, skip, limit, sortOrder, lastId, queryAsJson, null);
    }

    /**
     * Gets a list of messages from a Thread.
     *
     * @param threadId    ID of the Thread to list messages from
     * @param skip        skip number of elements to skip from result
     * @param limit       limit of elements to return for query
     * @param sortOrder   order of elements in result ("asc" for ascending, "desc" for descending)
     * @param lastId      ID of the element from which query results should start
     * @param queryAsJson stringified JSON object with a custom field to filter result
     * @param sortBy      field by elements are sorted in result
     * @return list of messages
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native PagingList<Message> listMessages(String threadId, long skip, long limit, String sortOrder, String lastId, String queryAsJson, String sortBy) throws PrivmxException, NativeException, IllegalStateException;


    /**
     * Deletes a message by given message ID.
     *
     * @param messageId ID of the message to delete
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadMessageDeleted
     * channel: thread/&lt;threadId&gt;/messages
     * payload: {@link ThreadDeletedMessageEventData}
     */
    public native void deleteMessage(String messageId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Updates message in a Thread.
     *
     * @param messageId   ID of the message to update
     * @param publicMeta  public message metadata
     * @param privateMeta private message metadata
     * @param data        new content of the message
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     * @event type: threadUpdatedMessage
     * channel: thread/&lt;threadId&gt;/messages
     * payload: {@link Message}
     */
    public native void updateMessage(String messageId, byte[] publicMeta, byte[] privateMeta, byte[] data) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for the Thread module main events.
     *
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void subscribeForThreadEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribes from the Thread module main events.
     *
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFromThreadEvents() throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Subscribes for events in given Thread.
     *
     * @param threadId ID of the Thread to subscribe
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void subscribeForMessageEvents(String threadId) throws PrivmxException, NativeException, IllegalStateException;

    /**
     * Unsubscribes from events in given Thread.
     *
     * @param threadId ID of the Thread to unsubscribe
     * @throws IllegalStateException thrown when instance is closed.
     * @throws PrivmxException       thrown when method encounters an exception.
     * @throws NativeException       thrown when method encounters an unknown exception.
     */
    public native void unsubscribeFromMessageEvents(String threadId) throws PrivmxException, NativeException, IllegalStateException;

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