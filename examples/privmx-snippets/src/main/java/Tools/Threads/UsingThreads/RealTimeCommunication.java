package Tools.Threads.UsingThreads;

import com.simplito.java.privmx_endpoint_extra.events.EventType;

public class RealTimeCommunication extends ManagingThreads{
    void handlingThreadAndMessageEvents() {
        String callbacksID = "CALLBACK_ID";
        String threadID = "THREAD_ID";
        // Starting the Event Loop
        endpointContainer.startListening();

        // Handling Thread events
        endpointSession.registerCallback(
                callbacksID,
                EventType.ThreadCreatedEvent,
                newThread -> {
                    System.out.println(newThread.threadId);
                }
        );

        //Handling message Events
        endpointSession.registerCallback(
                callbacksID,
                EventType.ThreadNewMessageEvent(threadID),
                newMessage -> {
                    System.out.println(newMessage.info.messageId);
                }
        );
    }
}
