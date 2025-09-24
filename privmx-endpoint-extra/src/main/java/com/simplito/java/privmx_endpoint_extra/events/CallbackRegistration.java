package com.simplito.java.privmx_endpoint_extra.events;

/**
 * Contains all necessary information for registering an event callback.
 */
public class CallbackRegistration<T> {
    public Object callbackGroup;
    public EventCallback<T> callback;
    public EventType<T> eventType;

    public CallbackRegistration(
            Object callbackGroup,
            EventType<T> eventType,
            EventCallback<T> callback
    ) {
        this.callbackGroup = callbackGroup;
        this.callback = callback;
        this.eventType = eventType;
    }
}
