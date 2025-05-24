package com.simplito.java.privmx_endpoint.modules.crypto;

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;

public class ExtKey implements AutoCloseable {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    private final Long key;

    public ExtKey(Long key) {
        this.key = key;
    }

    /**
     * Creates ExtKey from given seed.
     *
     * @param seed the seed used to generate Key
     * @return ExtKey object
     */
    public static native ExtKey fromSeed(byte[] seed) throws PrivmxException, NativeException;

    /**
     * Decodes ExtKey from Base58 format.
     *
     * @param base58 the ExtKey in Base58
     * @return ExtKey object
     */
    public static native ExtKey fromBase58(String base58) throws PrivmxException, NativeException;

    /**
     * Generates a new ExtKey.
     *
     * @return ExtKey object
     */
    public static native ExtKey generateRandom() throws PrivmxException, NativeException;

    private native void deinit() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Generates child ExtKey from a current ExtKey using BIP32.
     *
     * @param index number from 0 to 2^31-1
     * @return ExtKey object
     */
    public native ExtKey derive(int index) throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Generates hardened child ExtKey from a current ExtKey using BIP32.
     *
     * @param index number from 0 to 2^31-1
     * @return ExtKey object
     */
    public native ExtKey deriveHardened(int index) throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Converts ExtKey to Base58 string.
     *
     * @return ExtKey in Base58 format
     */
    public native String getPrivatePartAsBase58() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Converts the public part of ExtKey to Base58 string.
     *
     * @return ExtKey in Base58 format
     */
    public native String getPublicPartAsBase58() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Extracts ECC PrivateKey.
     *
     * @return ECC key in WIF format
     */
    public native String getPrivateKey() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Extracts ECC PublicKey.
     *
     * @return ECC key in BASE58DER format
     */
    public native String getPublicKey() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Extracts raw ECC PrivateKey.
     *
     * @return ECC PrivateKey
     */
    public native byte[] getPrivateEncKey() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Extracts ECC PublicKey Address.
     *
     * @return ECC Address in BASE58 format
     */
    public native String getPublicKeyAsBase58Address() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Gets the chain code of Extended Key.
     *
     * @return Raw chain code
     */
    public native byte[] getChainCode() throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Validates a signature of a message.
     *
     * @param message   data used on validation
     * @param signature signature of data to verify
     * @return message validation result
     */
    public native boolean verifyCompactSignatureWithHash(byte[] message, byte[] signature) throws IllegalStateException, PrivmxException, NativeException;

    /**
     * Checks if ExtKey is Private.
     *
     * @return returns true if ExtKey is private
     */
    public native boolean isPrivate() throws IllegalStateException, PrivmxException, NativeException;

    @Override
    public void close() throws Exception {
        deinit();
    }
}