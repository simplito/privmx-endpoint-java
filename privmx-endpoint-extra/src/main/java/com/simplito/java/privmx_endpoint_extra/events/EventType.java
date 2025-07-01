//
// PrivMX Endpoint Java Extra.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.events;

import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.Inbox;
import com.simplito.java.privmx_endpoint.model.InboxEntry;
import com.simplito.java.privmx_endpoint.model.Kvdb;
import com.simplito.java.privmx_endpoint.model.KvdbEntry;
import com.simplito.java.privmx_endpoint.model.Message;
import com.simplito.java.privmx_endpoint.model.Store;
import com.simplito.java.privmx_endpoint.model.Thread;
import com.simplito.java.privmx_endpoint.model.events.InboxDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.InboxEntryDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.KvdbDeletedEntryEventData;
import com.simplito.java.privmx_endpoint.model.events.KvdbDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.KvdbStatsEventData;
import com.simplito.java.privmx_endpoint.model.events.StoreDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.StoreFileDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.StoreStatsChangedEventData;
import com.simplito.java.privmx_endpoint.model.events.ThreadDeletedEventData;
import com.simplito.java.privmx_endpoint.model.events.ThreadDeletedMessageEventData;
import com.simplito.java.privmx_endpoint.model.events.ThreadStatsEventData;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint;

/**
 * Defines the structure to register PrivMX Bridge event callbacks using {@link PrivmxEndpoint#registerCallback(Object, EventType, EventCallback)}.
 *
 * @param <T> the type of data contained in the Event.
 * @category core
 */
public class EventType<T> {
    /**
     * Channel of this event type.
     */
    public final String channel;

    /**
     * This event type as a string.
     */
    public final String eventType;

    /**
     * Type of event data.
     */
    public final Class<T> eventResultClass;

    private EventType(String channel, String eventType, Class<T> eventClass) {
        this.channel = channel;
        this.eventType = eventType;
        eventResultClass = eventClass;
    }

    /**
     * Predefined event type that captures successful platform connection events.
     */
    public static final EventType<Void> ConnectedEvent = new EventType<>(
            "",
            "libConnected",
            Void.class
    );

    /**
     * Predefined event type to catch special events.
     * This type could be used to emit/handle events with custom implementations (e.g. to break event loops).
     */
    public static final EventType<Void> LibBreakEvent = new EventType<>(
            "",
            "libBreak",
            Void.class
    );

    /**
     * Predefined event type to catch disconnection events.
     */
    public static final EventType<Void> DisconnectedEvent = new EventType<>(
            "",
            "libDisconnected",
            Void.class
    );

    /**
     * Predefined event type to catch created Thread events.
     */
    public static final EventType<Thread> ThreadCreatedEvent = new EventType<>(
            "thread",
            "threadCreated",
            Thread.class
    );

    /**
     * Predefined event type to catch updated Thread events.
     */
    public static final EventType<Thread> ThreadUpdatedEvent = new EventType<>(
            "thread",
            "threadUpdated",
            Thread.class
    );

    /**
     * Predefined event type to catch updated Thread stats events.
     */
    public static final EventType<ThreadStatsEventData> ThreadStatsChangedEvent = new EventType<>(
            "thread",
            "threadStats",
            ThreadStatsEventData.class
    );

    /**
     * Predefined event type to catch deleted Thread events.
     */
    public static final EventType<ThreadDeletedEventData> ThreadDeletedEvent = new EventType<>(
            "thread",
            "threadDeleted",
            ThreadDeletedEventData.class
    );
    /**
     * Predefined event type to catch created Store events.
     */
    public static final EventType<Store> StoreCreatedEvent = new EventType<>(
            "store",
            "storeCreated",
            Store.class
    );
    /**
     * Predefined event type to catch updated Store events.
     */
    public static final EventType<Store> StoreUpdatedEvent = new EventType<>(
            "store",
            "storeUpdated",
            Store.class
    );
    /**
     * Predefined event type to catch updated Store stats events.
     */
    public static final EventType<StoreStatsChangedEventData> StoreStatsChangedEvent = new EventType<>(
            "store",
            "storeStatsChanged",
            StoreStatsChangedEventData.class
    );
    /**
     * Predefined event type to catch deleted Store stats events.
     */
    public static final EventType<StoreDeletedEventData> StoreDeletedEvent = new EventType<>(
            "store",
            "storeDeleted",
            StoreDeletedEventData.class
    );

