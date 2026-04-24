package com.simplito.privmx.endpoint.snippets.Stacks.JavaKotlin.streams

import Stacks.JavaKotlin.contextId
import Stacks.JavaKotlin.user1Id
import Stacks.JavaKotlin.user1PublicKey
import Stacks.JavaKotlin.user2Id
import Stacks.JavaKotlin.user2PublicKey
import com.simplito.java.privmx_endpoint.model.PagingList
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.model.stream.StreamRoom
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


// START: Creating Stream Rooms

fun createStreamRoomBasic() {
    val users = listOf<UserWithPubKey?>(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers = mutableListOf<UserWithPubKey?>(
        UserWithPubKey(user1Id, user1PublicKey)
    )

    val roomId = streamApi.createStreamRoom(
        contextId,
        users,
        managers,
        ByteArray(0),       // publicMeta
        ByteArray(0),       // privateMeta,
        null                // policies
    )
}

fun createStreamRoomWithName() {
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val publicMeta = ByteArray(0)
    val streamRoomNameAsPrivateMeta = "New stream room"

    val streamRoomId = streamApi.createStreamRoom(
        contextId,
        users,
        managers,
        publicMeta,
        streamRoomNameAsPrivateMeta.encodeToByteArray(),
        null
    )
}

@Suppress("UnsafeOptInUsageError")
@Serializable
data class StreamRoomPublicMeta(
    val title: String,
    val type: String,
    val scheduledAt: String
)

fun createStreamRoomWithPublicMeta() {
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val publicMeta = StreamRoomPublicMeta(
        title = "Daily meeting #34",
        type = "meeting",
        scheduledAt = "2026-02-23T10:00:00Z",
    )
    val privateMeta = ByteArray(0)

    val roomId: String = streamApi.createStreamRoom(
        contextId,
        users,
        managers,
        Json.encodeToString(publicMeta).encodeToByteArray(),
        privateMeta,
        null        // policies
    )
}

// END: Creating Stream Rooms


// START: Getting Stream Rooms

fun getMostRecentStreamRooms() {
    val startIndex = 0L
    val pageSize = 100L

    val streamRoomsPagingList: PagingList<StreamRoom> = streamApi.listStreamRooms(
        contextId,
        startIndex,
        pageSize,
        SortOrder.DESC,
        null,       // lastId
        null,       // sortBy
        null        // queryAsJson
    )

    val streamRooms = streamRoomsPagingList.readItems.map {
        StreamRoomItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

fun getMostOldestStreamRooms() {
    val startIndex = 0L
    val pageSize = 100L

    val streamRoomsPagingList: PagingList<StreamRoom> = streamApi.listStreamRooms(
        contextId,
        startIndex,
        pageSize,
        SortOrder.ASC,
        null,       // lastId
        null,       // sortBy
        null        // queryAsJson
    )

    val streamRooms = streamRoomsPagingList.readItems.map {
        StreamRoomItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

fun getStreamRoomById() {
    val streamRoomId = "STREAM_ROOM_ID"

    val streamRoomItem = streamApi.getStreamRoom(streamRoomId).let {
        StreamRoomItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

// END: Getting Stream Rooms


// START: Managing Stream Rooms

fun renamingStreamRoom() {
    val streamRoomId = "STREAM_ROOM_ID"
    val streamRoom: StreamRoom = streamApi.getStreamRoom(streamRoomId)

    val users = streamRoom.users.map { userId ->
        // Your application must provide a way
        // to get user's public key from their userId
        UserWithPubKey(userId, "USER_PUBLIC_KEY")
    }

    val managers = streamRoom.managers.map { userId ->
        UserWithPubKey(userId, "USER_PUBLIC_KEY")
    }

    val newStreamRoomNameAsPrivateMeta = "New stream room name"

    streamApi.updateStreamRoom(
        streamRoomId,
        users,
        managers,
        streamRoom.publicMeta,
        newStreamRoomNameAsPrivateMeta.encodeToByteArray(),
        streamRoom.version,
        false,  // force
        false,  // forceGenerateNewKey
        null    // policies
    )
}

fun removingUserFromStreamRoom() {
    val streamRoomId = "STREAM_ROOM_ID"
    val streamRoom: StreamRoom = streamApi.getStreamRoom(streamRoomId)
    val userToRemove = "USERID_TO_REMOVE"
    val newUsers = streamRoom
        .users
        .filter {
            it != userToRemove
        }.map { userId ->
            // Your application must provide a way,
            // to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }
    val managers = streamRoom
        .managers
        .map { userId ->
            // Your application must provide a way,
            // to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }

    streamApi.updateStreamRoom(
        streamRoomId,
        newUsers,
        managers,
        streamRoom.publicMeta,
        streamRoom.privateMeta,
        streamRoom.version,
        false,                  // force
        false,                  //forceGenerateNewKey
        null                    // policies
    )
}

fun deletingStreamRoom() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.deleteStreamRoom(streamRoomId)
}

// END: Managing Stream Rooms