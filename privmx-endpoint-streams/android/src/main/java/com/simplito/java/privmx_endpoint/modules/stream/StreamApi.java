package com.simplito.java.privmx_endpoint.modules.stream;


import static android.media.AudioManager.GET_DEVICES_OUTPUTS;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.DeviceType;
import com.simplito.java.privmx_endpoint.model.MediaDevice;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.stream.Settings;
import com.simplito.java.privmx_endpoint.model.stream.StreamHandle;
import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;
import com.simplito.java.privmx_endpoint.model.stream.StreamPublishResult;
import com.simplito.java.privmx_endpoint.model.stream.StreamRoom;
import com.simplito.java.privmx_endpoint.model.stream.StreamSubscription;
import com.simplito.java.privmx_endpoint.model.stream.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.stream.events.eventTypes.StreamEventType;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoSink;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;
import org.webrtc.audio.AudioDeviceModule;
import org.webrtc.audio.JavaAudioDeviceModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//TODO: Good to remove context from StreamApi
public class StreamApi {
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final String AUDIO_TRACK_ID = "ARDAMSa0";
    public static final String VIDEO_TRACK_TYPE = "video";
    private static final String TAG = "StreamApi";

    private final Context appContext;
    private final EglBase rootEglBase;
    private final StreamApiLow api;
    private final PeerConnectionManager pcManager;

    private static PeerConnectionFactory DefaultPeerConnectionFactory(
            Context appContext,
            EglBase eglBase,
            PeerConnectionFactory.Options options
    ) {
        AudioDeviceModule adm = JavaAudioDeviceModule
                .builder(appContext)
                .createAudioDeviceModule();

        boolean enableH264HighProfile = false;

        VideoEncoderFactory encoderFactory = new DefaultVideoEncoderFactory(
                eglBase.getEglBaseContext(),
                true,  /* enableIntelVp8Encoder */
                enableH264HighProfile
        );

        VideoDecoderFactory decoderFactory = new DefaultVideoDecoderFactory(
                eglBase.getEglBaseContext()
        );

        PeerConnectionFactory factory = PeerConnectionFactory.builder()
                .setVideoDecoderFactory(decoderFactory)
                .setVideoEncoderFactory(encoderFactory)
                .setOptions(options)
                .setAudioDeviceModule(adm)
                .createPeerConnectionFactory();

        adm.release();
        return factory;
    }

    public StreamApi(
            Context appContext,
            EglBase rootEglBase,
            StreamApiLow api
    ) {
        this(appContext, rootEglBase, api, null);
    }

