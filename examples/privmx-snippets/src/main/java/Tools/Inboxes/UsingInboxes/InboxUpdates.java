package Tools.Inboxes.UsingInboxes;

import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class InboxUpdates extends WorkingWithInboxes {
    void handlingInboxEvents() {
        String InboxCallbackID = "INBOX_CALLBACK_ID";
        String EntryCallbackID = "ENTRY_CALLBACK_ID";
        String inboxID = "INBOX_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        // Handling Inbox Events
        endpointSession.registerCallback(
                InboxCallbackID,
                EventType.InboxUpdatedEvent,
                updatedInbox -> {
                    System.out.println(updatedInbox.lastModifier);
                }
        );

        // Handling Inbox Entry Events
        endpointSession.registerCallback(
                EntryCallbackID,
                EventType.InboxEntryCreatedEvent(inboxID),
                newEntry -> {
                    System.out.println(newEntry.inboxId);
                }
        );

        // Finish handling events
        endpointSession.unregisterCallbacks(InboxCallbackID);
        endpointSession.unregisterCallbacks(EntryCallbackID);

        endpointSession.unregisterAll();
    }
}