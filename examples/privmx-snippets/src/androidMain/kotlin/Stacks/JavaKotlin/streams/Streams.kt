package Stacks.JavaKotlin.streams

import com.simplito.java.privmx_endpoint.model.stream.StreamHandle
import com.simplito.java.privmx_endpoint.model.stream.StreamInfo
import com.simplito.java.privmx_endpoint.model.stream.StreamSubscription
import com.simplito.java.privmx_endpoint.modules.stream.TrackObserver
import org.webrtc.AudioTrack
import org.webrtc.MediaStreamTrack
import org.webrtc.VideoTrack

// START: Listing and Filtering Streams snippets

fun listStreams() {
    val streamRoomId = "STREAM_ROOM_ID"

    val streams: List<StreamInfo> = streamApi.listStreams(streamRoomId)
}

fun listStreamsWithVideo() {
    val streamRoomId = "STREAM_ROOM_ID"

    val streamsWithVideo = streamApi.listStreams(streamRoomId)
        .filter { stream ->
            stream.tracks.any { it.type == "video" }
        }
}

fun listStreamsWithAudio() {
    val streamRoomId = "STREAM_ROOM_ID"

    val streamsWithAudio = streamApi.listStreams(streamRoomId)
        .filter { stream ->
            stream.tracks.any { it.type == "audio" }
        }
}

fun listStreamsByUser() {
    val streamRoomId = "STREAM_ROOM_ID"
    val userId = "USER_ID"

    val userStreams = streamApi.listStreams(streamRoomId)
        .filter { it.userId == userId }
}

fun listRealStreams() {
    val streamRoomId = "STREAM_ROOM_ID"

    val realStreams = streamApi.listStreams(streamRoomId)
        .filter { it.dummy != true }
}

fun observingAllTracks() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    streamApi.setTrackObserver(streamRoomId, object : TrackObserver {
        override fun OnRemoteTrack(streamId: String?, track: MediaStreamTrack?) {
            // handle remote tracks
        }
    })
}

fun observingSpecificStreamTracks() {
    val streamRoomId = "STREAM_ROOM_ID"
    val streamId = "STREAM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    streamApi.setTrackObserver(streamRoomId, object : TrackObserver {
        override fun OnRemoteTrack(streamId: String?, track: MediaStreamTrack?) {
            // handle remote tracks
        }
    }, streamId)
}


// START: Publishing Streams snippets

fun publishingStream() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    val streamHandle: StreamHandle = streamApi.createStream(streamRoomId)
    streamApi.publishStream(streamHandle)
}

fun updatingStream(streamHandle: StreamHandle) {
    streamApi.updateStream(streamHandle)
}

fun unpublishingStream(streamHandle: StreamHandle) {
    streamApi.unpublishStream(streamHandle)
}


// START: Managing Tracks snippets

fun addingVideoTrack(videoTrack: VideoTrack) {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    val streamHandle: StreamHandle = streamApi.createStream(streamRoomId)
    streamApi.addTrack(streamHandle, videoTrack)
    streamApi.publishStream(streamHandle)
}

fun addingAudioTrack(audioTrack: AudioTrack) {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    val streamHandle: StreamHandle = streamApi.createStream(streamRoomId)
    streamApi.addTrack(streamHandle, audioTrack)
    streamApi.publishStream(streamHandle)
}

fun removingTrack(streamHandle: StreamHandle, audioTrack: AudioTrack) {
    streamApi.removeTrack(streamHandle, audioTrack)
    streamApi.updateStream(streamHandle)
}


// START: Subscribing to Remote Streams snippets

fun subscribingToAllRemoteStreams() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
    val subscriptions = streamApi.listStreams(streamRoomId)
        .flatMap { stream ->
            stream.tracks.map { track ->
                StreamSubscription(
                    stream.id,
                    track.mid
                )
            }
        }.toList()

    streamApi.subscribeToRemoteStreams(streamRoomId, subscriptions)
}

fun subscribingToUserRemoteStreams() {
    val streamRoomId = "STREAM_ROOM_ID"
    val userId = "USER_ID"

    streamApi.joinStreamRoom(streamRoomId)
    val subscriptions = streamApi.listStreams(streamRoomId)
        .filter { it.userId == userId }
        .flatMap { stream ->
            stream.tracks.map { track ->
                StreamSubscription(
                    stream.id,
                    track.mid
                )
            }
        }.toList()

    streamApi.subscribeToRemoteStreams(streamRoomId, subscriptions)
}

fun unsubscribingFromRemoteStreams(subscriptionsToRemove: List<StreamSubscription>) {
    val streamRoomId = "STREAM_ROOM_ID"
    streamApi.unsubscribeFromRemoteStreams(streamRoomId, subscriptionsToRemove)
}