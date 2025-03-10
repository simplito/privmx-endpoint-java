package com.simplito.java.privmx_endpoint.model.events;

public class InboxCustomEventData {
    /**
     * id of inbox from which it was sent
     */
    public final String inboxId;
    /**
     * id of user which sent it
     */
    public final String userId;
    /**
     * event data
     */
    public final byte[] data;

    public InboxCustomEventData(String inboxId, String userId, byte[] data) {
        this.inboxId = inboxId;
        this.userId = userId;
        this.data = data;
    }
}
