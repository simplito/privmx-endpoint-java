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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.stream.Key;
import com.simplito.java.privmx_endpoint.model.stream.KeyType;
import com.simplito.java.privmx_endpoint.model.stream.SdpWithTypeModel;
import com.simplito.java.privmx_endpoint.modules.stream.WebRTCInterface;

import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RoomJanusSession {
    @NonNull
    public final String roomID;
    @NonNull
    final PeerConnectionFactory pcFactory;
    @Nullable
    private JanusSubscriber subscriber = null;
    @Nullable
    private JanusPublisher publisher = null;
    private final PmxKeyStore keyStore;
    public final WebRTCImpl webrtc = new WebRTCImpl();
    private final BiConsumer<Long, String> onTrickle;
    private final Map<String, TrackObserver> trackObserversByStreamId = new HashMap<>();
    private final TrackObserver trackObserver = new TrackObserverImpl();
    private Consumer<PeerConnection.IceConnectionState> onConnectionChangeCallback = null;
    private final BiConsumer<Long, SdpWithTypeModel> setNewOfferOnReconfigure;
    private Consumer<Map<String, Long>> onSpeakingStatsChanged = null;
    private final SpeakingAnalyzer audioSpeakingAnalyzer = new SpeakingAnalyzer(SpeakingAnalyzer.DefaultConfig);

    //TODO: Add error listener for catch errors from webrtcInterface
    public RoomJanusSession(
            @NonNull String roomId,
            @NonNull PeerConnectionFactory pcFactory,
            BiConsumer<Long, String> onTrickle,
            BiConsumer<Long, SdpWithTypeModel> acceptRenegotiationOffer
    ) {
        this.pcFactory = pcFactory;
        this.roomID = roomId;
        this.keyStore = PmxFrameCryptorFactory.createPmxKeyStore();
        this.onTrickle = onTrickle;
        this.setNewOfferOnReconfigure = acceptRenegotiationOffer;
    }

    @Nullable
    public synchronized JanusSubscriber getSubscriber() {
        return subscriber;
    }

    @Nullable
    public synchronized JanusPublisher getPublisher() {
        return publisher;
    }

    public synchronized void createSubscriber() {
        createSubscriber(trackObserver);
    }

    public synchronized void createSubscriber(TrackObserver observer) {
        if (subscriber == null) {
            subscriber = new JanusSubscriber(pcFactory, keyStore, observer, onTrickle, this::onRmsChanged);
        } else if (subscriber.isEnded()) {
            subscriber.close();
            subscriber = new JanusSubscriber(pcFactory, keyStore, observer, onTrickle, this::onRmsChanged);
        } else {
            throw new IllegalStateException("Subscriber is currently active.");
        }
    }

    public synchronized void createPublisher() {
        createPublisher(null);
    }

    public synchronized void createPublisher(TrackObserver observer) {
        if (publisher == null) {
            publisher = new JanusPublisher(
                    pcFactory,
                    keyStore,
                    observer,
                    onTrickle,
                    setNewOfferOnReconfigure,
                    this::onConnectionChange,
                    this::onRmsChanged
            );
        } else if (publisher.isEnded()) {
            publisher.close();
            publisher = new JanusPublisher(
                    pcFactory,
                    keyStore,
                    observer,
                    onTrickle,
                    setNewOfferOnReconfigure,
                    this::onConnectionChange,
                    this::onRmsChanged
            );
        } else {
            throw new IllegalStateException("Publisher is currently active.");
        }
    }

    public void setTrackObserver(
            TrackObserver trackObserver
    ) {
        setTrackObserver(null, trackObserver);
    }

    public void setTrackObserver(
            String streamId,
            TrackObserver trackObserver
    ) {
        synchronized (trackObserversByStreamId) {
            trackObserversByStreamId.put(streamId, trackObserver);
        }
    }

    public synchronized void setOnConnectionChange(
            Consumer<PeerConnection.IceConnectionState> onConnectionChange
    ) {
        this.onConnectionChangeCallback = onConnectionChange;
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        if (subscriber != null) {
            subscriber.setFrameCryptorOptions(options);
        }
        if (publisher != null) {
            publisher.setFrameCryptorOptions(options);
        }
    }

    private void onConnectionChange(PeerConnection.IceConnectionState connectionState) {
        if (onConnectionChangeCallback != null) {
            onConnectionChangeCallback.accept(connectionState);
        }
    }

    public void setOnSpeakingStatsChanged(Consumer<Map<String, Long>> speakingStatsChanged) {
        this.onSpeakingStatsChanged = speakingStatsChanged;
    }

    private void onRmsChanged(String streamId, byte rms, long timestamp) {
        if (onSpeakingStatsChanged != null) {
            audioSpeakingAnalyzer.onRms(streamId, rms, timestamp);
            onSpeakingStatsChanged.accept(audioSpeakingAnalyzer.getSpeakersInfo());
        }
    }

    void unpublish() {
        if (publisher != null && !publisher.isEnded()) {
            final JanusPublisher publisher = this.publisher;
            synchronized (publisher) {
                publisher.close();
                this.publisher = null;
            }
        }
    }

    public class WebRTCImpl implements WebRTCInterface {
        private final ExecutorService executor = Executors.newSingleThreadExecutor();

        private WebRTCImpl() {
        }

        @Override
        public String createOfferAndSetLocalDescription(String streamRoomId) {
            try {
                if (publisher != null) {
                    return publisher.createOffer();
                } else {
                    throw new RuntimeException("Create publisher first");
                }
            } catch (Exception ignored) {
                return "";
            }
        }


        @Override
        public String createAnswerAndSetDescriptions(String streamRoomId, String sdp, String type) {
            try {
                if (subscriber != null) {
                    return subscriber.createAnswer(sdp, type);
                } else {
                    throw new RuntimeException("Create subscriber first");
                }
            } catch (Exception ignored) {
                return "";
            }
        }

        @Override
        public void setAnswerAndSetRemoteDescription(String streamRoomId, String sdp, String type) {
            try {
                if (publisher != null) {
                    publisher.setAnswer(sdp, type);
                } else {
                    throw new RuntimeException("Create publisher first");
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void close(String streamRoomId) {
            //TODO: Clean all objects correctly
            if (publisher != null) {
                publisher.close();
            }
            if (subscriber != null) {
                subscriber.close();
            }
        }

        @Override
        public void updateKeys(String streamRoomId, List<Key> keys) {
            executor.execute(() -> {
                List<PmxKeyStore.Key> list = keys.stream().map(key ->
                        new PmxKeyStore.Key(
                                key.keyId,
                                key.key,
                                key.type == KeyType.LOCAL ? PmxKeyStore.KeyType.LOCAL : PmxKeyStore.KeyType.REMOTE
                        )
                ).collect(Collectors.toList());
                keyStore.setKeys(list);
            });
        }

        @Override
        public void updateSessionId(String streamRoomId, Long sessionId, String connectionType) {
            switch (connectionType) {
                case "subscriber":
                    if (subscriber != null) {
                        subscriber.updateSessionId(sessionId);
                    }
                    break;

                case "publisher":
                    if (publisher != null) {
                        publisher.updateSessionId(sessionId);
                    }
                    break;
            }
        }
    }

    private class TrackObserverImpl implements TrackObserver {
        @Override
        public void OnRemoteTrack(String streamId, MediaStreamTrack track) {
            synchronized (trackObserversByStreamId) {
                Optional.ofNullable(trackObserversByStreamId.get(streamId)).ifPresent(observer -> {
                    observer.OnRemoteTrack(streamId, track);
                });

                Optional.ofNullable(trackObserversByStreamId.get(null)).ifPresent(observer -> {
                    observer.OnRemoteTrack(streamId, track);
                });
            }
        }
    }
}
