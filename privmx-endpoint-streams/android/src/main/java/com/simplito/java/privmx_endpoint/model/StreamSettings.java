package com.simplito.java.privmx_endpoint.model;

import com.simplito.java.privmx_endpoint.model.stream.Settings;

public class StreamSettings {
    public Settings settings;
    public boolean dropCorruptedFrames = true;

    public StreamSettings(Settings settings, boolean dropCorruptedFrames) {
        this.settings = settings;
        this.dropCorruptedFrames = dropCorruptedFrames;
    }

    public StreamSettings(Settings settings) {
        this(settings, true);
    }
}