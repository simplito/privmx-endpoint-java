package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Unique handle to a stream, used to perform operations on the stream.
 */
public class StreamHandle {
    /**
     * The value of the {@link StreamHandle}.
     */
    private final Long value;

    /**
     * Constructs a new {@link StreamHandle} instance.
     *
     * @param value The value of the {@link StreamHandle}.
     */
    public StreamHandle(Long value) {
        this.value = value;
    }

    /**
     * Gets the value of the {@link StreamHandle}.
     */
    public Long getValue() {
        return value;
    }
}