package Tools.UsingKvdbs

import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import java.nio.charset.StandardCharsets
import java.util.Set

open class ManagingKvdbs {
    // START: Initial Assumptions Snippets
    /*
        All the values below like BRIDGE_URL, SOLUTION_ID, CONTEXT_ID
        should be replaced by the ones corresponding to your Bridge Server instance.

        The private keys here are for demonstration purposes only.
        Normally, they should be kept separately by each user and stored in a safe place,
        or generated from a password (see the derivePrivateKey2() method in the Crypto API)
    */
    var bridgeUrl: String = "YOUR_BRIDGE_URL"
    var solutionId: String = "YOUR_SOLUTION_ID"
    var contextId: String = "YOUR_CONTEXT_ID"

    var user1Id: String = "USER_ID_1"
    var user1PublicKey: String = "PUBLIC_KEY_1"
    var user1PrivateKey: String = "PRIVATE_KEY_1"

    var user2Id: String = "USER_ID_2"
    var user2PublicKey: String = "PUBLIC_KEY_2"

    var user3Id: String = "USER_ID_3"
    var user3PublicKey: String = "PUBLIC_KEY_3"

    var endpointContainer: PrivmxEndpointContainer = PrivmxEndpointContainer()

    var initModules: MutableSet<Modules?> = Set.of<Modules?>(Modules.KVDB)
    var endpointSession: PrivmxEndpoint = endpointContainer.connect(
        initModules,
        user1PrivateKey,
        solutionId,
        bridgeUrl
    )

    // END: Initial Assumptions Snippets
    fun creatingKvdbs() {
        val privateMeta = "KVDB's private data".toByteArray(StandardCharsets.UTF_8)
        val publicMeta = "KVDB's public data".toByteArray(StandardCharsets.UTF_8)
        val users = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey),
            UserWithPubKey(user2Id, user2PublicKey)
        )
        val managers = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey)
        )

        endpointSession.kvdbApi.createKvdb(
            contextId,
            users,
            managers,
            publicMeta,
            privateMeta
        )
    }
}