    /**
     * Returns instance to register on new message Events.
     *
     * @param threadId ID of the Thread to observe
     * @return Predefined event type to catch new messages in matching Thread events
     */
    public static EventType<Message> ThreadNewMessageEvent(String threadId) throws NullPointerException {
        if (threadId == null) throw new NullPointerException("Thread id cannot be null");
        return new EventType<>(
                "thread/" + threadId + "/messages",
                "threadNewMessage",
                Message.class
        );
    }

    /**
     * Returns instance to register on message update Events.
     *
     * @param threadId ID of the Thread to observe
     * @return predefined event type to catch message updates in matching Thread events
     */
    public static EventType<Message> ThreadMessageUpdatedEvent(String threadId) throws NullPointerException {
        if (threadId == null) throw new NullPointerException("Thread id cannot be null");
        return new EventType<>(
                "thread/" + threadId + "/messages",
                "threadUpdatedMessage",
                Message.class
        );
    }

    /**
     * Returns instance to register on deleted message Events.
     *
     * @param threadId ID of the Thread to observe
     * @return Predefined event type to catch deleted messages in matching Thread events
     */
    public static EventType<ThreadDeletedMessageEventData> ThreadMessageDeletedEvent(String threadId) throws NullPointerException {
        if (threadId == null) throw new NullPointerException("Thread id cannot be null");
        return new EventType<>(
                "thread/" + threadId + "/messages",
                "threadMessageDeleted",
                ThreadDeletedMessageEventData.class
        );
    }

    /**
     * Returns instance to register on created file Events.
     *
     * @param storeId ID of the store to observe
     * @return Predefined event type to catch new files in matching Store events
     */
    public static EventType<File> StoreFileCreatedEvent(String storeId) throws NullPointerException {
        if (storeId == null) throw new NullPointerException("Store id cannot be null");
        return new EventType<>(
                "store/" + storeId + "/files",
                "storeFileCreated",
                File.class
        );
    }

    /**
     * Returns instance to register on file update Events.
     *
     * @param storeId ID of the Store to observe
     * @return Predefined event type to catch updated files in matching Store events
     */
    public static EventType<File> StoreFileUpdatedEvent(String storeId) throws NullPointerException {
        if (storeId == null) throw new NullPointerException("Store id cannot be null");
        return new EventType<>(
                "store/" + storeId + "/files",
                "storeFileUpdated",
                File.class
        );
    }

    /**
     * Returns instance to register on deleted file Events.
     *
     * @param storeId ID of the Store to observe
     * @return Predefined event type to catch deleted files in matching Store events
     */
    public static EventType<StoreFileDeletedEventData> StoreFileDeletedEvent(String storeId) throws NullPointerException {
        if (storeId == null) throw new NullPointerException("Store id cannot be null");
        return new EventType<>(
                "store/" + storeId + "/files",
                "storeFileDeleted",
                StoreFileDeletedEventData.class
        );
    }

    /**
     * Predefined event type to catch created Inbox events.
     */
    public static EventType<Inbox> InboxCreatedEvent = new EventType<>(
            "inbox",
            "inboxCreated",
            Inbox.class
    );

    /**
     * Predefined event type to catch update Inbox events.
     */
    public static EventType<Inbox> InboxUpdatedEvent = new EventType<>(
            "inbox",
            "inboxUpdated",
            Inbox.class
    );

