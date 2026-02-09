package com.simplito.java.privmx_endpoint.model;

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