package com.simplito.java.privmx_endpoint_streams.model;

import java.util.function.Consumer;

public class StreamSettings {
    public Settings settings;
    public boolean dropCorruptedFrames = true;
    public OnFrameCallback OnFrame = null;
    public Consumer<String> OnVideo = null;     //  _onVideoTrack.value()(_streamRoomId + "-" + receiver->track()->id().std_string());

    public Consumer<String> OnVideoRemove = null;

    public StreamSettings(Settings settings, boolean dropCorruptedFrames, OnFrameCallback onFrame, Consumer<String> onVideo, Consumer<String> onVideoRemove) {
        this.settings = settings;
        this.dropCorruptedFrames = dropCorruptedFrames;
        OnFrame = onFrame;
        OnVideo = onVideo;
        OnVideoRemove = onVideoRemove;
    }

    public StreamSettings(Settings settings, boolean dropCorruptedFrames, OnFrameCallback onFrame, Consumer<String> onVideo) {
        this(settings, dropCorruptedFrames, onFrame, onVideo, null);
    }

    public StreamSettings(Settings settings, OnFrameCallback onFrame, boolean dropCorruptedFrames) {
        this(settings, dropCorruptedFrames, onFrame, null, null);

    }

    public StreamSettings(Settings settings, boolean dropCorruptedFrames) {
        this(settings, dropCorruptedFrames, null, null, null);

    }

    public StreamSettings(Settings settings) {
        this(settings, true, null, null, null);
    }
}