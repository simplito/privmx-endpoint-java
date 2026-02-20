package com.simplito.java.privmx_endpoint.model.stream;

public class RecordingEncKey {
    public byte[] id;
    public byte[] key;

    public RecordingEncKey(byte[] id, byte[] key) {
        this.id = id;
        this.key = key;
    }
}