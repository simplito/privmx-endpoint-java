package com.simplito.java.privmx_endpoint.model;

import com.simplito.java.privmx_endpoint.modules.crypto.ExtKey;

public class BIP39_t {
    /**
     * BIP-39 mnemonic.
     */
    String mnemonic;
    /**
     * Ecc Key.
     */
    ExtKey ext_key;
    /**
     * BIP-39 entropy.
     */
    byte[] entropy;
}
