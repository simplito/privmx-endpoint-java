package Stacks.JavaKotlin

import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint_extra.model.SortOrder

fun CreatingThreadWithCustomField() {
    val contextId = "CONTEXT_ID"
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val privateMeta = "PRIVATE_META"

    val publicMeta = """
    {
        "threadType": "special"
        "numberOfMessages": 20
        "tags": ["TAG1", "TAG2", "TAG3"]
    }
    """.trimIndent()

    endpointSession.threadApi.createThread(
        contextId,
        users,
        managers,
        publicMeta.encodeToByteArray(),
        privateMeta.encodeToByteArray()
    )
}

fun ListingThreadsWithCustomQueries() {
    var query = """
        {
            "threadType": "special"
        }
    """.trimIndent()

    query = """
        {
            "numberOfMessages": { "${'$'}gt": 10 },
            "tags": "TAG2"
        }
    """.trimIndent()

    query = """
        {
            "${'$'}or": [
                { "threadType": "archived"},
                { "numberOfMessages": 20}
            ]
        }
    """.trimIndent()

    val startIndex = 0L
    val pageSize = 100L
    val lastId = null

    endpointSession.threadApi.listThreads(
        contextId,
        startIndex,
        pageSize,
        SortOrder.ASC,
        lastId,
        query
    )
}