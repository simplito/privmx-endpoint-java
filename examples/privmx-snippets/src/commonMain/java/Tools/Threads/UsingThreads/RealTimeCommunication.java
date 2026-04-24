package Tools.Threads.UsingThreads;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.ThreadEventSelectorType;
import com.simplito.java.privmx_endpoint_extra.events.CallbackRegistration;
import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class RealTimeCommunication extends ManagingThreads {
    void handlingThreadAndMessageEvents() {
        String threadCallbacksGroup = "THREAD_CALLBACKS_GROUP";
        String messageCallbacksGroup = "MESSAGE_CALLBACKS_GROUP";
        String threadID = "THREAD_ID";

        // Starting the Event Loop
        endpointContainer.startListening();

        endpointSession.registerManyCallbacks(

                // Handling Thread events
                new CallbackRegistration<>(
                        threadCallbacksGroup,
                        EventType.ThreadCreatedEvent(contextId),
                        newThread -> {
                            System.out.println(newThread.threadId);
                        }
                ),

                //Handling message Events
                new CallbackRegistration<>(
                        messageCallbacksGroup,
                        EventType.ThreadNewMessageEvent(
                                ThreadEventSelectorType.THREAD_ID,
                                threadID
                        ),
                        newMessage -> {
                            System.out.println(newMessage.info.messageId);
                        }
                )
        );

        endpointSession.unregisterCallbacks(threadCallbacksGroup, messageCallbacksGroup);
    }
}
