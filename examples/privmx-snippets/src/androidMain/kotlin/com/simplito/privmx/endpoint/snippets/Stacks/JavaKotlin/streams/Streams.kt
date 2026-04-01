package com.simplito.privmx.endpoint.snippets.Stacks.JavaKotlin.streams

import com.simplito.java.privmx_endpoint.model.stream.StreamHandle
import com.simplito.java.privmx_endpoint.model.stream.StreamInfo
import com.simplito.java.privmx_endpoint.model.stream.StreamPublishResult
import com.simplito.java.privmx_endpoint.model.stream.StreamSubscription
import com.simplito.java.privmx_endpoint.modules.stream.TrackObserver
import org.webrtc.AudioTrack
import org.webrtc.MediaStreamTrack
import org.webrtc.VideoTrack


// START: Join and Leave Stream Room

fun joiningStreamRoom() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.joinStreamRoom(streamRoomId)
}

fun leavingStreamRoom() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.leaveStreamRoom(streamRoomId)
}

// END: Join and Leave Stream Room


// START: Create Stream

fun creatingStream(): StreamHandle {
    val streamRoomId = "STREAM_ROOM_ID"

    // 1. Create tracks
    // Create sources from device input
    val videoSource = streamApi.trackFactory.createVideoSource(false /* isScreenCast */)
    val audioSource = streamApi.trackFactory.createAudioSource()

    // Wrap in tracks
    val videoTrack = streamApi.trackFactory.createVideoTrack("video0", videoSource)
    val audioTrack = streamApi.trackFactory.createAudioTrack("audio0", audioSource)


    // 2. Create stream and attach tracks
    val streamHandle: StreamHandle = streamApi.createStream(streamRoomId)

    streamApi.addTrack(streamHandle, videoTrack)
    streamApi.addTrack(streamHandle, audioTrack)

    return streamHandle
}
// END: Create Stream


// START: Publish Stream

fun publishingStream(streamHandle: StreamHandle) {
    val streamPublishResult: StreamPublishResult = streamApi.publishStream(streamHandle)
}

// END: Publish Stream


// START: Modify Streams

fun updatingStream(streamHandle: StreamHandle) {
   val streamPublishResult: StreamPublishResult = streamApi.updateStream(streamHandle)
}

// END: Modify Streams


// START: Unpublish Stream

fun unpublishingStream(streamHandle: StreamHandle) {
    streamApi.unpublishStream(streamHandle)
}

// END: Unpublish Stream


// START: Manage Media Tracks

fun createVideoTrack(): VideoTrack {
    // Create source from device input
    val videoSource = streamApi.trackFactory.createVideoSource(false /* isScreenCast */)

    // Wrap in track
    return streamApi.trackFactory.createVideoTrack("video0", videoSource)
}

fun createAudioTrack(): AudioTrack {
    // Create source from device input
    val audioSource = streamApi.trackFactory.createAudioSource()

    // Wrap in track
    return streamApi.trackFactory.createAudioTrack("audio0", audioSource)
}

fun removeAudioTrack(streamHandle: StreamHandle, audioTrack: MediaStreamTrack) {
    streamApi.removeTrack(streamHandle, audioTrack)
}

fun removeVideoTrack(streamHandle: StreamHandle, videoTrack: VideoTrack) {
    streamApi.removeTrack(streamHandle, videoTrack)
}

fun observingAllTracks() {
    val streamRoomId = "STREAM_ROOM_ID"

    streamApi.setTrackObserver(streamRoomId, object : TrackObserver {
        override fun OnRemoteTrack(streamId: String?, track: MediaStreamTrack?) {
            // handle remote tracks
        }
    })
}

fun observingSpecificStreamTracks() {
    val streamRoomId = "STREAM_ROOM_ID"
    val streamId = "STREAM_ID"

    streamApi.setTrackObserver(
        streamRoomId, object : TrackObserver {
            override fun OnRemoteTrack(streamId: String?, track: MediaStreamTrack?) {
                // handle remote tracks
            }
        },
        streamId
    )
}
// END: Manage Media Tracks


// START: List Streams

fun listStreams() {
    val streamRoomId = "STREAM_ROOM_ID"

    val streams: List<StreamInfo> = streamApi.listStreams(streamRoomId)
}

// END: List Streams


// START: Manage Remote Streams Subscriptions

fun subscribingToAllRemoteStreams() : List<StreamSubscription> {
    val streamRoomId = "STREAM_ROOM_ID"

    val subscriptions: List<StreamSubscription> = streamApi.listStreams(streamRoomId)
        .flatMap { stream ->
            stream.tracks.map { track ->
                StreamSubscription(
                    stream.id,
                    track.mid
                )
            }
        }.toList()

    streamApi.subscribeToRemoteStreams(streamRoomId, subscriptions)

    return subscriptions
}

fun subscribingToUserRemoteStreams(): List<StreamSubscription>  {
    val streamRoomId = "STREAM_ROOM_ID"
    val userId = "USER_ID"

    val subscriptions: List<StreamSubscription> = streamApi.listStreams(streamRoomId)
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

    return subscriptions
}

fun unsubscribingFromRemoteStreams(subscriptionsToRemove: List<StreamSubscription>) {
    val streamRoomId = "STREAM_ROOM_ID"

    // unsubscribe from previously tracked remote subscriptions
    streamApi.unsubscribeFromRemoteStreams(streamRoomId, subscriptionsToRemove)
}

fun modifySRemoteSubscriptions(previousSubscriptions: List<StreamSubscription>) {
    val streamRoomId = "STREAM_ROOM_ID"

    // Fetch the latest available streams and map them to subscriptions
    val currentSubscriptions: List<StreamSubscription> = streamApi.listStreams(streamRoomId)
        .flatMap { stream ->
            stream.tracks.map { track ->
                StreamSubscription(
                    stream.id,
                    track.mid
                )
            }
        }.toList()

    // Calculate differences between previous and current subscriptions
    // new tracks that were not subscribed before
    val subscriptionsToAdd = currentSubscriptions - previousSubscriptions;

    // tracks that are no longer available
    val subscriptionsToRemove = previousSubscriptions - currentSubscriptions;

    streamApi.modifyRemoteStreamsSubscriptions(
        streamRoomId,
        subscriptionsToAdd,
        subscriptionsToRemove
    )
}

// END: Manage Remote Streams Subscriptions
