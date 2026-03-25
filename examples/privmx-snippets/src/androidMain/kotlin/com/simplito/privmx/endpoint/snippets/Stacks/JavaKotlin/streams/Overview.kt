package com.simplito.privmx.endpoint.snippets.Stacks.JavaKotlin.streams

import Stacks.JavaKotlin.endpointSession
import Stacks.JavaKotlin.stores.StorePublicMeta
import android.content.Context
import com.simplito.java.privmx_endpoint.model.stream.StreamRoom
import com.simplito.java.privmx_endpoint.modules.stream.StreamApi
import com.simplito.java.privmx_endpoint.modules.stream.StreamApiLow
import org.webrtc.EglBase
import org.webrtc.PeerConnection

lateinit var streamApiLow: StreamApiLow
lateinit var streamApi: StreamApi

data class StreamRoomItem(
    val streamRoom: StreamRoom,
    val decodedPrivateMeta: String,
    val decodedPublicMeta: StorePublicMeta
)

fun initializeStreamApi(context: Context) {

    // Create EGL context (required for video rendering)
    val eglBase: EglBase = EglBase.create()
    val appContext = context.applicationContext

    // Initialize low-level dependency
    streamApiLow = endpointSession.initializeStreamApiLow()

    // Create commonMain streaming API
    streamApi = StreamApi(appContext, eglBase, streamApiLow)
}


fun observingConnectionState() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    streamApi.setConnectionStateObserver(streamRoomId) { state ->
        when (state) {
            PeerConnection.IceConnectionState.CONNECTED -> {
                // handle connected
            }

            PeerConnection.IceConnectionState.DISCONNECTED -> {
                // handle disconnected
            }

            PeerConnection.IceConnectionState.FAILED -> {
                // handle failed
            }

            else -> {}
        }
    }
}