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

import com.simplito.java.privmx_endpoint.model.Event;
import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.Inbox;
import com.simplito.java.privmx_endpoint.model.InboxEntry;
import com.simplito.java.privmx_endpoint.model.Kvdb;
import com.simplito.java.privmx_endpoint.model.KvdbEntry;
import com.simplito.java.privmx_endpoint.model.Message;
import com.simplito.java.privmx_endpoint.model.Store;
import com.simplito.java.privmx_endpoint.model.Thread;
import com.simplito.java.privmx_endpoint.model.events.CollectionChangedEventData;
import com.simplito.java.privmx_endpoint.model.events.ContextCustomEventData;
import com.simplito.java.privmx_endpoint.model.events.ContextUserEventData;
import com.simplito.java.privmx_endpoint.model.events.ContextUsersStatusChangedEventData;
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
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.CustomEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.EventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.CoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;

import java.util.Objects;

/**
 * Defines the structure to register PrivMX Bridge event callbacks
 *
 * @param <T> the type of data contained in the Event.
 * @category core
 */
public class EventType<T> {
    //TODO: Add better descriptions for new eventType constructors, especially for containers where selector type is not passed as parameter
    /**
     * This event type as a string.
     */
    public final String eventName;
    public final String channelName;

    public final com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType libEventType;
    public final EventSelectorType eventSelectorType;
    public final String eventSelectorId;
    public final Class<T> eventResultClass;

    private EventType(String eventName, com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType libEventType, EventSelectorType eventSelectorType, String eventSelectorId, String channelName, Class<T> eventClass) {
        this.eventName = eventName;
        this.channelName = channelName;
        this.libEventType = libEventType;
        this.eventSelectorType = eventSelectorType;
        this.eventSelectorId = eventSelectorId;
        eventResultClass = eventClass;
    }

    private EventType(String eventName, com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType libEventType, EventSelectorType eventSelectorType, String eventSelectorId, Class<T> eventClass) {
        this.eventName = eventName;
        this.channelName = null;
        this.libEventType = libEventType;
        this.eventSelectorType = eventSelectorType;
        this.eventSelectorId = eventSelectorId;
        eventResultClass = eventClass;
    }

    private EventType(String eventName, Class<T> eventClass) {
        this.eventName = eventName;
        this.channelName = null;
        this.libEventType = null;
        this.eventSelectorType = null;
        this.eventSelectorId = null;
        eventResultClass = eventClass;
    }

    public boolean isLibEvent() {
        return this.equals(EventType.ConnectedEvent) || this.equals(DisconnectedEvent) || this.equals(LibBreakEvent);
    }

