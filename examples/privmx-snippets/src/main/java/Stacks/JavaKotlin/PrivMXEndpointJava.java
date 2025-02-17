package Stacks.JavaKotlin;

import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint;
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer;
import com.simplito.java.privmx_endpoint_extra.model.Modules;

import java.util.Set;

public class PrivMXEndpointJava {
    PrivmxEndpointContainer endpointContainer;
    PrivmxEndpoint endpointSession;

// START: Initial assumption snippet
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
// END: Initial assumption snippet

    void makeConnection(){
        // START: Make connection snippet
        String pathToCerts = "PATH_TO_CERTS"; // Path to .pem ssl certificate to connect with Privmx Bridge
        Set<Modules> initModules = Set.of(
                Modules.THREAD, // initializes ThreadApi to working with Threads
                Modules.STORE, // initializes StoreApi to working with Stores
                Modules.INBOX // initializes InboxApi to working with Inboxes
        ); // set of modules to activate in new connection

        PrivmxEndpointContainer endpointContainer = new PrivmxEndpointContainer();
        endpointContainer.setCertsPath(pathToCerts);

        PrivmxEndpoint endpointSession = endpointContainer.connect(
                initModules,
                user1PrivateKey,
                solutionId,
                bridgeUrl
        );
        // END: Make connection snippet

        this.endpointContainer = endpointContainer;
        this.endpointSession = endpointSession;
    }
}
