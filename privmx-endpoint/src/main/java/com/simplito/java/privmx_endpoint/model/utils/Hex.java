package com.simplito.java.privmx_endpoint.model.utils;

public class Hex {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    /**
     * Encodes buffer to a string in Hex format.
     *
     * @param data buffer to encode
     * @return string in Hex format
     */
    public static native String encode(byte[] data);

    /**
     * Decodes string in Hex to buffer.
     *
     * @param hex_data string to decode
     * @return buffer with decoded data
     */
    public static native byte[] decode(String hex_data);

    /**
     * Checks if given string is in Hex format.
     *
     * @param data string to check
     * @return data check result
     */
    public static native boolean is(String data);
}
