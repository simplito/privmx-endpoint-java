package com.simplito.java.privmx_endpoint.model.utils;

public class Base64 {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    /**
     * Encodes buffer to string in Base64 format.
     *
     * @param data buffer to encode
     * @return string in Base64 format
     */
    public static native String encode(byte[] data);

    /**
     * Decodes string in Base64 to buffer.
     *
     * @param base64_data string to decode
     * @return buffer with decoded data
     */
    public static native byte[] decode(String base64_data);

    /**
     * Checks if given string is in Base64 format.
     *
     * @param data string to check
     * @return data check result
     */
    public static native boolean is(String data);
}
