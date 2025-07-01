package Stacks.JavaKotlin.kvdb

import Stacks.JavaKotlin.contextId
import Stacks.JavaKotlin.endpointSession
import Stacks.JavaKotlin.user1Id
import Stacks.JavaKotlin.user1PublicKey
import Stacks.JavaKotlin.user2Id
import Stacks.JavaKotlin.user2PublicKey
import com.simplito.java.privmx_endpoint.model.Kvdb
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint.modules.kvdb.KvdbApi
import com.simplito.java.privmx_endpoint_extra.model.SortOrder
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class KvdbPublicMeta(val tags: List<String>)

lateinit var kvdbApi: KvdbApi

fun setKvdbApi() {
    val kvdbApi = endpointSession.kvdbApi
}

// START: Creating kvdb snippets

fun createKvdbBasic() {
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val publicMeta = ByteArray(0)
    val privateMeta = ByteArray(0)

    val kvdbId = kvdbApi.createKvdb(
        contextId,
        users,
        managers,
        publicMeta,
        privateMeta
    )
}

fun createKvdbWithName() {
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val kvdbNameAsPrivateMeta = "New kvdb"
    val publicMeta = ByteArray(0)

    val kvdbId = kvdbApi.createKvdb(
        contextId,
        users,
        managers,
        publicMeta,
        kvdbNameAsPrivateMeta.encodeToByteArray()
    )
}

fun createKvdbWithPublicMeta() {
    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey),
        UserWithPubKey(user2Id, user2PublicKey)
    )
    val managers: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey)
    )
    val kvdbNameAsPrivateMeta = "New kvdb"
    val publicMeta = KvdbPublicMeta(
        listOf("TAG1", "TAG2", "TAG3")
    )

    val kvdbId = kvdbApi.createKvdb(
        contextId,
        users,
        managers,
        Json.encodeToString(publicMeta).encodeToByteArray(),
        kvdbNameAsPrivateMeta.encodeToByteArray()
    )
}

// END: Creating kvdb snippets


// START: Getting kvdb snippets

data class KvdbItem(
    val kvdb: Kvdb,
    val decodedPrivateMeta: String,
    val decodedPublicMeta: KvdbPublicMeta
)

fun getMostRecentKvdbs() {
    val startIndex = 0L
    val pageSize = 100L

    val kvdbsPagingList = kvdbApi.listKvdbs(
        contextId,
        startIndex,
        pageSize,
        SortOrder.DESC
    )
    val kvdbs = kvdbsPagingList.readItems.map {
        KvdbItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

fun getOldestKvdbs() {
    val startIndex = 0L
    val pageSize = 100L

    val kvdbsPagingList = kvdbApi.listKvdbs(
        contextId,
        startIndex,
        pageSize,
        SortOrder.ASC
    )
    val kvdbs = kvdbsPagingList.readItems.map {
        KvdbItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

fun getKvdbById() {
    val kvdbId = "KVDB_ID"
    val kvdbItem = kvdbApi.getKvdb(kvdbId).let {
        KvdbItem(
            it,
            it.privateMeta.decodeToString(),
            Json.decodeFromString(it.publicMeta.decodeToString())
        )
    }
}

// END: Getting kvdb snippets


// START: Managing kvdb snippets

fun renamingKvdb() {
    val kvdbID = "KVDB_ID"
    val kvdb: Kvdb = kvdbApi.getKvdb(kvdbID)
    val users = kvdb
        .users
        .map { userId ->
            //Your application must provide a way,
            //to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }
    val managers = kvdb
        .managers
        .map { userId ->
            //Your application must provide a way,
            //to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }
    val newKvdbNameAsPrivateMeta = "New kvdb name"

    kvdbApi.updateKvdb(
        kvdb.kvdbId,
        users,
        managers,
        kvdb.publicMeta,
        newKvdbNameAsPrivateMeta.encodeToByteArray(),
        kvdb.version,
        false
    )
}

fun removingUser() {
    val kvdbID = "THREAD_ID"
    val kvdb: Kvdb = kvdbApi.getKvdb(kvdbID)
    val userToRemove = "USER_ID_TO_REMOVE"
    val newUsers = kvdb
        .users
        .filter {
            it != userToRemove
        }.map { userId ->
            //Your application must provide a way,
            //to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }
    val managers = kvdb
        .managers
        .map { userId ->
            //Your application must provide a way,
            //to get user's public key from their userId.
            UserWithPubKey(
                userId,
                "USER_PUBLIC_KEY"
            )
        }

    kvdbApi.updateKvdb(
        kvdb.kvdbId,
        newUsers,
        managers,
        kvdb.publicMeta,
        kvdb.privateMeta,
        kvdb.version,
        false
    )
}

fun deletingKvdb() {
    val kvdbId = "KVDB_ID"
    kvdbApi.deleteKvdb(kvdbId)
}

// END: Managing kvdb snippets