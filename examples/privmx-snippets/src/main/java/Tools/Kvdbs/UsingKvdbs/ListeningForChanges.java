package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;

public class ListeningForChanges extends ManagingKvdbs {
    void handlingKvdbEvents() {
        String KvdbCallbackID = "KVDB_CALLBACK_ID";
        String EntrycallbackID = "ENTRY_CALLBACK_ID";
        String kvdbID = "KVDB_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        // Handling KVDB Events
        endpointSession.registerCallback(
                KvdbCallbackID,
                EventType.KvdbStatsEvent,
                kvdbStats -> {
                    System.out.println(kvdbStats.lastEntryDate);
                }
        );

        // Handling KVDB Entry Events
        endpointSession.registerCallback(
                EntrycallbackID,
                EventType.KvdbNewEntryEvent(kvdbID),
                newEntry -> {
                    System.out.println(newEntry.info.key);
                }
        );

        // Finish handling events
        endpointSession.unregisterCallbacks(KvdbCallbackID);
        endpointSession.unregisterCallbacks(EntrycallbackID);
    }
}