package com.simplito.java.privmx_endpoint.model.events;

public class ThreadCustomEventData {
    /**
     * id of thread from which it was sent
     */
    public final String threadId;
    /**
     * id of user which sent it
     */
    public final String userId;
    /**
     * event data
     */
    public final byte[] data;
    
    public ThreadCustomEventData(String threadId, String userId, byte[] data) {
        this.threadId = threadId;
        this.userId = userId;
        this.data = data;
    }
}
