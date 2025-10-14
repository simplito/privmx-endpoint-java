package Stacks.JavaKotlin.events

import Stacks.JavaKotlin.contextId
import Stacks.JavaKotlin.endpointSession
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration
import com.simplito.java.privmx_endpoint_extra.events.EventType

// START: Mixed events snippets
fun handlingMixedEvents() {
    val newUserCallbacksGroup = "NEW_USER_CALLBACKS_GROUP"
    val newMessageCallbacksGroup = "NEW_MESSAGE_CALLBACKS_GROUP"
    val threadID = "THREAD_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            newUserCallbacksGroup,
            EventType.ContextUserAddedEvent(contextId)
        ) { newUserData ->
            // e.g. Send a message to a new user who has been added to the context
        },

        CallbackRegistration(
            newMessageCallbacksGroup,
            EventType.ThreadNewMessageEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { newMessageData ->
            // e.g. Notify me when new message is posted in this thread
        }
    )
}
// END: Mixed events snippets

// START: Connection events snippets
fun handlingConnectionEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.ConnectedEvent
        ) {
            // some actions when lib was connected
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.DisconnectedEvent
        ) {
            // some actions when lib was disconnected
        }
    )
}
// END: Connection events snippets

// START: Core events snippets
fun handlingCoreEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.ContextUserAddedEvent(contextId)
        ) { newUserData ->
            // some actions when a user is added to the context
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ContextUserRemovedEvent(contextId)
        ) { removedUserData ->
            // some actions when a user is removed from the context
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ContextUsersStatusChangeEvent(contextId)
        ) { usersWithStatusUpdateData ->
            // some actions when user statuses have changed
        }
    )
}
// END: Core events snippets

// START: Threads events snippets
fun handlingThreadEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadCreatedEvent(contextId)
        ) { newThreadData ->
            // some actions when a new thread is created
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadUpdatedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { threadUpdateData ->
            // some actions when a thread is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadStatsChangedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId,
            )
        ) { threadUpdateData ->
            // some actions when thread stats have changed
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.CollectionChangedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when thread collection changes
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadDeletedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { deletedThreadData ->
            // some actions when thread is deleted
        }
    )
}

fun handlingMessageEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"
    val threadID = "THREAD_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadNewMessageEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { newMessageData ->
            // some actions on a new message
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadMessageUpdatedEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { updatedMessageData ->
            // some actions when a message is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.ThreadMessageDeletedEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { deletedMessageData ->
            // some actions when a message is deleted
        }
    )
}
// END: Threads events snippets


// START: Stores events snippets
fun handlingStoreEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.StoreCreatedEvent(contextId)
        ) { newStoreData ->
            // some actions when new store created
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.StoreUpdatedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { storeUpdateData ->
            // some actions when a store is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.StoreStatsChangedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { storeStatsUpdateData ->
            // some actions when store stats have changed
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.CollectionChangedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when store collection changes
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.StoreDeletedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { deletedStoreData ->
            // some actions when a store is deleted
        }
    )
}

fun handlingFileEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"
    val storeID = "STORE_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.StoreFileCreatedEvent(
                StoreEventSelectorType.STORE_ID,
                storeID
            )
        ) { newFileData ->
            // some actions on a new file
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.StoreFileUpdatedEvent(
                StoreEventSelectorType.STORE_ID,
                storeID
            )
        ) { updatedFileData ->
            // some actions when a file is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.StoreFileDeletedEvent(
                StoreEventSelectorType.STORE_ID,
                storeID
            )
        ) { deletedFileData ->
            // some actions when a file is deleted
        }
    )
}
// END: Stores events snippets


// START: Inboxes events snippets
fun handlingInboxEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.InboxCreatedEvent(contextId)
        ) { newInboxData ->
            // some actions when a new inbox is created
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.InboxUpdatedEvent(
                InboxEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { inboxUpdateData ->
            // some actions when an inbox is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.CollectionChangedEvent(
                InboxEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when inbox collection changes
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.InboxDeletedEvent(
                InboxEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { deletedInboxData ->
            // some actions when an inbox is deleted
        }
    )
}

fun handlingEntriesEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"
    val inboxID = "INBOX_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.InboxEntryCreatedEvent(
                InboxEventSelectorType.INBOX_ID,
                inboxID
            )
        ) { newEntryData ->
            // some actions on a new entry
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.InboxEntryDeletedEvent(
                InboxEventSelectorType.INBOX_ID,
                inboxID
            )
        ) { deletedEntryData ->
            // some actions when an entry is deleted
        }
    )
}
// END: Inboxes events snippets


// START: KVDBs events snippets
fun handlingKvdbsEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbCreatedEvent(contextId)
        ) { kvdbCreatedData ->
            // some actions when a new KVDB is created
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbUpdatedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { kvdbUpdatedData ->
            // some actions when a KVDB is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbStatsChangedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { kvdbStatsUpdateData ->
            // some actions when kvdb stats have changed
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.CollectionChangedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when kvdb collection changes
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbDeletedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { kvdbDeletedData ->
            // some actions when KVDB deleted
        }
    )
}

fun handlingKvdbEntriesEvents() {
    val callbacksGroup = "CALLBACKS_GROUP"
    val kvdbID = "KVDB_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbNewEntryEvent(
                KvdbEventSelectorType.KVDB_ID,
                kvdbID
            )
        ) { newEntryData ->
            // some actions on a new KVDB entry
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbEntryUpdatedEvent(
                KvdbEventSelectorType.KVDB_ID,
                kvdbID
            )
        ) { updatedEntryData ->
            // some actions when a KVDB entry is updated
        },

        CallbackRegistration(
            callbacksGroup,
            EventType.KvdbEntryDeletedEvent(
                KvdbEventSelectorType.KVDB_ID,
                kvdbID
            )
        ) { deletedEntryData ->
            // some actions when a KVDB entry is deleted
        }
    )
}
// END: KVDBs events snippets