    public StreamApi(
            @NonNull Context appContext,
            @NonNull EglBase rootEglBase,
            @NonNull StreamApiLow api,
            @Nullable PeerConnectionFactory pcFactory
    ) {
        this.appContext = appContext;
        this.rootEglBase = rootEglBase;
        this.api = api;
        PeerConnectionFactory factory = pcFactory;
        if (factory == null) {
            //TODO: What should be passed to the options parameter
            factory = DefaultPeerConnectionFactory(appContext, rootEglBase, new PeerConnectionFactory.Options());
        }
        pcManager = new PeerConnectionManager(
                factory,
                (sessionId, rtcConfiguration)->{
                    if(sessionId != null) {
                        this.api.trickle(sessionId, rtcConfiguration);
                    }
                });
    }

    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies
    ) {
        return api.createStreamRoom(contextId, users, managers, publicMeta, privateMeta, policies);
    }

    public void updateStreamRoom(
            String streamRoomId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            long version,
            boolean force,
            boolean forceGenerateNewKey,
            ContainerPolicy policies
    ) {
        api.updateStreamRoom(streamRoomId, users, managers, publicMeta, privateMeta, version, force, forceGenerateNewKey, policies);
    }

    public PagingList<StreamRoom> listStreamRooms(
            String contextId,
            long skip,
            long limit,
            String sortOrder,
            String lastId,
            String sortBy
    ) {
        return api.listStreamRooms(contextId, skip, limit, sortOrder, lastId, sortBy);
    }

    public StreamRoom getStreamRoom(String streamRoomId) {
        return api.getStreamRoom(streamRoomId);
    }

    public void deleteStreamRoom(String streamRoomId) {
        api.deleteStreamRoom(streamRoomId);
    }

    public List<StreamInfo> listStreams(String streamRoomId) {
        return api.listStreams(streamRoomId);
    }

    public void joinStreamRoom(
            String streamRoomId,
            TrackObserver trackObserver
    ) {
        RoomJanusSession session = pcManager.createSession(streamRoomId,trackObserver);
        api.joinStreamRoom(streamRoomId, session.webrtc);
    }

    public void leaveStreamRoom(String streamRoomId) {
        pcManager.leaveStreamRoom(streamRoomId);
        api.leaveStreamRoom(streamRoomId);
    }

    public StreamHandle createStream(String streamRoomId) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if(session == null) throw new IllegalStateException("Session to this room is not exsists. Call joinStreamRoom first");
        try {
            session.createPublisher();
        }catch (IllegalStateException e){
            throw new IllegalStateException("Publisher is now active, try use modifyRemoteStreamsSubscriptions");
        }

        StreamHandle handle = api.createStream(streamRoomId);
        pcManager.createHandleToRoom(handle, streamRoomId);
        return handle;
    }

    //
    public List<MediaDevice> getMediaDevices() {
        List<MediaDevice> result = new ArrayList<>();

        AudioManager audioManager = (AudioManager) appContext.getSystemService(Context.AUDIO_SERVICE);
        CameraEnumerator videoManager;

        android.media.AudioDeviceInfo[] audioDevices = audioManager.getDevices(GET_DEVICES_OUTPUTS);
        if (Camera2Enumerator.isSupported(appContext)) {
            videoManager = new Camera2Enumerator(appContext);
        } else {
            videoManager = new Camera1Enumerator(true);
        }
        String[] videoDevices = videoManager.getDeviceNames();

        audioManager.getActiveRecordingConfigurations().forEach(AudioRecordingConfiguration::getAudioDevice);

        List<MediaDevice> audio = Arrays.stream(audioDevices).map(it ->
                new MediaDevice(
                        it.getProductName().toString(),
                        String.valueOf(it.getId()),
                        DeviceType.Audio
                )
        ).collect(Collectors.toList());
        result.addAll(audio);

        List<MediaDevice> video = Arrays.stream(videoDevices).map(it ->
                new MediaDevice(
                        it,
                        it,                 // todo: what else can it be?
                        DeviceType.Audio
                )
        ).collect(Collectors.toList());
        result.addAll(video);

        return result;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Logging.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }


    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator, boolean isBackFacing) {
        final String[] deviceNames = enumerator.getDeviceNames();
        // First, try to find front facing camera
        Logging.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null && !isBackFacing) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Logging.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                Logging.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null && isBackFacing) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    /**
     * @param context
     * @param localSink
     * @param streamHandle
     * @param track
     * @throws IllegalStateException if call addTrack before call createStream
     */
    public void addTrack(
            Context context,
            VideoSink localSink,
            StreamHandle streamHandle,
            MediaDevice track
    ) throws IllegalStateException {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream not exists. Create stream first.");
        JanusPublisher connection = session.getPublisher();
        if (connection == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        switch (track.type) {
            case Audio: {
                AudioSource audioSource = connection.peerConnectionFactory.createAudioSource(new MediaConstraints());
                AudioTrack audioTrack = connection.peerConnectionFactory.createAudioTrack(track.name, audioSource);
                audioTrack.setVolume(10.0);
                connection.addAudioTrack(audioTrack);
                break;
            }

            case Video: {
                SurfaceTextureHelper surfaceTextureHelper =
                        SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
                VideoSource videoSource = connection.peerConnectionFactory.createVideoSource(false, false);        // todo - zaimplementowac caly capturer?
                VideoCapturer capturer = createCameraCapturer(new Camera2Enumerator(context));
                capturer.initialize(surfaceTextureHelper, appContext, videoSource.getCapturerObserver());
//                    capturer.startCapture(1920, 1080, 30);
                VideoTrack videoTrack = connection.peerConnectionFactory.createVideoTrack(track.name, videoSource);
                videoTrack.setEnabled(true);
                videoTrack.addSink(localSink);
                connection.addVideoTrack(videoTrack,capturer);
//                    if (Camera2Enumerator.isSupported(appContext)) {
//                        enumerator = new Camera2Enumerator(appContext);
//                    } else {
//                        enumerator = new Camera1Enumerator(true);
//                    }
//                    capturer = enumerator.createCapturer(track.name, null);

                capturer.startCapture(1280, 720, 30); // ???
                System.out.println("after start capturer");
                break;
            }
        }
    }

    public void addTrackAudio(
            AudioTrack audioTrack,
            StreamHandle streamHandle,
            MediaDevice track
    ) {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        JanusPublisher connection = session.getPublisher();
        if (session == null)
            throw new IllegalStateException("Stream not exists. Create stream first.");
        if (connection == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");

        if (track.type == DeviceType.Audio) {
            audioTrack.setEnabled(true);
            connection.addAudioTrack(audioTrack);
        }
    }

    public void addTrackVideo(
            Context context,
            VideoSink localSink,
            StreamHandle streamHandle,
            MediaDevice track,
            Boolean isScreenCast,
            VideoCapturer capturer,
            boolean isBackFacing
    ) {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        JanusPublisher connection = session.getPublisher();
        SurfaceTextureHelper surfaceTextureHelper;

        VideoCapturer currentCapturer;
        if (isScreenCast) {
            surfaceTextureHelper = SurfaceTextureHelper.create("ScreenCaptureThread", rootEglBase.getEglBaseContext());
            currentCapturer = capturer;
        } else {
            surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", rootEglBase.getEglBaseContext());
            currentCapturer = createCameraCapturer(new Camera2Enumerator(context), isBackFacing);
        }

        VideoSource videoSource = connection.peerConnectionFactory.createVideoSource(isScreenCast);
        currentCapturer.initialize(surfaceTextureHelper, appContext, videoSource.getCapturerObserver());

        VideoTrack videoTrack = connection.peerConnectionFactory.createVideoTrack(track.name, videoSource);
        videoTrack.setEnabled(true);
        videoTrack.addSink(localSink);
        connection.addVideoTrack(
                videoTrack,
                currentCapturer
        );

        DisplayMetrics metrics = new DisplayMetrics();
        Objects.requireNonNull(context.getDisplay()).getMetrics(metrics);

        int width = metrics.widthPixels;
        int height = metrics.heightPixels;

        currentCapturer.startCapture(width, height, 30);

    }

    /**
     * @param streamHandle
     * @param track
     * @throws IllegalStateException when Stream with this StreamHandle doesn't exist.
     */
    public void removeTrack(
            StreamHandle streamHandle,
            MediaStreamTrack track
    ) throws IllegalStateException {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream with this StreamHandle doesn't exist.");
        JanusPublisher publisher = session.getPublisher();
        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        if (track instanceof AudioTrack) {
            publisher.removeAudioTrack(track.id());
        } else if (track instanceof VideoTrack) {
            publisher.removeVideoTrack(track.id());
        }
    }

    public void removeTrack(
            StreamHandle streamHandle,
            MediaDevice track
    ) throws IllegalStateException {
        RoomJanusSession session = pcManager.getSession(streamHandle);
        if (session == null)
            throw new IllegalStateException("Stream with this StreamHandle doesn't exist.");
        JanusPublisher publisher = session.getPublisher();

        if (publisher == null)
            throw new IllegalStateException("This StreamHandle has not created companion publisher.");
        if (track.type == DeviceType.Audio) {
            publisher.removeAudioTrack(track.name);
        } else if (track.type == DeviceType.Video) {
            publisher.removeVideoTrack(track.name);
        }
    }

    public StreamPublishResult publishStream(StreamHandle streamHandle) {
        return api.publishStream(streamHandle);
    }

    public StreamPublishResult updateStream(StreamHandle streamHandle) {
        return api.updateStream(streamHandle);
    }

    public void unpublishStream(StreamHandle streamHandle) {
        api.unpublishStream(streamHandle);
    }

    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions
    ) {
        subscribeToRemoteStreams(streamRoomId, subscriptions, new Settings());
    }

    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions,
            Settings options
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if (session == null)
            throw new IllegalStateException("No active session to this Stream Room. Join stream room first");
        try {
            session.createSubscriber();
        }catch (IllegalStateException e){
            throw new IllegalStateException("Subscriber is now active, try use modifyRemoteStreamsSubscriptions");
        }
        api.subscribeToRemoteStreams(streamRoomId, subscriptions, options);
    }

    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove
    ) {
        modifyRemoteStreamsSubscriptions(
                streamRoomId,
                subscriptionsToAdd,
                subscriptionsToRemove,
                new Settings()
        );
    }

    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove,
            Settings options
    ) {
        api.modifyRemoteStreamsSubscriptions(
                streamRoomId,
                subscriptionsToAdd,
                subscriptionsToRemove,
                options
        );
    }

    public void unsubscribeFromRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToRemove
    ) {
        api.unsubscribeFromRemoteStreams(
                streamRoomId,
                subscriptionsToRemove
        );
    }

    public void dropBrokenFrames(
            String streamRoomId,
            boolean enable
    ) {
        RoomJanusSession session = pcManager.getSession(streamRoomId);
        if(session != null){
            PmxFrameCryptor.PmxFrameCryptorOptions options = new PmxFrameCryptor.PmxFrameCryptorOptions();
            options.dropFrameIfCryptionFailed = enable;
            session.setFrameCryptorOptions(options);
        }
    }

    public List<String> subscribeFor(List<String> subscriptionQueries) {
        return api.subscribeFor(subscriptionQueries);
    }

    public void unsubscribeFrom(List<String> subscriptionIds) {
        api.unsubscribeFrom(subscriptionIds);
    }

    public String buildSubscriptionQuery(
            StreamEventType eventType,
            StreamEventSelectorType selectorType,
            String selectorId
    ) {
        return api.buildSubscriptionQuery(
                eventType,
                selectorType,
                selectorId
        );
    }
}