    public static boolean isLibEvent(Event<?> event) {
        return event.subscriptions != null
                && event.subscriptions.isEmpty()
                && Objects.equals(event.type, EventType.ConnectedEvent.eventName)
                && Objects.equals(event.type, DisconnectedEvent.eventName)
                && Objects.equals(event.type, LibBreakEvent.eventName);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof EventType)) return false;
        EventType<?> eventType1 = (EventType<?>) o;
        return Objects.equals(eventName, eventType1.eventName) &&
                Objects.equals(channelName, eventType1.channelName) &&
                Objects.equals(libEventType, eventType1.libEventType) &&
                Objects.equals(eventSelectorType, eventType1.eventSelectorType) &&
                Objects.equals(eventSelectorId, eventType1.eventSelectorId) &&
                Objects.equals(eventResultClass, eventType1.eventResultClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventName, channelName, libEventType, eventSelectorType, eventSelectorId, eventResultClass);
    }

    /**
     * Predefined event type that captures successful platform connection events.
     */
    public static final EventType<Void> ConnectedEvent = new EventType<>(
            "libConnected",
            Void.class
    );
    /**
     * Predefined event type to catch special events.
     * This type could be used to emit/handle events with custom implementations (e.g. to break event loops).
     */
    public static final EventType<Void> LibBreakEvent = new EventType<>(
            "libBreak",
            Void.class
    );
    /**
     * Predefined event type to catch disconnection events.
     */
    public static final EventType<Void> DisconnectedEvent = new EventType<>(
            "libDisconnected",
            Void.class
    );

    /**
     * Predefined event type to catch created Thread events on specified {@code contextId}
     */
    public static EventType<Thread> ThreadCreatedEvent(String contextId) {
        return new EventType<>(
                "threadCreated",
                ThreadEventType.THREAD_CREATE,
                ThreadEventSelectorType.CONTEXT_ID,
                contextId,
                Thread.class);
    }

    /**
     * Predefined event type to catch updated Thread events.
     */
    public static EventType<Thread> ThreadUpdatedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadUpdated",
                ThreadEventType.THREAD_UPDATE,
                selectorType,
                selectorId,
                Thread.class);
    }

    /**
     * Predefined event type to catch updated Thread stats events.
     */
    public static EventType<ThreadStatsEventData> ThreadStatsChangedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadStats",
                ThreadEventType.THREAD_STATS,
                selectorType,
                selectorId,
                ThreadStatsEventData.class);
    }

    /**
     * Predefined event type to catch deleted Thread events.
     */
    public static EventType<ThreadDeletedEventData> ThreadDeletedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadDeleted",
                ThreadEventType.THREAD_DELETE,
                selectorType,
                selectorId,
                ThreadDeletedEventData.class);
    }

    /**
     * Predefined event type to catch created Store events.
     */
    public static EventType<Store> StoreCreatedEvent(String contextId) {
        return new EventType<>(
                "storeCreated",
                StoreEventType.STORE_CREATE,
                StoreEventSelectorType.CONTEXT_ID,
                contextId,
                Store.class);
    }

    /**
     * Predefined event type to catch updated Store events.
     */
    public static EventType<Store> StoreUpdatedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeUpdated",
                StoreEventType.STORE_UPDATE,
                selectorType,
                selectorId,
                Store.class);
    }

    /**
     * Predefined event type to catch updated Store stats events.
     */
    public static EventType<StoreStatsChangedEventData> StoreStatsChangedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeStatsChanged",
                StoreEventType.STORE_STATS,
                selectorType,
                selectorId,
                StoreStatsChangedEventData.class);
    }

    /**
     * Predefined event type to catch deleted Store stats events.
     */
    public static EventType<StoreDeletedEventData> StoreDeletedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeDeleted",
                StoreEventType.STORE_DELETE,
                selectorType,
                selectorId,
                StoreDeletedEventData.class);
    }

    /**
     * Predefined event type to catch created KVDB events.
     */
    public static EventType<Kvdb> KvdbCreatedEvent(String contextId) {
        return new EventType<>(
                "kvdbCreated",
                KvdbEventType.KVDB_CREATE,
                KvdbEventSelectorType.CONTEXT_ID,
                contextId,
                Kvdb.class);
    }

    /**
     * Predefined event type to catch updated KVDB events.
     */
    public static EventType<Kvdb> KvdbUpdatedEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbUpdated",
                KvdbEventType.KVDB_UPDATE,
                selectorType,
                selectorId,
                Kvdb.class);
    }

    /**
     * Predefined event type to catch deleted KVDB events.
     */
    public static EventType<KvdbDeletedEventData> KvdbDeletedEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbDeleted",
                KvdbEventType.KVDB_DELETE,
                selectorType,
                selectorId,
                KvdbDeletedEventData.class);
    }

    /**
     * Predefined event type to catch updated KVDB stats events.
     */
    public static EventType<KvdbStatsEventData> KvdbStatsEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbStatsChanged",
                KvdbEventType.KVDB_STATS,
                selectorType,
                selectorId,
                KvdbStatsEventData.class);
    }

    /**
     * Predefined event type to catch created Inbox events.
     */
    public static EventType<Inbox> InboxCreatedEvent(String contextId) {
        return new EventType<>(
                "inboxCreated",
                InboxEventType.INBOX_CREATE,
                InboxEventSelectorType.CONTEXT_ID,
                contextId,
                Inbox.class);
    }

    /**
     * Predefined event type to catch update Inbox events.
     */
    public static EventType<Inbox> InboxUpdatedEvent(InboxEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "inboxUpdated",
                InboxEventType.INBOX_UPDATE,
                selectorType,
                selectorId,
                Inbox.class);
    }

    /**
     * Predefined event type to catch deleted Inbox events.
     */
    public static EventType<InboxDeletedEventData> InboxDeletedEvent(InboxEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "inboxDeleted",
                InboxEventType.INBOX_DELETE,
                selectorType,
                selectorId,
                InboxDeletedEventData.class);
    }

    /**
     * Returns instance to register on new message Events.
     *
     * @return Predefined event type to catch new messages in matching Thread events
     */
    public static EventType<Message> ThreadNewMessageEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadNewMessage",
                ThreadEventType.MESSAGE_CREATE,
                selectorType,
                selectorId,
                Message.class);
    }

    /**
     * Returns instance to register on message update Events.
     *
     * @return predefined event type to catch message updates in matching Thread events
     */
    public static EventType<Message> ThreadMessageUpdatedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadUpdatedMessage",
                ThreadEventType.MESSAGE_UPDATE,
                selectorType,
                selectorId,
                Message.class);
    }

    /**
     * Returns instance to register on deleted message Events.
     *
     * @return Predefined event type to catch deleted messages in matching Thread events
     */
    public static EventType<ThreadDeletedMessageEventData> ThreadMessageDeletedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "threadMessageDeleted",
                ThreadEventType.MESSAGE_DELETE,
                selectorType,
                selectorId,
                ThreadDeletedMessageEventData.class);
    }

    /**
     * Returns instance to register on created file Events.
     *
     * @return Predefined event type to catch new files in matching Store events
     */
    public static EventType<File> StoreFileCreatedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeFileCreated",
                StoreEventType.FILE_CREATE,
                selectorType,
                selectorId,
                File.class);
    }

    /**
     * Returns instance to register on file update Events.
     *
     * @return Predefined event type to catch updated files in matching Store events
     */
    public static EventType<File> StoreFileUpdatedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeFileUpdated",
                StoreEventType.FILE_UPDATE,
                selectorType,
                selectorId,
                File.class);
    }

    /**
     * Returns instance to register on deleted file Events.
     *
     * @return Predefined event type to catch deleted files in matching Store events
     */
    public static EventType<StoreFileDeletedEventData> StoreFileDeletedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "storeFileDeleted",
                StoreEventType.FILE_DELETE,
                selectorType,
                selectorId,
                StoreFileDeletedEventData.class);
    }

    /**
     * Returns instance to register on created entry Events.
     *
     * @return predefined event type to catch created entries in matching Inbox events
     */
    public static EventType<InboxEntry> InboxEntryCreatedEvent(InboxEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "inboxEntryCreated",
                InboxEventType.ENTRY_CREATE,
                selectorType,
                selectorId,
                InboxEntry.class);
    }

    /**
     * Returns instance to register on deleting entries Events.
     *
     * @return predefined event type to catch deleted entries in matching Inbox events
     */
    public static EventType<InboxEntryDeletedEventData> InboxEntryDeletedEvent(InboxEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "inboxEntryDeleted",
                InboxEventType.ENTRY_DELETE,
                selectorType,
                selectorId,
                InboxEntryDeletedEventData.class);
    }

    /**
     * Returns instance to register for custom Context Events.
     *
     * @param contextId   ID of the Context to observe
     * @param channelName name of the Channel
     * @return predefined event type to catch emitted custom Context events
     */
    public static EventType<ContextCustomEventData> ContextCustomEvent(String contextId, String channelName) {
        return new EventType<>(
                "contextCustom",
                null,
                CustomEventSelectorType.CONTEXT_ID,
                contextId,
                channelName,
                ContextCustomEventData.class);
    }

    /**
     * Predefined event type to catch created KVDB entries events.
     */
    public static EventType<KvdbEntry> KvdbNewEntryEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbNewEntry",
                KvdbEventType.ENTRY_CREATE,
                selectorType,
                selectorId,
                KvdbEntry.class);
    }

    /**
     * Predefined event type to catch updated KVDB entries events.
     */
    public static EventType<KvdbEntry> KvdbEntryUpdatedEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbEntryUpdated",
                KvdbEventType.ENTRY_UPDATE,
                selectorType,
                selectorId,
                KvdbEntry.class);
    }

    /**
     * Predefined event type to catch deleted KVDB entries events.
     */
    public static EventType<KvdbDeletedEntryEventData> KvdbEntryDeletedEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "kvdbEntryDeleted",
                KvdbEventType.ENTRY_DELETE,
                selectorType,
                selectorId,
                KvdbDeletedEntryEventData.class);
    }

    public static EventType<CollectionChangedEventData> CollectionChangedEvent(ThreadEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "collectionChanged",
                ThreadEventType.COLLECTION_CHANGE,
                selectorType,
                selectorId,
                CollectionChangedEventData.class
        );
    }

    public static EventType<CollectionChangedEventData> CollectionChangedEvent(StoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "collectionChanged",
                StoreEventType.COLLECTION_CHANGE,
                selectorType,
                selectorId,
                CollectionChangedEventData.class
        );
    }

    public static EventType<CollectionChangedEventData> CollectionChangedEvent(InboxEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "collectionChanged",
                InboxEventType.COLLECTION_CHANGE,
                selectorType,
                selectorId,
                CollectionChangedEventData.class
        );
    }

    public static EventType<CollectionChangedEventData> CollectionChangedEvent(KvdbEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "collectionChanged",
                KvdbEventType.COLLECTION_CHANGE,
                selectorType,
                selectorId,
                CollectionChangedEventData.class
        );
    }


    public static EventType<ContextUserEventData> ContextUserAddedEvent(CoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "contextUserAdded",
                CoreEventType.USER_ADD,
                selectorType,
                selectorId,
                ContextUserEventData.class
        );
    }

    public static EventType<ContextUserEventData> ContextUserRemovedEvent(CoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "contextUserRemoved",
                CoreEventType.USER_REMOVE,
                selectorType,
                selectorId,
                ContextUserEventData.class
        );
    }

    public static EventType<ContextUsersStatusChangedEventData> ContextUsersStatusChangeEvent(CoreEventSelectorType selectorType, String selectorId) {
        return new EventType<>(
                "contextUserStatusChanged",
                CoreEventType.USER_STATUS,
                selectorType,
                selectorId,
                ContextUsersStatusChangedEventData.class
        );
    }

}