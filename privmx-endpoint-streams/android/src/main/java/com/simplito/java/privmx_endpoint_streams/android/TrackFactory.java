//
// PrivMX Endpoint Streams Java Android.
// Copyright © 2026 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_streams.android;

import org.webrtc.*;

public class TrackFactory {
    private final PeerConnectionFactory factory;
    TrackFactory(PeerConnectionManager pcManager){
        factory = pcManager.pcFactory;
    }

    public VideoSource createVideoSource(boolean isScreenCast){
        return factory.createVideoSource(isScreenCast);
    }

    public VideoSource createVideoSource(boolean isScreenCast, boolean alignTimestamps){
        return factory.createVideoSource(isScreenCast,alignTimestamps);
    }

    public AudioSource createAudioSource(){
        return factory.createAudioSource(new MediaConstraints());
    }

    public VideoTrack createVideoTrack(
            String id,
            VideoSource videoSource
    ){
        return factory.createVideoTrack(id,videoSource);
    }

    public AudioTrack createAudioTrack(
            String id,
            AudioSource audioSource
    ){
        return factory.createAudioTrack(id,audioSource);
    }

}