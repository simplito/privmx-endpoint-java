package com.simplito.java.privmx_endpoint_extra.events;

/**
 * Represents a registration of a callback for a specific event type.
 * This class encapsulates the information needed to register and subscribe a callback.
 * @param <T> The type of event data.
 */
public class CallbackRegistration<T> {
    /**
     * An identifier used to group related callbacks.
     */
    public Object callbackGroup;
    /**
     * The block of code that will be executed
     * when an event of the specified {@link #eventType} is handled.
     */
    public EventCallback<T> callback;
    /**
     * The specific type of event to subscribe to.
     */
    public EventType<T> eventType;

    /**
     * Creates a new callback registration instance.
     * This constructor is used to define a specific callback that should be executed
     * when an event of a particular type occurs. Callbacks are grouped
     * for easier management, such as unregistering multiple related callbacks at once.
     *
     * @param callbackGroup An identifier used to group related callbacks.
     *                      This allows for collective operations, like unregistering
     *                      all callbacks belonging to the same group. Can be any object,
     *                      its identity (reference equality) is used for grouping.
     * @param eventType     The specific type of event to subscribe to. The provided
     *                      {@code callback} will only be invoked for events matching
     *                      this type.
     * @param callback      The {@link EventCallback} instance that will be executed
     *                      when an event of the specified {@code eventType} is handled.
     *                      This callback will receive the event data as a parameter.
     */
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
