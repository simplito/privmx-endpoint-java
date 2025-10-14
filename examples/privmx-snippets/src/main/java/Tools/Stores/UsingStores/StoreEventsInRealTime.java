package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class StoreEventsInRealTime extends ManagingStores {
    void handlingStoreEvents() {
        String callbacksID = "CALLBACK_ID";
        String storeID = "STORE_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling Store Events
                new CallbackRegistration<>(
                        callbacksID,
                        EventType.StoreCreatedEvent(contextId),
                        newStore -> {
                            System.out.println(newStore.storeId);
                        }
                ),

                // Handling File Events
                new CallbackRegistration<>(
                        callbacksID,
                        EventType.StoreFileCreatedEvent(
                                StoreEventSelectorType.STORE_ID,
                                storeID
                        ),
                        newFile -> {
                            System.out.println(newFile.info.fileId);
                        }
                )
        );
    }
}
