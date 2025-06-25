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

fun creatinqQueryWithOneSpecifiedValue(): String {
    //Start snippet
    val query = """
    {
        "threadType": "special"
    }
    """.trimIndent()
    //End snippet

    return query
}

fun creatinqQueryWithTwoSpecifiedValues(): String {
    //Start snippet
    val query = """
    {
        "numberOfMessages": { "${'$'}gt": 10 },
        "tags": "TAG2"
    }
    """.trimIndent()
    //End snippet

    return query
}

fun creatinqQueryWithOrCondition(): String {
    //Start snippet
    val query = """
    {
        "${'$'}or": [
            { "threadType": "archived"},
            { "numberOfMessages": 20}
        ]
    }
    """.trimIndent()
    //End snippet

    return query
}

fun ListingThreadsWithCustomQueries(query: String) {
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