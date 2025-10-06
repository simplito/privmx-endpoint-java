package Stacks.JavaKotlin.events

import com.simplito.java.privmx_endpoint_extra.events.EventType
import Stacks.JavaKotlin.endpointSession

// START: Connection events snippets
fun handlingConnectionEvents() {
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerCallback(
        callbacksId,
        EventType.ConnectedEvent
    ) {
        // some actions when lib was connected
    }

    endpointSession.registerCallback(
        callbacksId,
        EventType.DisconnectedEvent
    ) {
        // some actions when lib was disconnected
    }
}
// END: Connection events snippets

// START: Core events snippets
fun handlingCoreEvents() {
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.ContextUserAddedEvent(contextId)
        ) { newUserData ->
            // some actions when a user is added to the context
        },

        CallbackRegistration(
            callbacksId,
            EventType.ContextUserRemovedEvent(contextId)
        ) { removedUserData ->
            // some actions when a user is removed from the context
        },

        CallbackRegistration(
            callbacksId,
            EventType.ContextUsersStatusChangeEvent(contextId)
        ) { usersWithStatusUpdateData ->
            // some actions when user statuses have changed
        }
    )
}
// END: Core events snippets

// START: Threads events snippets
fun handlingThreadEvents() {
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.ThreadCreatedEvent(contextId)
        ) { newThreadData ->
            // some actions when a new thread is created
        },

        CallbackRegistration(
            callbacksId,
            EventType.ThreadUpdatedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { threadUpdateData ->
            // some actions when a thread is updated
        },

        CallbackRegistration(
            callbacksId,
            EventType.ThreadStatsChangedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId,
            )
        ) { threadUpdateData ->
            // some actions when thread stats have changed
        },

        CallbackRegistration(
            callbacksId,
            EventType.CollectionChangedEvent(
                ThreadEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when thread collection changes
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"
    val threadID = "THREAD_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.ThreadNewMessageEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { newMessageData ->
            // some actions on a new message
        },

        CallbackRegistration(
            callbacksId,
            EventType.ThreadMessageUpdatedEvent(
                ThreadEventSelectorType.THREAD_ID,
                threadID
            )
        ) { updatedMessageData ->
            // some actions when a message is updated
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.StoreCreatedEvent(contextId)
        ) { newStoreData ->
            // some actions when new store created
        },

        CallbackRegistration(
            callbacksId,
            EventType.StoreUpdatedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { storeUpdateData ->
            // some actions when a store is updated
        },

        CallbackRegistration(
            callbacksId,
            EventType.StoreStatsChangedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { storeStatsUpdateData ->
            // some actions when store stats have changed
        },

        CallbackRegistration(
            callbacksId,
            EventType.CollectionChangedEvent(
                StoreEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when store collection changes
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"
    val storeID = "STORE_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.StoreFileCreatedEvent(
                StoreEventSelectorType.STORE_ID,
                storeID
            )
        ) { newFileData ->
            // some actions on a new file
        },

        CallbackRegistration(
            callbacksId,
            EventType.StoreFileUpdatedEvent(
                StoreEventSelectorType.STORE_ID,
                storeID
            )
        ) { updatedFileData ->
            // some actions when a file is updated
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.InboxCreatedEvent(contextId)
        ) { newInboxData ->
            // some actions when a new inbox is created
        },

        CallbackRegistration(
            callbacksId,
            EventType.InboxUpdatedEvent(
                InboxEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { inboxUpdateData ->
            // some actions when an inbox is updated
        },

        CallbackRegistration(
            callbacksId,
            EventType.CollectionChangedEvent(
                InboxEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when inbox collection changes
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"
    val inboxID = "INBOX_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.InboxEntryCreatedEvent(
                InboxEventSelectorType.INBOX_ID,
                inboxID
            )
        ) { newEntryData ->
            // some actions on a new entry
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"

    endpointSession.registerManyCallbacks(
        CallbackRegistration(
            callbacksId,
            EventType.KvdbCreatedEvent(contextId)
        ) { kvdbCreatedData ->
            // some actions when a new KVDB is created
        },

        CallbackRegistration(
            callbacksId,
            EventType.KvdbUpdatedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { kvdbUpdatedData ->
            // some actions when a KVDB is updated
        },

        CallbackRegistration(
            callbacksId,
            EventType.KvdbStatsEvent(       // todo - change
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { kvdbStatsUpdateData ->
            // some actions when kvdb stats have changed
        },

        CallbackRegistration(
            callbacksId,
            EventType.CollectionChangedEvent(
                KvdbEventSelectorType.CONTEXT_ID,
                contextId
            )
        ) { changedCollectionData ->
            // some actions when kvdb collection changes
        },

        CallbackRegistration(
            callbacksId,
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
    val callbacksId = "CALLBACKS_ID"
    val kvdbID = "KVDB_ID"

    endpointSession.registerCallback(
        callbacksId,
        EventType.KvdbNewEntryEvent(kvdbID)
    ) { newEntryData ->
        // some actions on new KVDB entry
    }

    endpointSession.registerCallback(
        callbacksId,
        EventType.KvdbEntryUpdatedEvent(kvdbID)
    ) { updatedEntryData ->
        // some actions when KVDB entry updated
    }

    endpointSession.registerCallback(
        callbacksId,
        EventType.KvdbEntryDeletedEvent(kvdbID)
    ) { deletedEntryData ->
        // some actions when KVDB entry deleted
    }
}
// END: KVDBs events snippets
