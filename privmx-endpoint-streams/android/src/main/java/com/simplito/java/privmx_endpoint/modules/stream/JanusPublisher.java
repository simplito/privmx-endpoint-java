package com.simplito.java.privmx_endpoint.modules.stream;

import androidx.annotation.NonNull;
import com.simplito.java.privmx_endpoint.model.AudioTrackInfo;
import com.simplito.java.privmx_endpoint.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.stream.SdpWithTypeModel;
import com.simplito.java.privmx_endpoint.model.VideoTrackInfo;

import org.webrtc.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JanusPublisher extends JanusConnection {
    private final Map<String, AudioTrackInfo> audioTracks = new HashMap<>();
    private final Map<String, VideoTrackInfo> videoTracks = new HashMap<>();
    private DataChannel dataChannel = null;
    private final BiConsumer<Long, SdpWithTypeModel> setNewOfferOnReconfigure;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JanusPublisher(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            RemoteStreamObserver observer,
            BiConsumer<Long, String> onTrickle,
            BiConsumer<Long, SdpWithTypeModel> acceptRenegotiationOffer,
            Consumer<PeerConnection.IceConnectionState> onConnectionChange
    ) {
        super(pcFactory, keyStore, ConnectionType.Publisher, observer, onTrickle, onConnectionChange);
        this.setNewOfferOnReconfigure = acceptRenegotiationOffer;
    }

    public void addAudioTrack(org.webrtc.AudioTrack audioTrack) {
        synchronized (peerConnection) {
            RtpSender rtpSender2 = peerConnection.addTrack(audioTrack);
            PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                    peerConnectionFactory,
                    rtpSender2,
                    keyStore
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
                        keyStore
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

    @NonNull
    public synchronized DataChannel getOrCreateDataChannel() throws RuntimeException{
        if (dataChannel != null) {
            if (dataChannel.state() == DataChannel.State.OPEN) return dataChannel;
            //TODO: try reconnecting
            if (dataChannel.state() == DataChannel.State.CLOSING)
                throw new IllegalStateException("Data channel is now closing");
            //If state is closed then create new data channel and wait for open
            //If datachannel state is connecting then method execution waits on synchronized block.
        }
        if (dataChannel == null || dataChannel.state() == DataChannel.State.CLOSED) {
            dataChannel = createNewDataChannel();
        }
        waitForOpen(dataChannel,false);
        if(dataChannel.state() != DataChannel.State.OPEN) throw new RuntimeException("Cannot open data channel");
        return dataChannel;
    }

    private DataChannel createNewDataChannel() {
        synchronized (peerConnection) {
            return peerConnection.createDataChannel("JanusDataChannel", getDataChannelInit());
        }
    }

    private void waitForOpen(DataChannel channel, boolean withTimeout) {
        if (channel.state() == DataChannel.State.OPEN) return;
        if (channel.state() != DataChannel.State.CONNECTING) throw new RuntimeException("Channel is closing or closed");
        CompletableFuture<DataChannel> dataChannelCompletableFuture = new CompletableFuture<>();
        channel.registerObserver(new DataChannel.Observer() {
            @Override
            public void onBufferedAmountChange(long l) {
            }

            @Override
            public void onStateChange() {
                System.out.println("datachannel state updated: " + channel.state().name());
                if (channel.state() == DataChannel.State.OPEN) {
                    dataChannelCompletableFuture.complete(channel);
                } else {
                    dataChannelCompletableFuture.completeExceptionally(new RuntimeException("Cannot open data channel"));
                }
            }

            @Override
            public void onMessage(DataChannel.Buffer buffer) {
            }
        });
        try {
            if (withTimeout) {
                dataChannelCompletableFuture.get(20, TimeUnit.SECONDS);
                return;
            }
            dataChannelCompletableFuture.get();
        }catch (Throwable ignored){}
    }

    private void closeDataChannel(){
        if(dataChannel != null && dataChannel.state() != DataChannel.State.CLOSING && dataChannel.state() != DataChannel.State.CLOSING) dataChannel.dispose();
        dataChannel = null;
    }

    private DataChannel.Init getDataChannelInit() {
        DataChannel.Init init = new DataChannel.Init();
        init.ordered = true;
        init.negotiated = false;
        return init;
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
}
