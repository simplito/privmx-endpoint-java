package com.simplito.java.privmx_endpoint.modules.stream;


import static android.media.AudioManager.GET_DEVICES_OUTPUTS;

import android.content.Context;
import android.media.AudioManager;
import android.media.AudioRecordingConfiguration;

import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.AudioTrackInfo;
import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.DeviceType;
import com.simplito.java.privmx_endpoint.model.JanusConnection;
import com.simplito.java.privmx_endpoint.model.Key;
import com.simplito.java.privmx_endpoint.model.KeyType;
import com.simplito.java.privmx_endpoint.model.MediaDevice;
import com.simplito.java.privmx_endpoint.model.OnFrameCallback;
import com.simplito.java.privmx_endpoint.model.PagingList;
import com.simplito.java.privmx_endpoint.model.PcObserver;
import com.simplito.java.privmx_endpoint.model.PeerConnection2;
import com.simplito.java.privmx_endpoint.model.PeerConnectionManager;
import com.simplito.java.privmx_endpoint.model.StreamHandle;
import com.simplito.java.privmx_endpoint.model.StreamInfo;
import com.simplito.java.privmx_endpoint.model.StreamPublishResult;
import com.simplito.java.privmx_endpoint.model.StreamRoom;
import com.simplito.java.privmx_endpoint.model.StreamSettings;
import com.simplito.java.privmx_endpoint.model.StreamStatus;
import com.simplito.java.privmx_endpoint.model.StreamSubscription;
import com.simplito.java.privmx_endpoint.model.UserWithPubKey;
import com.simplito.java.privmx_endpoint.model.VideoTrackInfo;
import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.StreamEventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.StreamEventType;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.EglBase;
import org.webrtc.FrameCryptorKeyProvider;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.RtpSender;
import org.webrtc.SessionDescription;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class StreamApi {
    public static final String VIDEO_TRACK_ID = "ARDAMSv0";
    public static final String AUDIO_TRACK_ID = "ARDAMSa0";
    public static final String VIDEO_TRACK_TYPE = "video";
    private static final String TAG = "StreamApi";

    public class TrackParams {
        public String params_JSON;
    }

    public class StreamJoinSettings {

    }

    public interface TrackObserver {
        void onTrack(MediaStreamTrack track);
    }


    private final Context appContext;
    private final EglBase rootEglBase;
    //    private final Connection connection;
    private final StreamApiLow api;

    @Nullable
    private PeerConnectionFactory peerConnectionFactory;
    private FrameCryptorKeyProvider frameCryptorKeyProvider;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    public StreamApi(
            Context appContext,
            EglBase rootEglBase,
            StreamApiLow api,
            @Nullable PeerConnectionFactory peerConnectionFactory
    ) {
        this.appContext = appContext;
        this.rootEglBase = rootEglBase;
        this.api = api;
        this.peerConnectionFactory = peerConnectionFactory;
    }

    private class WebRTCImpl implements WebRTCInterface {

        private PmxKeyStore store = PmxFrameCryptorFactory.createPmxKeyStore();
        private PeerConnection peerConnection;
        private TrackObserver trackObserver;
        public PcObserver pcObserver;
        PeerConnectionManager peerConnectionManager;


        // --------------------------------------------------------------
        class SdpObserver implements org.webrtc.SdpObserver {
            private CompletableFuture<String> res;

            SdpObserver(CompletableFuture<String> res) {
                this.res = res;
            }

            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                executor.execute(() -> {
                    peerConnection.setLocalDescription(this, sessionDescription);
                });
                res.complete(sessionDescription.description);
            }

            @Override
            public void onSetSuccess() {
            }

            @Override
            public void onCreateFailure(String s) {
            }

            @Override
            public void onSetFailure(String s) {
            }
        }

        // --------------------------------------------------------------
        class SdpObserver2 implements org.webrtc.SdpObserver {
            private CompletableFuture<String> res;

            SdpObserver2(CompletableFuture<String> res) {
                this.res = res;
            }

            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                executor.execute(() -> {
                    peerConnection.setLocalDescription(new SdpObserver(null), sessionDescription);
                });
                res.complete(sessionDescription.description);
            }

            @Override
            public void onSetSuccess() {
                executor.execute(() -> {
                    peerConnection.createAnswer(this, new MediaConstraints());
                });
            }

            @Override
            public void onCreateFailure(String s) {
            }

            @Override
            public void onSetFailure(String s) {
            }
        }


        // --------------------------------------------------------------
        // --------------------------------------------------------------


        WebRTCImpl(TrackObserver trackObserver) {
            this.trackObserver = trackObserver;
        }

        PeerConnection createPeerConnection(String streamRoomId) {
            executor.execute(() -> {
                // TODO: get ice servers from Bridge using getTurnCredentials()
                PeerConnection2 peerConnection;
                ArrayList<PeerConnection.IceServer> iceServers = new ArrayList<>();
                PeerConnection.RTCConfiguration rtcConfig = new PeerConnection.RTCConfiguration(iceServers);
                assert peerConnectionFactory != null;
                PeerConnection tmpPeerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, pcObserver);

                // todo - store twice ??
                peerConnection = new PeerConnection2(
                        tmpPeerConnection,
                        new PcObserver(
                                peerConnectionFactory,
                                streamRoomId,
                                store,
                                null
                        ),
                        store
                );

                peerConnection.pc.createNativePeerConnectionObserver(peerConnection.observer);      // todo - nie powinno sie samo robic w konstruktorze?
            });


            // gdzies przechowywac?
            return peerConnection;
        }

        // addAudio/Video

        @Override
        public String createOfferAndSetLocalDescription(String streamRoomId) {
            CompletableFuture<String> res = new CompletableFuture<>();
            executor.execute(() -> {
                PeerConnection pc = peerConnectionManager.getConnectionWithSession(
                        streamRoomId,
                        ConnectionType.Publisher
                ).peerConnection.pc;

                pc.createOffer(
                        new SdpObserver(res),
                        new MediaConstraints()
                );
            });
            try {
                return res.get();
            } catch (ExecutionException | InterruptedException ignored) {
            }
            return "";
        }

        @Override
        public String createAnswerAndSetDescriptions(String streamRoomId, String sdp, String type) {
            CompletableFuture<String> res = new CompletableFuture<>();
            executor.execute(() -> {
                PeerConnection pc = peerConnectionManager.getConnectionWithSession(
                        streamRoomId,
                        ConnectionType.Subscriber
                ).peerConnection.pc;

                // todo - idk czy type dobrze
                pc.setRemoteDescription(
                        new SdpObserver2(res),
                        new SessionDescription(
                                SessionDescription.Type.fromCanonicalForm(type),
                                sdp
                        )
                );
            });
            try {
                return res.get();
            } catch (ExecutionException | InterruptedException ignored) {
            }
            return "";
        }

        @Override
        public void setAnswerAndSetRemoteDescription(String streamRoomId, String sdp, String type) {
            executor.execute(() -> {
                peerConnection.setRemoteDescription(
                        new SdpObserver(null),
                        new SessionDescription(
                                SessionDescription.Type.fromCanonicalForm(type),
                                sdp
                        )
                );
            });
        }

        @Override
        public void close(String streamRoomId) {
            executor.execute(() -> {
                if (peerConnection == null) return;
                JanusConnection connection = peerConnectionManager.getConnectionWithSession(
                        streamRoomId,
                        ConnectionType.Subscriber
                );
                connection.peerConnection.audioTracks.clear();
                connection.peerConnection.videoTracks.clear();
                connection.peerConnection.pc.close();
            });
        }

        @Override
        public void updateKeys(String streamRoomId, List<Key> keys) {
            // todo - check with c++
            executor.execute(() -> {
                ArrayList<PmxKeyStore.Key> list = new ArrayList<>();
                for (Key key : keys) {
                    list.add(new PmxKeyStore.Key(key.keyId, key.key, key.type == KeyType.LOCAL ? PmxKeyStore.KeyType.LOCAL : PmxKeyStore.KeyType.REMOTE));
                }
                store.setKeys(list);
            });
        }

        @Override
        public void updateSessionId(String streamRoomId, Long sessionId, String connectionType) {
            switch (connectionType) {
                case "subscriber":
                    peerConnectionManager.updateSessionForConnection(
                            streamRoomId,
                            ConnectionType.Subscriber,
                            sessionId
                    );
                    break;

                case "publisher":
                    peerConnectionManager.updateSessionForConnection(
                            streamRoomId,
                            ConnectionType.Publisher,
                            sessionId
                    );
                    break;
            }
        }

        public void addAudioTrack(
                String streamRoomId,
                org.webrtc.AudioTrack audioTrack,
                String id
        ) {
            if (peerConnectionFactory != null) {
                RtpSender rtpSender = peerConnection.addTrack(audioTrack);
                PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                        peerConnectionFactory,
                        rtpSender,
                        store
                        // options ?
                );
                peerConnectionManager
                        .getConnectionWithSession(streamRoomId, ConnectionType.Publisher)
                        .peerConnection
                        .addAudioTrack(
                                id,
                                new AudioTrackInfo(
                                        audioTrack,
                                        rtpSender,
                                        frameCryptor
                                )
                        );
            }
        }

        public void addVideoTrack(
                String streamRoomId,
                org.webrtc.VideoTrack videoTrack,
                String id
        ) {
            if (peerConnectionFactory != null) {
                RtpSender rtpSender = peerConnection.addTrack(videoTrack);
                PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                        peerConnectionFactory,
                        rtpSender,
                        store
                        // options ?
                );
                peerConnectionManager
                        .getConnectionWithSession(streamRoomId, ConnectionType.Publisher)
                        .peerConnection
                        .addVideoTrack(
                                id,
                                new VideoTrackInfo(
                                        videoTrack,
                                        rtpSender,
                                        frameCryptor
                                )
                        );
            }
        }

        public void removeAudioTrack(
                String streamRoomId,
                String id
        ) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Publisher)
                    .peerConnection
                    .removeAudioTrack(id);
        }

        public void removeVideoTrack(
                String streamRoomId,
                String id
        ) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Publisher)
                    .peerConnection
                    .removeVideoTrack(id);
        }

        // TODO !!
        public void setFrameCryptorOptions(
                String streamRoomId,
                PmxFrameCryptor.PmxFrameCryptorOptions options
        ) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Subscriber)
                    .peerConnection
                    .setFrameCryptorOptions(options);
        }

        public void setOnFrame(
                String streamRoomId,
                OnFrameCallback onFrame
        ) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Subscriber)
                    .peerConnection
                    .observer
                    .setOnFrame(onFrame);
        }

        public void setOnRemoveVideoTrack(String streamRoomId, Consumer<String> onVideoRemove) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Subscriber)
                    .peerConnection
                    .observer
                    .setOnRemoveVideoTrack(onVideoRemove);
        }

        public void setOnVideoTrack(
                String streamRoomId,
                Consumer<String> onVideoTrack
        ) {
            peerConnectionManager
                    .getConnectionWithSession(streamRoomId, ConnectionType.Subscriber)
                    .peerConnection
                    .observer
                    .setOnVideoTrack(onVideoTrack);
        }

    }

    // streamHandle  -  trackObserver  -  StreamRoomId
    private class StreamData {
        public long streamHandle;
        public String streamRoomId;
        StreamStatus status;
        Map<String, VideoCapturer> streamCapturers;     // different from c++
        @Nullable
        public WebRTCImpl webRTC;
    }

    class StreamMap {
        private Map<Long, StreamData> map = new HashMap<>();
        private long currentId = 1;

        //        StreamData get(long streamId) {
        StreamData get(long streamId) {
            synchronized (map) {
                return map.get(streamId);
            }
        }

        public long getRandomHandle() {
            Random random = new Random(1024L);
            long h = random.nextLong();
            while (map.containsKey(h)) {
                h = random.nextLong();
            }
            return h;
        }

        StreamData create(
                TrackObserver trackObserver
        ) {
            synchronized (map) {
                long handle = getRandomHandle();
                StreamData streamData = new StreamData();
                streamData.streamHandle = handle;
                streamData.webRTC = new WebRTCImpl(trackObserver);
//                streamData.webRTC.createPeerConnection();         // create without observer?
                streamData.status = StreamStatus.Online;
                streamData.streamCapturers = new HashMap<>();
                map.put(handle, streamData);
                return streamData;
            }
        }

        StreamData create(
                TrackObserver trackObserver,
                String StreamRoomId
        ) {
            synchronized (map) {
                long handle = getRandomHandle();
                StreamData streamData = new StreamData();
                streamData.streamHandle = handle;
                streamData.webRTC = new WebRTCImpl(trackObserver);
                streamData.webRTC.createPeerConnection(StreamRoomId);
                streamData.status = StreamStatus.Online;
                streamData.streamRoomId = StreamRoomId;
                map.put(handle, streamData);
                return streamData;
            }
        }

        StreamData create(
                StreamHandle streamHandle,
                TrackObserver trackObserver
        ) {
            synchronized (map) {
//                long streamId = currentId++;
                StreamData streamData = new StreamData();
                streamData.streamHandle = streamHandle.getValue();
                streamData.webRTC = new WebRTCImpl(trackObserver);
//                streamData.webRTC.createPeerConnection();         // create without observer?
                streamData.status = StreamStatus.Online;
                streamData.streamCapturers = new HashMap<>();
                map.put(streamHandle.getValue(), streamData);
                return streamData;
            }
        }

        StreamData create(
                StreamHandle streamHandle,
                TrackObserver trackObserver,
                String StreamRoomId
        ) {
            synchronized (map) {
//                long streamId = currentId++;
                StreamData streamData = new StreamData();
                streamData.streamHandle = streamHandle.getValue();
                streamData.webRTC = new WebRTCImpl(trackObserver);
                streamData.webRTC.createPeerConnection(StreamRoomId);
                streamData.status = StreamStatus.Online;
                streamData.streamRoomId = StreamRoomId;
                map.put(streamHandle.getValue(), streamData);
                return streamData;
            }
        }
    }


    private StreamMap streamMap = new StreamMap();

