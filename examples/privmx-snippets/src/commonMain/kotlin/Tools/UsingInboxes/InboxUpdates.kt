package Tools.UsingInboxes

import com.simplito.java.privmx_endpoint.model.Inbox
import com.simplito.java.privmx_endpoint.model.InboxEntry
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration
import com.simplito.java.privmx_endpoint_extra.events.EventCallback
import com.simplito.java.privmx_endpoint_extra.events.EventType

class InboxUpdates : WorkingWithInboxes() {
    fun handlingInboxEvents() {
        val InboxCallbackID = "INBOX_CALLBACK_ID"
        val EntryCallbackID = "ENTRY_CALLBACK_ID"
        val inboxID = "INBOX_ID"

        // Starting the Event Loop
        endpointContainer.startListening()

        endpointSession.registerManyCallbacks(

            // Handling Inbox Events
            CallbackRegistration(
                InboxCallbackID,
                EventType.InboxUpdatedEvent(
                    InboxEventSelectorType.CONTEXT_ID,
                    contextId
                ),
                EventCallback { updatedInbox: Inbox? ->
                    println(updatedInbox!!.lastModifier)
                }
            ),

            // Handling Inbox Entry Events
            CallbackRegistration(
                EntryCallbackID,
                EventType.InboxEntryCreatedEvent(
                    InboxEventSelectorType.INBOX_ID,
                    inboxID
                ),
                EventCallback { newEntry: InboxEntry? ->
                    println(newEntry!!.inboxId)
                }
            )
        )


        endpointSession.unregisterCallbacks(EntryCallbackID)
    }
}