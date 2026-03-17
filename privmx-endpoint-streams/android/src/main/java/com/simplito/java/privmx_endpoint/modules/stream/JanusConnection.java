package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.ConnectionType;

import org.json.JSONObject;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxKeyStore;
import org.webrtc.SessionDescription;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class JanusConnection {
    protected final PeerConnection peerConnection;
    protected final PeerConnectionFactory peerConnectionFactory;
    protected PmxKeyStore keyStore;
    public final ConnectionType connectionType;
    private long sessionId = -1L;
    private final PcObserver pcObserver;

    public JanusConnection(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            ConnectionType connectionType,
            TrackObserver trackObserver,
            BiConsumer<Long,String> onTrickle
    ) {
        this.peerConnectionFactory = pcFactory;
        this.connectionType = connectionType;
        this.keyStore = keyStore;
        this.pcObserver = new PcObserver(
                peerConnectionFactory,
                keyStore,
                trackObserver,
                iceCandidate -> {
                    if(sessionId > -1) {
                        JSONObject obj = new JSONObject();
                        try{
                            obj.put("sdp",iceCandidate.sdp);
                            obj.put("adapterType",iceCandidate.adapterType.ordinal());
                            obj.put("sdpMid",iceCandidate.sdpMid);
                            obj.put("sdpMLineIndex",iceCandidate.sdpMLineIndex);
                            obj.put("serverUrl",iceCandidate.serverUrl);
                        }catch (Exception ignore){
                            System.out.println("Error with obj json");
                        }
                        //TODO: Uncomment trickle when update privmx streams compilation
//                        onTrickle.accept(sessionId,obj.toString());
//                        System.out.println("Trickle executed");
                    }
                }
        );
        this.peerConnection = createPeerConnection(pcObserver);
    }

    static class SdpObserver implements org.webrtc.SdpObserver {
        private final CompletableFuture<SessionDescription> res;

        SdpObserver(CompletableFuture<SessionDescription> res) {
            this.res = res;
        }

        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            res.complete(sessionDescription);
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

    //TODO: We need method to pass framecryptorOptions and TrackObserver
    private PeerConnection createPeerConnection(PcObserver pcObserver) {
        return peerConnectionFactory.createPeerConnection(
                new PeerConnection.RTCConfiguration(Collections.emptyList()),
                pcObserver
        );
    }

    public PeerConnection.PeerConnectionState getConnectionState(){
        return peerConnection.connectionState();
    }

    void updateSessionId(long sessionId){
        this.sessionId = sessionId;
    }

    public long getSessionId(){
        return this.sessionId;
    }


    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        pcObserver.setFrameCryptorOptions(options);
    }

    public boolean isEnded(){
        return peerConnection.connectionState() == PeerConnection.PeerConnectionState.DISCONNECTED ||
                peerConnection.connectionState() == PeerConnection.PeerConnectionState.CLOSED ||
                peerConnection.connectionState() == PeerConnection.PeerConnectionState.FAILED;
    }

    public void close(){
        if(peerConnection.connectionState() != PeerConnection.PeerConnectionState.CLOSED) {
            peerConnection.dispose();
        }
    }

    public void setRTCConfiguration(List<PeerConnection.IceServer> configuration) {
        this.peerConnection.setConfiguration(new PeerConnection.RTCConfiguration(configuration));
    }
}