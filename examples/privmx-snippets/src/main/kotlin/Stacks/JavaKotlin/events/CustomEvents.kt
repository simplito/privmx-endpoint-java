package Stacks.JavaKotlin.events

import Stacks.JavaKotlin.endpointSession
import Stacks.JavaKotlin.user1Id
import Stacks.JavaKotlin.user1PublicKey
import Stacks.JavaKotlin.user2Id
import Stacks.JavaKotlin.user2PublicKey
import com.simplito.java.privmx_endpoint.model.UserWithPubKey
import com.simplito.java.privmx_endpoint_extra.events.EventType

fun emittingCustomEvents() {
    val contextId = "CONTEXT_ID"
    val channelName = "CHANNEL_NAME"

    val users: List<UserWithPubKey> = listOf(
        UserWithPubKey(user1Id, user1PublicKey), UserWithPubKey(user2Id, user2PublicKey)
    )
    val eventData = "Custom Event Data"

    endpointSession.eventApi.emitEvent(
        contextId, users, channelName, eventData.encodeToByteArray()
    )
}

fun handlingCustomEvents() {
    val callbacksId = "CALLBACKS_ID"
    val contextId = "CONTEXT_ID"
    val channelName = "CHANNEL_NAME"

    endpointSession.registerCallback(
        callbacksId, EventType.ContextCustomEvent(contextId, channelName)
    ) { customEventData ->
        // some actions when custom event arrives
    }
}