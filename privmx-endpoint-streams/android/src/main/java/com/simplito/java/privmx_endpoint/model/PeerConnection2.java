package com.simplito.java.privmx_endpoint.model;

import org.webrtc.PeerConnection;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.PmxKeyStore;

import java.util.HashMap;
import java.util.Map;

public class PeerConnection2 {
    public PeerConnection pc;
    public PcObserver observer;
    public PmxKeyStore keys;
    public Map<String, AudioTrackInfo> audioTracks = new HashMap<>();
    public Map<String, VideoTrackInfo> videoTracks = new HashMap<>();

    public PeerConnection2(PeerConnection pc, PcObserver observer, PmxKeyStore keys) {
        this.pc = pc;
        this.observer = observer;
        this.keys = keys;
    }

    public void addVideoTrack(String id, VideoTrackInfo track) {
        videoTracks.put(id, track);
    }

    public void addAudioTrack(String id, AudioTrackInfo track) {
        audioTracks.put(id, track);
    }

    public void removeAudioTrack(String id) {
        AudioTrackInfo audioTrack = audioTracks.get(id);
        assert audioTrack != null;
        pc.removeTrack(audioTrack.sender);
        audioTracks.remove(id);
    }

    public void removeVideoTrack(String id) {
        VideoTrackInfo videoTrack = videoTracks.get(id);
        assert videoTrack != null;
        pc.removeTrack(videoTrack.sender);
        videoTracks.remove(id);
    }

    public void setFrameCryptorOptions(PmxFrameCryptor.PmxFrameCryptorOptions options) {
        observer.setFrameCryptorOptions(options);
    }


    //    std::map<std::string, AudioTrackInfo> audioTracks;
//    std::map<std::string, VideoTrackInfo> videoTracks;
//    std::shared_mutex trackMutex;
//    std::shared_ptr<privmx::webrtc::KeyStore> keys;
}
