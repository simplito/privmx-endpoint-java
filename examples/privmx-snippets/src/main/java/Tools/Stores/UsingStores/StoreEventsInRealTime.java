package Tools.Stores.UsingStores;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StoreEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class StoreEventsInRealTime extends ManagingStores {
    void handlingStoreEvents() {
        String storeCallbacksGroup = "STORE_CALLBACKS_GROUP";
        String fileCallbacksGroup = "FILE_CALLBACKS_GROUP";
        String storeID = "STORE_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling Store Events
                new CallbackRegistration<>(
                        storeCallbacksGroup,
                        EventType.StoreCreatedEvent(contextId),
                        newStore -> {
                            System.out.println(newStore.storeId);
                        }
                ),

                // Handling File Events
                new CallbackRegistration<>(
                        fileCallbacksGroup,
                        EventType.StoreFileCreatedEvent(
                                StoreEventSelectorType.STORE_ID,
                                storeID
                        ),
                        newFile -> {
                            System.out.println(newFile.info.fileId);
                        }
                )
        );

        endpointSession.unregisterCallbacks(storeCallbacksGroup, fileCallbacksGroup);
    }
}
