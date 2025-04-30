package com.simplito.java.privmx_endpoint.model;

/**
 * Options used to verify if Bridge on given url is the one you expect.
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