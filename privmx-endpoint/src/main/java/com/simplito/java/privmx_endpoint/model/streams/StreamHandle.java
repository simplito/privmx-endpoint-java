package com.simplito.java.privmx_endpoint.model.streams;

public class StreamHandle {
    private final long value;

    public StreamHandle(Long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}