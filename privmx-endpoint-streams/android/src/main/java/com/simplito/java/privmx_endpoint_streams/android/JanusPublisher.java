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


import com.simplito.java.privmx_endpoint_streams.android.model.AudioTrackInfo;
import com.simplito.java.privmx_endpoint_streams.android.model.ConnectionType;
import com.simplito.java.privmx_endpoint.model.stream.SdpWithTypeModel;
import com.simplito.java.privmx_endpoint_streams.android.model.VideoTrackInfo;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxFrameCryptorFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.RtpSender;
import org.webrtc.SessionDescription;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class JanusPublisher extends JanusConnection{
    private final Map<String, AudioTrackInfo> audioTracks = new HashMap<>();
    private final Map<String, VideoTrackInfo> videoTracks = new HashMap<>();
    private final BiConsumer<Long, SdpWithTypeModel> setNewOfferOnReconfigure;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public JanusPublisher(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            TrackObserver observer,
            BiConsumer<Long, String> onTrickle,
            BiConsumer<Long, SdpWithTypeModel> acceptRenegotiationOffer,
            Consumer<PeerConnection.IceConnectionState> onConnectionChange
    ) {
        super(pcFactory, keyStore, ConnectionType.Publisher, observer, onTrickle, onConnectionChange);
        this.setNewOfferOnReconfigure = acceptRenegotiationOffer;
    }

    public void addAudioTrack(org.webrtc.AudioTrack audioTrack) {
        synchronized (audioTracks) {
            RtpSender rtpSender2 = peerConnection.addTrack(audioTrack);
            PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                    peerConnectionFactory,
                    rtpSender2,
                    keyStore,
                    null
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
            synchronized (videoTracks) {
                RtpSender rtpSender = peerConnection.addTrack(videoTrack);
                PmxFrameCryptor frameCryptor = PmxFrameCryptorFactory.createPmxFrameCryptorFromRtpSender(
                        peerConnectionFactory,
                        rtpSender,
                        keyStore,
                        null
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

    public void removeAudioTrack(String id) {
        synchronized (audioTracks) {
            AudioTrackInfo audioTrackInfo = audioTracks.get(id);
            if (audioTrackInfo == null) return;
            peerConnection.removeTrack(audioTrackInfo.sender);

            AudioTrackInfo removedAudioTrackInfo = audioTracks.remove(id);
            if (removedAudioTrackInfo != null) {
                removedAudioTrackInfo.frameCryptor.dispose();
            }
        }
    }

    public void removeVideoTrack(String id) {
        synchronized (videoTracks) {
            VideoTrackInfo videoTrackInfo = videoTracks.get(id);
            if (videoTrackInfo == null) return;
            peerConnection.removeTrack(videoTrackInfo.sender);
            videoTracks.remove(id);

            VideoTrackInfo removedVideoTrackInfo = videoTracks.remove(id);
            if (removedVideoTrackInfo != null) {
                removedVideoTrackInfo.frameCryptor.dispose();
            }
        }
    }

    public String createOffer(){
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
        peerConnection.createOffer(new SdpObserver(res), new MediaConstraints());
        try {
            SessionDescription offer = res.get();
            peerConnection.setLocalDescription(new SdpObserver(null),offer);
            return offer.description;
        }catch (Exception e){
            throw new RuntimeException("Cannot create offer");
        }
    }

    public void setAnswer(String sdp, String type){
        peerConnection.setRemoteDescription(new SdpObserver(null),new SessionDescription(SessionDescription.Type.fromCanonicalForm(type),sdp));
    }


    @Override
    public void close() {
        try {
            audioTracks.values().forEach(track -> track.frameCryptor.dispose());
            videoTracks.values().forEach(track -> track.frameCryptor.dispose());
        } catch (IllegalStateException ignored) {}
        audioTracks.clear();
        videoTracks.clear();

        super.close();
    }

    @Override
    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        this.videoTracks.values().forEach(it ->{
            it.frameCryptor.setOptions(options);
        });
        this.audioTracks.values().forEach(it ->{
            it.frameCryptor.setOptions(options);
        });
    }


    @Override
    public void onRenegotiationNeeded() {
        if(getSessionId() > -1) {
            executorService.execute(()->{
                setNewOfferOnReconfigure.accept(getSessionId(),new SdpWithTypeModel(createOffer(), SessionDescription.Type.OFFER.canonicalForm()));
            });
        }
    }
}
