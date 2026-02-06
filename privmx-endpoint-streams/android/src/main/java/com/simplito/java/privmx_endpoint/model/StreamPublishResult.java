package com.simplito.java.privmx_endpoint.model;

public class StreamPublishResult {
public Boolean published;
public PublishedStreamData data;

    public StreamPublishResult(Boolean published) {
        this(published, null);
    }
    public StreamPublishResult(Boolean published, PublishedStreamData data) {
        this.published = published;
        this.data = data;
    }
}
