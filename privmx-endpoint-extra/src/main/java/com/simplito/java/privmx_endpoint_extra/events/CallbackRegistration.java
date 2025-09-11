package com.simplito.java.privmx_endpoint_extra.events;

public class CallbackRegistration {
    public Object callbackGroup;
    public EventCallback<?> callback;
    public EventType<?> eventType;

    public CallbackRegistration(
            Object callbackGroup,
            EventType<?> eventType,
            EventCallback<?> callback
    ) {
        this.callbackGroup = callbackGroup;
        this.callback = callback;
        this.eventType = eventType;
    }
}
