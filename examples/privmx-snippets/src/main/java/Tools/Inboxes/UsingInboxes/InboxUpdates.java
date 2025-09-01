package Tools.Inboxes.UsingInboxes;

import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;

public class InboxUpdates extends WorkingWithInboxes {
    void handlingInboxEvents() {
        PrivmxEndpointContainer container = new PrivmxEndpointContainer();
        Long connectionID = 0L;
        String InboxCallbackID = "INBOX_CALLBACK_ID";
        String EntryCallbackID = "ENTRY_CALLBACK_ID";
        String inboxID = "INBOX_ID";

        // Starting the Event Loop
        container.startListening();

        // Handling Inbox Events
        container.getEndpoint(connectionID).registerCallback(
                InboxCallbackID,
                EventType.InboxUpdatedEvent,
                updatedInbox -> {
                    System.out.println(updatedInbox.lastModifier);
                }
        );

        // Handling Inbox Entry Events
        container.getEndpoint(connectionID).registerCallback(
                EntryCallbackID,
                EventType.InboxEntryCreatedEvent(inboxID),
                newEntry -> {
                    System.out.println(newEntry.inboxId);
                }
        );

        // Finish handling events
        container.getEndpoint(connectionID).unregisterCallbacks(InboxCallbackID);
        container.getEndpoint(connectionID).unregisterCallbacks(EntryCallbackID);

        container.getEndpoint(connectionID).unregisterAll();
    }
}