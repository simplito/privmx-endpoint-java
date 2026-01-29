package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.Key;
import com.simplito.java.privmx_endpoint.model.KeyType;
import com.simplito.java.privmx_endpoint.model.PcObserver;

import org.webrtc.MediaConstraints;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.SessionDescription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
    public StreamApi.TrackObserver observer;
    private final StreamApi.TrackObserver _observer = new StreamApi.TrackObserver() {
        @Override
        public void onTrack(MediaStreamTrack track) {
            if(observer != null){
                observer.onTrack(track);
            }
        }
    };

    //TODO: Add error listener for catch errors from webrtcInterface
    public RoomJanusSession(@NonNull String roomId, @NonNull PeerConnectionFactory pcFactory) {
        this.pcFactory = pcFactory;
        this.roomID = roomId;
        this.keyStore = PmxFrameCryptorFactory.createPmxKeyStore();
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
        if (subscriber == null) {
            PeerConnection pc = createPeerConnection();
            subscriber = new JanusSubscriber(pcFactory, keyStore, pc);
        } else if (/*TODO: Check if current subscriber is disconnected*/false) {

        }
    }

    public synchronized void createPublisher() {
        if (publisher == null) {
            PeerConnection pc = createPeerConnection();
            publisher = new JanusPublisher(pcFactory, keyStore, pc);

        } else if (/*TODO: Check if current publisher is disconnected*/false) {

        }
    }

    //TODO: We need method to pass framecryptorOptions and TrackObserver
    private PeerConnection createPeerConnection() {
        return pcFactory.createPeerConnection(
                new PeerConnection.RTCConfiguration(Collections.emptyList()),
                new PcObserver(pcFactory, roomID, keyStore, new PmxFrameCryptor.PmxFrameCryptorOptions(),_observer)
        );
    }

    public void setTrackObserver(StreamApi.TrackObserver observer){
        this.observer = observer;
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
                    return subscriber.createAnswer(sdp);
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
                    publisher.setAnswer(sdp);
                } else {
                    throw new RuntimeException("Create publisher first");
                }
            } catch (Exception ignored) {
            }
        }

        @Override
        public void close(String streamRoomId) {
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
                        subscriber.sessionId = sessionId;
                    }
                    break;

                case "publisher":
                    if (publisher != null) {
                        publisher.sessionId = sessionId;
                    }
                    break;
            }
        }
    }


}
