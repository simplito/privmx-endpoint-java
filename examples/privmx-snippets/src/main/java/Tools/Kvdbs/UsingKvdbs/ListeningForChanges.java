package Tools.Kvdbs.UsingKvdbs;

import com.simplito.java.privmx_endpoint_extra.events.EventType;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;

public class ListeningForChanges extends ManagingKvdbs {
    void handlingKvdbEvents() {
        PrivmxEndpointContainer container = new PrivmxEndpointContainer();
        Long connectionID = 0L;
        String KvdbCallbackID = "KVDB_CALLBACK_ID";
        String EntrycallbackID = "ENTRY_CALLBACK_ID";
        String kvdbID = "KVDB_ID";

        // Starting the Event Loop
        container.startListening();

        // Handling KVDB Events
        container.getEndpoint(connectionID).registerCallback(
                KvdbCallbackID,
                EventType.KvdbStatsEvent,
                kvdbStats -> {
                    System.out.println(kvdbStats.lastEntryDate);
                }
        );

        // Handling KVDB Entry Events
        container.getEndpoint(connectionID).registerCallback(
                EntrycallbackID,
                EventType.KvdbNewEntryEvent(kvdbID),
                newEntry -> {
                    System.out.println(newEntry.info.key);
                }
        );

        // Finish handling events
        container.getEndpoint(connectionID).unregisterCallbacks(KvdbCallbackID);
        container.getEndpoint(connectionID).unregisterCallbacks(EntrycallbackID);
    }
}