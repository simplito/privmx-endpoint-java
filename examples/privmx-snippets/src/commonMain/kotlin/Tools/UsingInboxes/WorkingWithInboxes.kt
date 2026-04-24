package Tools.UsingInboxes

import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpoint
import com.simplito.java.privmx_endpoint_extra.lib.PrivmxEndpointContainer
import com.simplito.java.privmx_endpoint_extra.model.Modules
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import java.nio.charset.StandardCharsets
import java.util.Set

open class WorkingWithInboxes {
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

    var initModules = Set.of<Modules>(Modules.INBOX)
    var endpointSession: PrivmxEndpoint = endpointContainer.connect(
        initModules,
        user1PrivateKey,
        solutionId,
        bridgeUrl
    )

    // END: Initial Assumptions Snippets
    fun creatingInboxes() {
        val privateMeta = "My private data".toByteArray(StandardCharsets.UTF_8)
        val publicMeta = "My public data".toByteArray(StandardCharsets.UTF_8)
        val users = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey),
            UserWithPubKey(user2Id, user2PublicKey)
        )
        val managers = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey)
        )

        val inboxID = endpointSession.inboxApi.createInbox(
            contextId,
            users,
            managers,
            publicMeta,
            privateMeta
        )
    }

    fun listingInboxes() {
        val limit = 30L
        val skip = 0L

        val inboxes = endpointSession.inboxApi.listInboxes(
            contextId,
            skip,
            limit,
            SortOrder.DESC
        )
    }

    fun modifyingInboxes() {
        val inboxID = "INBOX_ID"
        val inbox = endpointSession.inboxApi.getInbox(inboxID)
        val newUsers = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey),
            UserWithPubKey(user2Id, user2PublicKey),
            UserWithPubKey(user3Id, user3PublicKey)
        )
        val newManagers = listOf<UserWithPubKey?>(
            UserWithPubKey(user1Id, user1PublicKey),
            UserWithPubKey(user2Id, user2PublicKey)
        )
        val newPrivateMeta = "New inbox name".toByteArray(StandardCharsets.UTF_8)

        endpointSession.inboxApi.updateInbox(
            inboxID,
            newUsers,
            newManagers,
            inbox.publicMeta,
            newPrivateMeta,
            null,  // filesConfig
            inbox.version,
            false // force
        )
    }
}