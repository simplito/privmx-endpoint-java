package com.simplito.java.privmx_endpoint_streams.model;

import com.simplito.java.privmx_endpoint.model.PublishedStreamData;

public class StreamPublishResult {
public Boolean published;
public PublishedStreamData data;

    public StreamPublishResult(boolean published) {
        this(published, null);
    }
    public StreamPublishResult(boolean published, PublishedStreamData data) {
        this.published = published;
        this.data = data;
    }
}
