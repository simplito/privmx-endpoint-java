package com.simplito.java.privmx_endpoint_extra.events;

import com.simplito.java.privmx_endpoint.model.EventSelector;

public class EventSelectorExtra<T> {
    public Object callbackId;
    public EventSelector eventSelector;
    public EventCallback<T> callback;
    public String query;
    public String channelName = null;

    public EventSelectorExtra(Object callbackId, EventSelector eventSelector, EventCallback<T> callback) {
        this.callback = callback;
        this.eventSelector = eventSelector;
        this.callbackId = callbackId;
    }

    public EventSelectorExtra(Object callbackId, EventSelector eventSelector, String channelName, EventCallback<T> callback) {
        this.callback = callback;
        this.eventSelector = eventSelector;
        this.callbackId = callbackId;
        this.channelName = channelName;
    }
}