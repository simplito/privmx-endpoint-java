package com.simplito.java.privmx_endpoint.modules.stream;

import org.webrtc.*;

/**
 * Factory for creating WebRTC media sources and tracks.
 * Provides methods for creating the audio and video objects needed
 * to publish a stream via {@link StreamApi}.
 * You do not create instances of this class directly — obtain the shared instance
 * from {@link StreamApi#trackFactory} after constructing a {@link StreamApi}.
 */
public class TrackFactory {
    private final PeerConnectionFactory factory;

    TrackFactory(PeerConnectionManager pcManager) {
        factory = pcManager.pcFactory;
    }

    /**
     * Creates a {@link VideoSource} that can capture a camera or screen with alignTimestamps to {@code true}.
     *
     * @param isScreenCast {@code true} if the source is capturing a screen share;
     *                     {@code false} for a regular camera
     * @return a new {@link VideoSource} instance
     */
    public VideoSource createVideoSource(boolean isScreenCast) {
        return factory.createVideoSource(isScreenCast);
    }

    /**
     * Creates a {@link VideoSource} with explicit timestamp alignment control.
     *
     * @param isScreenCast    {@code true} if the source is capturing a screen share;
     *                        {@code false} for a regular camera
     * @param alignTimestamps if {@code} false - the caller is responsible for aligning
     *                        frame timestamps to {@code rtc::TimeNanos()} — useful for
     *                        higher accuracy when there is a significant delay between
     *                        frame creation and delivery; if {@code true}, timestamps
     *                        are automatically aligned to {@code rtc::TimeNanos()}
     *                        upon arrival at the returned video source
     * @return a new {@link VideoSource} instance
     */
    public VideoSource createVideoSource(boolean isScreenCast, boolean alignTimestamps) {
        return factory.createVideoSource(isScreenCast, alignTimestamps);
    }

    /**
     * Creates an {@link AudioSource} with default media constraints.
     * The source captures audio from the device microphone. Default constraints
     * enable standard WebRTC audio processing (echo cancellation, noise suppression,
     * auto gain control).
     *
     * @return a new {@link AudioSource} instance
     */
    public AudioSource createAudioSource() {
        return factory.createAudioSource(new MediaConstraints());
    }

    /**
     * Creates a {@link VideoTrack} using the provided {@link VideoSource}.
     *
     * @param id          unique identifier for this track within the peer connection
     * @param videoSource source of video frames
     * @return a new {@link VideoTrack} instance
     */
    public VideoTrack createVideoTrack(
            String id,
            VideoSource videoSource
    ) {
        return factory.createVideoTrack(id, videoSource);
    }

    /**
     * Creates an {@link AudioTrack} using the provided {@link AudioSource}.
     *
     * @param id          unique identifier for this track within the peer connection
     * @param audioSource source providing audio samples
     * @return a new {@link AudioTrack} instance
     */
    public AudioTrack createAudioTrack(
            String id,
            AudioSource audioSource
    ) {
        return factory.createAudioTrack(id, audioSource);
    }

}