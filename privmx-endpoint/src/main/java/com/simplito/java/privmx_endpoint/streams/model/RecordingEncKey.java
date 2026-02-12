package com.simplito.java.privmx_endpoint.streams.model;

import jdk.internal.util.ByteArray;

public class RecordingEncKey {
    public ByteArray id;
    public ByteArray key;

    public RecordingEncKey(ByteArray id, ByteArray key) {
        this.id = id;
        this.key = key;
    }
}