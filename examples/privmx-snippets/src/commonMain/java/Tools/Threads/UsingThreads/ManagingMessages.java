package Tools.Threads.UsingThreads;

import com.simplito.java.privmx_endpoint.model.Message;
import com.simplito.java.privmx_endpoint_extra.model.SortOrder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ManagingMessages extends ManagingThreads {
    void listMessages() {
        String threadId = "THREAD_ID";
        long startIndex = 0;
        long pageSize = 30;

        List<Message> messages = endpointSession.threadApi.listMessages(
                threadId,
                startIndex,
                pageSize,
                SortOrder.DESC
        ).readItems;
    }

    void updatingMessages() {
        String messageId = "MESSAGE_ID";
        byte[] privateMeta = "New private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "New public data".getBytes(StandardCharsets.UTF_8);
        byte[] data = "New message data".getBytes(StandardCharsets.UTF_8);

        endpointSession.threadApi.updateMessage(
                messageId,
                publicMeta,
                privateMeta,
                data
        );
    }

    void deletingMessage() {
        String messageId = "MESSAGE_ID";
        endpointSession.threadApi.deleteMessage(messageId);
    }
}