//    StreamApi(Context appContext, EglBase rootEglBase, Connection connection, StreamApiLow api, PeerConnectionFactory peerConnectionFactory) {
//        this.appContext = appContext;
//        this.rootEglBase = rootEglBase;
//        this.connection = connection;
//        this.api = api;
//        this.peerConnectionFactory = peerConnectionFactory;
//    }


    public String createStreamRoom(
            String contextId,
            List<UserWithPubKey> users,
            List<UserWithPubKey> managers,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policies                // todo - can be null ??
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
        StreamData streamData = streamMap.create(trackObserver);
        api.joinStreamRoom(streamRoomId, streamData.webRTC);
    }

    // todo - probably wrong
    public void leaveStreamRoom(String streamRoomId) {
        streamMap.map.forEach((k, v) -> {
            if (Objects.equals(v.streamRoomId, streamRoomId))
                streamMap.map.remove(k);
        });
        api.leaveStreamRoom(streamRoomId);
    }

    // TODO ??
    public StreamHandle createStream(String streamRoomId) {
        StreamHandle handle = api.createStream(streamRoomId);
        streamMap.create(handle, null, streamRoomId);

        return handle;
    }

    // TODO
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

    public void addTrack(
            StreamHandle streamHandle,
            MediaDevice track
    ) {
        StreamData streamData = streamMap.get(streamHandle.getValue());
        if (peerConnectionFactory != null && streamData.webRTC != null) {

            switch (track.type) {
                case Audio: {
                    AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
                    AudioTrack audioTrack = peerConnectionFactory.createAudioTrack(track.name, audioSource);
                    audioTrack.setVolume(10.0);

                    streamData.webRTC.addAudioTrack(
                            streamData.streamRoomId,
                            audioTrack,
                            track.id
                    );
                    break;
                }

                case Video: {
                    VideoSource videoSource = peerConnectionFactory.createVideoSource(false, false);        // todo - zaimplementowac caly capturer?
                    VideoTrack videoTrack = peerConnectionFactory.createVideoTrack(track.name, videoSource);

                    streamData.webRTC.addVideoTrack(
                            streamData.streamRoomId,
                            videoTrack,
                            track.id
                    );

                    CameraVideoCapturer capturer;
                    CameraEnumerator enumerator;

                    if (Camera2Enumerator.isSupported(appContext)) {
                        enumerator = new Camera2Enumerator(appContext);
                    } else {
                        enumerator = new Camera1Enumerator(true);
                    }

                    capturer = enumerator.createCapturer(track.name, null);
                    streamData.streamCapturers.put(String.valueOf(track.id), capturer);

                    if (streamData.status == StreamStatus.Online)
                        capturer.startCapture(1280, 720, 30); // ???
                    break;
                }
            }
        }
    }

    public void removeTrack(
            StreamHandle streamHandle,
            MediaStreamTrack track
    ) {
        StreamData streamData = streamMap.get(streamHandle.getValue());

        if (streamData == null || streamData.webRTC == null) return;

        if (track instanceof AudioTrack) {
            streamData.webRTC.removeAudioTrack(streamData.streamRoomId, track.id());
        } else if (track instanceof VideoTrack) {
            streamData.webRTC.removeVideoTrack(streamData.streamRoomId, track.id());
        }
    }

    // TODO ???? - probably sth else - more
    public StreamPublishResult publishStream(StreamHandle streamHandle) {
        return api.publishStream(streamHandle);
    }

    // TODO ???? - probably sth else - more
    public StreamPublishResult updateStream(StreamHandle streamHandle) {
        return api.updateStream(streamHandle);
    }

    public void unpublishStream(StreamHandle streamHandle) {
        api.unpublishStream(streamHandle);
    }

    // TODO ??
    public void subscribeToRemoteStreams(
            String streamRoomId,
            List<StreamSubscription> subscriptions,
            StreamSettings options
    ) {
        StreamData streamData = streamMap.create(
                null,
                streamRoomId
        );

        if (options.OnFrame != null) {
            assert streamData.webRTC != null;
            streamData.webRTC.setOnFrame(streamRoomId, options.OnFrame);
        }

        if (options.OnVideoRemove != null) {
            assert streamData.webRTC != null;
            streamData.webRTC.setOnRemoveVideoTrack(streamRoomId, options.OnVideoRemove);
        }

        api.subscribeToRemoteStreams(streamRoomId, subscriptions, options);
    }

    public void modifyRemoteStreamsSubscriptions(
            String streamRoomId,
            List<StreamSubscription> subscriptionsToAdd,
            List<StreamSubscription> subscriptionsToRemove,
            StreamSettings options
    ) {
        // todo - nie ma w c++, ale czy nie jest potrzebne ?
        streamMap.map.forEach((k, v) -> {
            if (Objects.equals(v.streamRoomId, streamRoomId)) {
                assert v.webRTC != null;

                if (options.OnFrame != null) {
                    v.webRTC.setOnFrame(streamRoomId, options.OnFrame);
                }
                if (options.OnVideoRemove != null) {
                    v.webRTC.setOnRemoveVideoTrack(streamRoomId, options.OnVideoRemove);
                }
            }
        });


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
        // todo - nie ma w c++, ale czy nie jest potrzebne ?
        streamMap.map.forEach((k, v) -> {
            if (Objects.equals(v.streamRoomId, streamRoomId)) {
                assert v.webRTC != null;

                streamMap.map.remove(k);    // ??

                v.webRTC.setOnFrame(streamRoomId, null);
                v.webRTC.setOnRemoveVideoTrack(streamRoomId, null);
            }
        });


        api.unsubscribeFromRemoteStreams(
                streamRoomId,
                subscriptionsToRemove
        );
    }

    public void dropBrokenFrames(
            String streamRoomId,
            boolean enable
    ) {
        streamMap.map.forEach((k, v) -> {
            if (Objects.equals(v.streamRoomId, streamRoomId)) {
                PmxFrameCryptor.PmxFrameCryptorOptions options = new PmxFrameCryptor.PmxFrameCryptorOptions();
                options.dropFrameIfCryptionFailed = enable;

                assert v.webRTC != null;
                v.webRTC.setFrameCryptorOptions(
                        streamRoomId,
                        options
                );
            }
        });
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
