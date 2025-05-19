package com.simplito.java.privmx_endpoint.model;

/**
 * Bridge server identification details.
 */
public class BridgeIdentity {
    /**
     * Bridge URL.
     */
    public String url;

    /**
     * Bridge public Key.
     */
    public String pubKey;

    /**
     * Bridge instance Id given by PKI.
     */
    public String instanceId;

    /**
     * Creates instance of {@code BridgeIdentity}.
     *
     * @param url Bridge URL
     */
    public BridgeIdentity(String url) {
        this.url = url;
    }

    /**
     * Creates instance of {@code BridgeIdentity}.
     *
     * @param url        Bridge URL
     * @param pubKey     Bridge public Key
     * @param instanceId Bridge instance Id given by PKI
     */
    public BridgeIdentity(String url, String pubKey, String instanceId) {
        this.url = url;
        this.pubKey = pubKey;
        this.instanceId = instanceId;
    }
}