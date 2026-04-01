package com.simplito.java.privmx_endpoint.model;


import org.webrtc.AudioTrack;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.RtpSender;

public class AudioTrackInfo {
    public AudioTrack track;
    public RtpSender sender;
    public PmxFrameCryptor frameCryptor;

    public AudioTrackInfo(
            AudioTrack track,
            RtpSender sender,
            PmxFrameCryptor frameCryptor
    ) {
        this.track = track;
        this.sender = sender;
        this.frameCryptor = frameCryptor;
    }
}