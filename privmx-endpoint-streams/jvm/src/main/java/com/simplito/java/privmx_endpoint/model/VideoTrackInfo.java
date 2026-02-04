package com.simplito.java.privmx_endpoint.model;

import org.webrtc.PmxFrameCryptor;
import org.webrtc.RtpSender;
import org.webrtc.VideoTrack;

public class VideoTrackInfo {
    public VideoTrack track;
    public RtpSender sender;
    public PmxFrameCryptor frameCryptor;

    public VideoTrackInfo(
            VideoTrack track,
            RtpSender sender,
            PmxFrameCryptor frameCryptor
    ) {
        this.track = track;
        this.sender = sender;
        this.frameCryptor = frameCryptor;
    }
}
