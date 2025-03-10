package com.simplito.java.privmx_endpoint.model.events;

public class StoreCustomEventData {
    /**
     * id of store from which it was sent
     */
    public final String storeId;
    /**
     * id of user which sent it
     */
    public final String userId;
    /**
     * event data
     */
    public final byte[] data;

    public StoreCustomEventData(String storeId, String userId, byte[] data) {
        this.storeId = storeId;
        this.userId = userId;
        this.data = data;
    }
}
