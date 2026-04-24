package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.simplito.java.privmx_endpoint.model.AudioTrackInfo;
import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.stream.DataChannelMessage;
import com.simplito.java.privmx_endpoint.model.stream.SdpWithTypeModel;
import com.simplito.java.privmx_endpoint.model.VideoTrackInfo;

import org.webrtc.*;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JanusPublisher extends JanusConnection {
    private final Map<String, AudioTrackInfo> audioTracks = new HashMap<>();
    private final Map<String, VideoTrackInfo> videoTracks = new HashMap<>();
    private final BiConsumer<Long, SdpWithTypeModel> setNewOfferOnReconfigure;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private DataChannelWrapper wrappedDataChannel = null;

    public JanusPublisher(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            RemoteStreamObserver observer,
            BiConsumer<Long, String> onTrickle,
            BiConsumer<Long, SdpWithTypeModel> acceptRenegotiationOffer,
            Consumer<PeerConnection.IceConnectionState> onConnectionChange,
            InternalDataChannelEncryption dataChannelEncryption
    ) {
        super(pcFactory, keyStore, ConnectionType.Publisher, observer, onTrickle, onConnectionChange, dataChannelEncryption);
        this.setNewOfferOnReconfigure = acceptRenegotiationOffer;
    }

    public void addAudioTrack(org.webrtc.AudioTrack audioTrack) {
        synchronized (peerConnection) {
            RtpSender rtpSender2 = peerConnection.addTrack(audioTrack);
            PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                    peerConnectionFactory,
                    rtpSender2,
                    keyStore,
                    null
                    // options ?
            );

            audioTracks.put(
                    audioTrack.id(),
                    new AudioTrackInfo(
                            audioTrack,
                            rtpSender2,
                            frameCryptor
                    )
            );
        }
    }

    public void addVideoTrack(org.webrtc.VideoTrack videoTrack) {
        if (peerConnectionFactory != null) {
            synchronized (peerConnection) {
                RtpSender rtpSender = peerConnection.addTrack(videoTrack);
                PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                        peerConnectionFactory,
                        rtpSender,
                        keyStore,
                        null
                        // options ?
                );

                videoTracks.put(
                        videoTrack.id(),
                        new VideoTrackInfo(
                                videoTrack,
                                rtpSender,
                                frameCryptor
                        )
                );
            }
        }
    }

    public synchronized void createDataChannel(@Nullable Runnable onCloseDataChannel) {
        synchronized (peerConnection) {
            if (wrappedDataChannel != null) {
                if (wrappedDataChannel.dataChannel.state() != DataChannel.State.CLOSED)
                    throw new IllegalStateException("You already have an active data channel");
            }
            if (wrappedDataChannel == null || wrappedDataChannel.dataChannel.state() == DataChannel.State.CLOSED) {
                wrappedDataChannel = new DataChannelWrapper(peerConnection, onCloseDataChannel);
            }
        }
    }

    public synchronized void closeDataChannel() {
        if (wrappedDataChannel == null) return;
        if (wrappedDataChannel.dataChannel.state() == DataChannel.State.OPEN ||
                wrappedDataChannel.dataChannel.state() == DataChannel.State.CONNECTING) {
            wrappedDataChannel.dataChannel.close();
            wrappedDataChannel.dataChannel.dispose();
            wrappedDataChannel = null;
        }
    }

    public synchronized void sendMessage(byte[] message) {
        if (wrappedDataChannel == null)
            throw new IllegalStateException("DataChannel is not created");
        //TODO: Throw exception when cannot send message because channel is closed or closing
        //TODO: Throw exception when timeout reach. And throw that too long wait for open
        wrappedDataChannel.waitForDataChannelOpen(false);
        if (wrappedDataChannel.dataChannel.bufferedAmount() + message.length > DataChannelWrapper.MAX_BUFFERED_AMOUNT)
            throw new RuntimeException("Buffered messages size exceeded");
        if (dataChannelEncryption != null) {
            byte[] bytesToSend = dataChannelEncryption.encryptDataChannelMessage(new DataChannelMessage(message, 0));

        }else{
            wrappedDataChannel.dataChannel.send(new DataChannel.Buffer(ByteBuffer.wrap(message), true));
        }

    }

    public void removeAudioTrack(String id) {
        synchronized (audioTracks) {
            AudioTrackInfo audioTrackInfo = audioTracks.get(id);
            if (audioTrackInfo == null) return;
            peerConnection.removeTrack(audioTrackInfo.sender);
            audioTracks.remove(id);
        }
    }

    public void removeVideoTrack(String id) {
        synchronized (videoTracks) {
            VideoTrackInfo videoTrackInfo = videoTracks.get(id);
            if (videoTrackInfo == null) return;
            peerConnection.removeTrack(videoTrackInfo.sender);
            videoTracks.remove(id);
        }
    }

    public String createOffer() {
        synchronized (peerConnection) {
            CompletableFuture<SessionDescription> res = new CompletableFuture<>();
            peerConnection.createOffer(new SdpObserver(res), new MediaConstraints());
            try {
                SessionDescription offer = res.get();
                peerConnection.setLocalDescription(new SdpObserver(null), offer);
                System.out.println("publisher Created offer " + offer.description);
                return offer.description;
            } catch (Exception e) {
                throw new RuntimeException("Cannot create offer");
            }
        }
    }

    public void setAnswer(String sdp, String type) {
        synchronized (peerConnection) {
            peerConnection.setRemoteDescription(new SdpObserver(null), new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp));
        }
    }


    @Override
    public void close() {
        super.close();
        audioTracks.clear();
        videoTracks.clear();
    }

    @Override
    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        this.videoTracks.values().forEach(it -> {
            it.frameCryptor.setOptions(options);
        });
        this.audioTracks.values().forEach(it -> {
            it.frameCryptor.setOptions(options);
        });
    }


    @Override
    public void onRenegotiationNeeded() {
        if (getSessionId() > -1) {
            executorService.execute(() -> {
                setNewOfferOnReconfigure.accept(getSessionId(), new SdpWithTypeModel(createOffer(), SessionDescription.Type.OFFER.canonicalForm()));
            });
        }
    }

    private static class DataChannelWrapper {
        final DataChannel dataChannel;
        final Runnable onCloseDataChannel;
        final CompletableFuture<Void> dataChannelOpenCompletable = new CompletableFuture<>();
        private static final int MAX_BUFFERED_AMOUNT = 128 * 1024 * 1024;
        private static final int MAX_CHUNK_SIZE = 256 * 1024;

        private DataChannelWrapper(
                PeerConnection peerConnection,
                Runnable onCloseDataChannel
        ) {
            this.dataChannel = peerConnection.createDataChannel("JanusDataChannel", getDataChannelInit());
            dataChannel.registerObserver(new DataChannel.Observer() {
                @Override
                public void onBufferedAmountChange(long l) {
                }

                @Override
                public void onStateChange() {
                    if (dataChannel.state() == DataChannel.State.OPEN) {
                        if (!dataChannelOpenCompletable.isDone()) {
                            dataChannelOpenCompletable.complete(null);
                        }
                    } else {
                        if (!dataChannelOpenCompletable.isDone()) {
                            dataChannelOpenCompletable.completeExceptionally(new RuntimeException("Cannot open data channel"));
                        }
                        if (dataChannel.state() == DataChannel.State.CLOSED && onCloseDataChannel != null) {
                            onCloseDataChannel.run();
                        }
                    }
                }

                @Override
                public void onMessage(DataChannel.Buffer buffer) {
                }
            });
            this.onCloseDataChannel = onCloseDataChannel;
        }

        private DataChannel.Init getDataChannelInit() {
            DataChannel.Init init = new DataChannel.Init();
            init.ordered = true;
            init.negotiated = false;
            return init;
        }

        private void waitForDataChannelOpen(boolean withTimeout) {
            if (dataChannel.state() == DataChannel.State.OPEN) return;
            if (dataChannel.state() != DataChannel.State.CONNECTING)
                throw new RuntimeException("Channel is closing or closed");

            try {
                if (withTimeout) {
                    dataChannelOpenCompletable.get(20, TimeUnit.SECONDS);
                    return;
                }
                dataChannelOpenCompletable.get();
            } catch (Throwable ignored) {
            }
        }
    }
}
