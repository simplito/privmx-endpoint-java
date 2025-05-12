package com.simplito.java.privmx_endpoint.model.utils;

public class Base32 {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    /**
     * Encodes buffer to string in Base32 format.
     *
     * @param data buffer to encode
     * @return string in Base32 format
     */
    public static native String encode(byte[] data);

    /**
     * Decodes string in Base32 to buffer.
     *
     * @param base32_data string to decode
     * @return buffer with decoded data
     */
    public static native byte[] decode(String base32_data);

    /**
     * Checks if given string is in Base32 format.
     *
     * @param data string to check
     * @return data check result
     */
    public static native boolean is(String data);
}
