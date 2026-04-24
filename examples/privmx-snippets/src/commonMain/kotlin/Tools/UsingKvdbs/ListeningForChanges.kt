package Tools.UsingKvdbs

import com.simplito.java.privmx_endpoint.model.KvdbEntry
import com.simplito.java.privmx_endpoint.model.events.KvdbStatsEventData
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration
import com.simplito.java.privmx_endpoint_extra.events.EventType

class ListeningForChanges : ManagingKvdbs() {
    fun handlingKvdbEvents() {
        val KvdbCallbackID = "KVDB_CALLBACK_ID"
        val EntrycallbackID = "ENTRY_CALLBACK_ID"
        val kvdbID = "KVDB_ID"

        // Starting the Event Loop
        endpointContainer.startListening()

        endpointSession.registerManyCallbacks(

            // Handling KVDB Events
            CallbackRegistration(
                KvdbCallbackID,
                EventType.KvdbStatsChangedEvent(
                    KvdbEventSelectorType.CONTEXT_ID,
                    contextId
                ),
                { kvdbStats: KvdbStatsEventData? ->
                    println(kvdbStats!!.lastEntryDate)
                }
            ),

            // Handling KVDB Entry Events
            CallbackRegistration(
                EntrycallbackID,
                EventType.KvdbNewEntryEvent(
                    KvdbEventSelectorType.KVDB_ID,
                    kvdbID
                ),
                { newEntry: KvdbEntry? ->
                    println(newEntry!!.info.key)
                }
            )
        )

        // Finish handling events
        endpointSession.unregisterCallbacks(KvdbCallbackID)
        endpointSession.unregisterCallbacks(EntrycallbackID)
    }
}