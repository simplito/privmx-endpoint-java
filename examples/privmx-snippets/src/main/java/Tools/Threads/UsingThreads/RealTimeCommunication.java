package Tools.Threads.UsingThreads;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class RealTimeCommunication extends ManagingThreads {
    void handlingThreadAndMessageEvents() {
        String callbacksID = "CALLBACK_ID";
        String threadID = "THREAD_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling Thread events
                new CallbackRegistration<>(
                        callbacksID,
                        EventType.ThreadCreatedEvent(contextId),
                        newThread -> {
                            System.out.println(newThread.threadId);
                        }
                ),

                //Handling message Events
                new CallbackRegistration<>(
                        callbacksID,
                        EventType.ThreadNewMessageEvent(
                                ThreadEventSelectorType.THREAD_ID,
                                threadID
                        ),
                        newMessage -> {
                            System.out.println(newMessage.info.messageId);
                        }
                )
        );
    }
}
