package Tools.Threads.UsingThreads;

import java.nio.charset.StandardCharsets;

public class SendingMessages extends ManagingThreads{
    void sendingMessages() {
        String threadId = "THREAD_ID";
        byte[] privateMeta = "My private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "My public data".getBytes(StandardCharsets.UTF_8);
        byte[] message = "This is my message".getBytes(StandardCharsets.UTF_8);

        String msgId = endpointSession.threadApi.sendMessage(
                threadId,
                publicMeta,
                privateMeta,
                message
        );
    }
}
