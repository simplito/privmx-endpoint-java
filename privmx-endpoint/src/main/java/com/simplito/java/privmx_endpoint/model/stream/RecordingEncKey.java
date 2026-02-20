package com.simplito.java.privmx_endpoint.model.stream;

import jdk.internal.util.ByteArray;

public class RecordingEncKey {
    public byte[] id;
    public byte[] key;

    public RecordingEncKey(byte[] id, byte[] key) {
        this.id = id;
        this.key = key;
    }
}