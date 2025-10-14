package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.KvdbEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class ListeningForChanges extends ManagingKvdbs {
    void handlingKvdbEvents() {
        String KvdbCallbackID = "KVDB_CALLBACK_ID";
        String EntrycallbackID = "ENTRY_CALLBACK_ID";
        String kvdbID = "KVDB_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling KVDB Events
                new CallbackRegistration<>(
                        KvdbCallbackID,
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
                        EntrycallbackID,
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
        endpointSession.unregisterCallbacks(KvdbCallbackID);
        endpointSession.unregisterCallbacks(EntrycallbackID);
    }
}