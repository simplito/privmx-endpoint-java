package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information about emitted custom event.
 *
 * @category core
 * @group Events
 */
public class ContextCustomEventData {
    /**
     * ID of inbox from which the event was sent
     */
    public final String contextId;
    /**
     * ID of the user who sent it
     */
    public final String userId;
    /**
     * event data
     */
    public final byte[] data;

    public ContextCustomEventData(String contextId, String userId, byte[] data) {
        this.contextId = contextId;
        this.userId = userId;
        this.data = data;
    }
}
