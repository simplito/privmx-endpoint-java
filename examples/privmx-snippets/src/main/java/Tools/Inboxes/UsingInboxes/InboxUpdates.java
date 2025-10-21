package Tools.Inboxes.UsingInboxes;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.InboxEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class InboxUpdates extends WorkingWithInboxes {
    void handlingInboxEvents() {
        String inboxCallbacksGroup = "INBOX_CALLBACKS_GROUP";
        String entryCallbacksGroup = "ENTRY_CALLBACKS_GROUP";
        String inboxID = "INBOX_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling Inbox Events
                new CallbackRegistration<>(
                        inboxCallbacksGroup,
                        EventType.InboxUpdatedEvent(
                                InboxEventSelectorType.CONTEXT_ID,
                                contextId
                        ),
                        updatedInbox -> {
                            System.out.println(updatedInbox.lastModifier);
                        }
                ),

                // Handling Inbox Entry Events
                new CallbackRegistration<>(
                        entryCallbacksGroup,
                        EventType.InboxEntryCreatedEvent(
                                InboxEventSelectorType.INBOX_ID,
                                inboxID
                        ),
                        newEntry -> {
                            System.out.println(newEntry.inboxId);
                        }
                )
        );

        endpointSession.unregisterCallbacks(inboxCallbacksGroup, entryCallbacksGroup);
    }
}