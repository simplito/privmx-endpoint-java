package com.simplito.java.privmx_endpoint.model;

public class VerificationRequest {
    /**
     * Id of the Context
     */
    String contextId;
    /**
     * id of the sender
     */
    String senderId;
    /**
     * Public key of the sender
     */
    String senderPubKey;
    /**
     * The data creation date
     */
    Long date;

    public VerificationRequest(String contextId, String senderId, String senderPubKey, Long date) {
        this.contextId = contextId;
        this.senderId = senderId;
        this.senderPubKey = senderPubKey;
        this.date = date;
    }

    public VerificationRequest() {
    }
};