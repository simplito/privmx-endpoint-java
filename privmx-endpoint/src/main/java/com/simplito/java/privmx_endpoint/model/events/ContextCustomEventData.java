package com.simplito.java.privmx_endpoint.model.events;

public class ContextCustomEventData {
    /**
     * id of inbox from which it was sent
     */
    public final String contextId;
    /**
     * id of user which sent it
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
