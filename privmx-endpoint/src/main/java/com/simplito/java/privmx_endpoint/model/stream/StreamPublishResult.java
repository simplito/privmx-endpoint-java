package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Represents the result of a stream publish/update operation.
 */
public class StreamPublishResult {
    /**
     * Indicates if the stream was successfully published
     */
    public Boolean published;

    /**
     * Additional information about the published stream
     */
    public PublishedStreamData data;

    /**
     * Constructs a new {@link StreamPublishResult} instance.
     *
     * @param published Indicates if the stream was successfully published
     */
    public StreamPublishResult(Boolean published) {
        this(published, null);
    }

    /**
     * Constructs a new {@link StreamPublishResult} instance.
     *
     * @param published Indicates if the stream was successfully published
     * @param data      Additional information about the published stream
     */
    public StreamPublishResult(Boolean published, PublishedStreamData data) {
        this.published = published;
        this.data = data;
    }
}
