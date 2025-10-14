package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class ListeningForChanges extends ManagingKvdbs {
    void handlingKvdbEvents() {
        String kvdbCallbackID = "KVDB_CALLBACK_ID";
        String entryCallbackID = "ENTRY_CALLBACK_ID";
        String kvdbID = "KVDB_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling KVDB Events
                new CallbackRegistration<>(
                        kvdbCallbackID,
                        EventType.KvdbStatsEvent(
                                KvdbEventSelectorType.CONTEXT_ID,
                                contextId
                        ),
                        kvdbStats -> {
                            System.out.println(kvdbStats.lastEntryDate);
                        }
                ),

                // Handling KVDB Entry Events
                new CallbackRegistration<>(
                        entryCallbackID,
                        EventType.KvdbNewEntryEvent(
                                KvdbEventSelectorType.KVDB_ID,
                                kvdbID
                        ),
                        newEntry -> {
                            System.out.println(newEntry.info.key);
                        }
                )
        );

        // Finish handling events
        endpointSession.unregisterCallbacks(kvdbCallbackID, entryCallbackID);
    }
}