    /**
     * Predefined event type to catch deleted Inbox events.
     */
    public static EventType<InboxDeletedEventData> InboxDeletedEvent = new EventType<>(
            "inbox",
            "inboxDeleted",
            InboxDeletedEventData.class
    );

    /**
     * Returns instance to register on created entry Events.
     *
     * @param inboxId ID of the Inbox to observe
     * @return predefined event type to catch created entries in matching Inbox events
     */
    public static EventType<InboxEntry> InboxEntryCreatedEvent(String inboxId) throws NullPointerException {
        if (inboxId == null) throw new NullPointerException("Inbox id cannot be null");
        return new EventType<>(
                "inbox/" + inboxId + "/entries",
                "inboxEntryCreated",
                InboxEntry.class
        );
    }

    /**
     * Returns instance to register on deleting entries Events.
     *
     * @param inboxId ID of the Inbox to observe
     * @return predefined event type to catch deleted entries in matching Inbox events
     */
    public static EventType<InboxEntryDeletedEventData> InboxEntryDeletedEvent(String inboxId) throws NullPointerException {
        if (inboxId == null) throw new NullPointerException("Inbox id cannot be null");
        return new EventType<>(
                "inbox/" + inboxId + "/entries",
                "inboxEntryDeleted",
                InboxEntryDeletedEventData.class
        );
    }

    /**
     * Predefined event type to catch updated Kvdb events.
     */
    public static final EventType<Kvdb> KvdbUpdatedEvent = new EventType<>(
            "kvdb",
            "kvdbUpdated",
            Kvdb.class
    );

    /**
     * Predefined event type to catch deleted Kvdb events.
     */
    public static final EventType<KvdbDeletedEventData> KvdbDeletedEvent = new EventType<>(
            "kvdb",
            "kvdbDeleted",
            KvdbDeletedEventData.class
    );

    /**
     * Predefined event type to catch updated Kvdb stats events.
     */
    public static final EventType<KvdbStatsEventData> KvdbStatsEvent = new EventType<>(
            "kvdb",
            "kvdbStatsChanged",
            KvdbStatsEventData.class
    );

    /**
     * Predefined event type to catch created Kvdb events.
     */
    public static EventType<Kvdb> KvdbCreatedEvent = new EventType<>(
            "kvdb",
            "kvdbCreated",
            Kvdb.class
    );

    /**
     * Predefined event type to catch created KvdbEntry events.
     *
     * @param kvdbId ID of the Kvdb to observe
     */
    public static EventType<KvdbEntry> kvdbNewEntry(String kvdbId) throws NullPointerException {
        if (kvdbId == null) throw new NullPointerException("Kvdb id cannot be null");
        return new EventType<>(
                "kvdb/" + kvdbId + "/entries",
                "kvdbNewEntry",
                KvdbEntry.class
        );
    }

    /**
     * Predefined event type to catch updated KvdbEntry events.
     *
     * @param kvdbId ID of the Kvdb to observe
     */
    public static EventType<KvdbEntry> KvdbEntryUpdatedEvent(String kvdbId) throws NullPointerException {
        if (kvdbId == null) throw new NullPointerException("Kvdb id cannot be null");
        return new EventType<>(
                "kvdb/" + kvdbId + "/entries",
                "kvdbEntryUpdated",
                KvdbEntry.class
        );
    }

    /**
     * Predefined event type to catch deleted KvdbEntry events.
     *
     * @param kvdbId ID of the Kvdb to observe
     */
    public static EventType<KvdbDeletedEntryEventData> KvdbEntryDeletedEvent(String kvdbId) throws NullPointerException {
        if (kvdbId == null) throw new NullPointerException("Kvdb id cannot be null");
        return new EventType<>(
                "kvdb/" + kvdbId + "/entries",
                "kvdbEntryDeleted",
                KvdbDeletedEntryEventData.class
        );
    }
}
