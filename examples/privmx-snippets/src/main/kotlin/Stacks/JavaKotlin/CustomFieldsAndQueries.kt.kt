package Stacks.JavaKotlin

import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject

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

    buildJsonObject {
        put("threadType", JsonPrimitive("special"))
        put("numberOfMessages", JsonPrimitive(20))
        put("tags", buildJsonArray {
            add(JsonPrimitive("TAG1"))
            add(JsonPrimitive("TAG2"))
            add(JsonPrimitive("TAG3"))
        })
    }

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
    var queryJsonBuilder = buildJsonObject {
        put("threadType", JsonPrimitive("special"))
    }

    queryJsonBuilder = buildJsonObject {
        put("numberOfMessages", buildJsonObject { put("\$gt", JsonPrimitive(10)) })
        put("tags", JsonPrimitive("TAG2"))
    }

    queryJsonBuilder = buildJsonObject {
        put("\$or", buildJsonArray {
            add(buildJsonObject {
                put("threadType", JsonPrimitive("archived"))
            })
            add(buildJsonObject {
                put("numberOfMessages", JsonPrimitive(20))
            })
        })
    }

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

    var filteredListOfThreads = endpointSession.threadApi.listThreads(
        contextId,
        startIndex,
        pageSize,
        SortOrder.ASC,
        lastId,
        query
    )

    filteredListOfThreads = endpointSession.threadApi.listThreads(
        contextId,
        startIndex,
        pageSize,
        SortOrder.ASC,
        lastId,
        Json.encodeToString(JsonElement.serializer(), queryJsonBuilder)
    )
}