package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class StoreEventsInRealTime extends ManagingStores{
    void handlingStoreEvents() {
        String callbacksID = "CALLBACK_ID";
        String storeID = "STORE_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        // Handling Store Events
        endpointSession.registerCallback(
                callbacksID,
                EventType.StoreCreatedEvent,
                newStore -> {
                    System.out.println(newStore.storeId);
                }
        );

        // Handling File Events
        endpointSession.registerCallback(
                callbacksID,
                EventType.StoreFileCreatedEvent(storeID),
                newFile -> {
                    System.out.println(newFile.info.fileId);
                }
        );
    }
}
