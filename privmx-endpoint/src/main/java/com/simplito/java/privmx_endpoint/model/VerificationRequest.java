package com.simplito.java.privmx_endpoint.model;

/**
 * Represents a request used for verifying a sender in a specific context.
 *
 * @category core
 * @group Core
 */
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

    /**
     * Creates instance of {@code VerificationRequest}.
     *
     * @param contextId    ID of the Context
     * @param senderId     ID of the sender
     * @param senderPubKey Public key of the sender
     * @param date         Creation date of the data
     */
    public VerificationRequest(String contextId, String senderId, String senderPubKey, Long date) {
        this.contextId = contextId;
        this.senderId = senderId;
        this.senderPubKey = senderPubKey;
        this.date = date;
    }

    public VerificationRequest() {
    }
};