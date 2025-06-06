package com.simplito.java.privmx_endpoint.model;

/**
 * PrivMX Bridge server instance verification options using a PKI server.
 *
 * @category core
 * @group Core
 */
public class PKIVerificationOptions {
    /**
     * Bridge public Key.
     */
    public String bridgePubKey;

    /**
     * Bridge instance Id given by PKI.
     */
    public String bridgeInstanceId;

    /**
     * Creates instance of {@code PKIVerificationOptions}.
     */
    public PKIVerificationOptions() {
    }

    /**
     * Creates instance of {@code PKIVerificationOptions}.
     *
     * @param bridgePubKey     Bridge public Key
     * @param bridgeInstanceId Bridge instance Id given by PKI
     */
    public PKIVerificationOptions(String bridgePubKey, String bridgeInstanceId) {
        this.bridgePubKey = bridgePubKey;
        this.bridgeInstanceId = bridgeInstanceId;
    }
}