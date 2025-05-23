package Tools.Threads.UsingThreads;

import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.Thread;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;
import com.simplito.java.privmx_endpoint_extra.model.Modules;
import com.simplito.java.privmx_endpoint_extra.model.SortOrder;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

public class ManagingThreads {
    // START: Initial Assumptions Snippets
    /*
        All the values below like BRIDGE_URL, SOLUTION_ID, CONTEXT_ID
        should be replaced by the ones corresponding to your Bridge Server instance.

        The private keys here are for demonstration purposes only.
        Normally, they should be kept separately by each user and stored in a safe place,
        or generated from a password (see the derivePrivateKey2() method in the Crypto API)
    */

    String bridgeUrl = "YOUR_BRIDGE_URL";
    String solutionId = "YOUR_SOLUTION_ID";
    String contextId = "YOUR_CONTEXT_ID";

    String user1Id = "USER_ID_1";
    String user1PublicKey = "PUBLIC_KEY_1";
    String user1PrivateKey = "PRIVATE_KEY_1";

    String user2Id = "USER_ID_2";
    String user2PublicKey = "PUBLIC_KEY_2";

    String user3Id = "USER_ID_3";
    String user3PublicKey = "PUBLIC_KEY_3";

    PrivmxEndpointContainer endpointContainer = new PrivmxEndpointContainer();

    Set<Modules> initModules = Set.of(Modules.STORE);
    PrivmxEndpoint endpointSession = endpointContainer.connect(
            initModules,
            user1PrivateKey,
            solutionId,
            bridgeUrl
    );
    // END: Initial Assumptions Snippets

    void creatingThreads() {
        byte[] privateMeta = "My private data".getBytes(StandardCharsets.UTF_8);
        byte[] publicMeta = "My public data".getBytes(StandardCharsets.UTF_8);
        List<UserWithPubKey> users = List.of(
                new UserWithPubKey(user1Id, user1PublicKey),
                new UserWithPubKey(user2Id, user2PublicKey)
        );
        List<UserWithPubKey> managers = List.of(
                new UserWithPubKey(user1Id, user1PublicKey)
        );

        String threadID = endpointSession.threadApi.createThread(
                contextId,
                users,
                managers,
                publicMeta,
                privateMeta
        );
    }

    void listingThreads() {
        long limit = 30L;
        long skip = 0L;

        PagingList<Thread> threads = endpointSession.threadApi.listThreads(
                contextId,
                skip,
                limit,
                SortOrder.DESC
        );
    }

    void modifyingThreads() {
        String threadID = "THREAD_ID";
        Thread thread = endpointSession.threadApi.getThread(threadID);
        List<UserWithPubKey> newUsers = List.of(
                new UserWithPubKey(user1Id, user1PublicKey),
                new UserWithPubKey(user2Id, user2PublicKey),
                new UserWithPubKey(user3Id, user3PublicKey)
        );
        List<UserWithPubKey> newManagers = List.of(
                new UserWithPubKey(user1Id, user1PublicKey),
                new UserWithPubKey(user2Id, user2PublicKey)
        );
        byte[] newPrivateMeta = "New thread name".getBytes(StandardCharsets.UTF_8);

        endpointSession.threadApi.updateThread(
                threadID,
                newUsers,
                newManagers,
                thread.publicMeta,
                newPrivateMeta,
                thread.version,
                false
        );
    }
}
