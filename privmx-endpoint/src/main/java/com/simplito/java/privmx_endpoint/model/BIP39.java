package com.simplito.java.privmx_endpoint.model;

import com.simplito.java.privmx_endpoint.modules.crypto.ExtKey;

public class BIP39 {
    /**
     * BIP-39 mnemonic.
     */
    String mnemonic;
    /**
     * Ecc Key.
     */
    ExtKey extKey;
    /**
     * BIP-39 entropy.
     */
    byte[] entropy;

    /**
     * Creates instance of {@code BIP39}.
     *
     * @param mnemonic BIP-39 mnemonic
     * @param extKey   BIP-39 mnemonic
     * @param entropy  BIP-39 entropy
     */
    public BIP39(String mnemonic, ExtKey extKey, byte[] entropy) {
        this.mnemonic = mnemonic;
        this.extKey = extKey;
        this.entropy = entropy;
    }
}