package com.simplito.java.privmx_endpoint.modules.core;

public class Base64 {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    /**
     * Encodes byte array to string in Base64 format.
     *
     * @param data as byte array to encode
     * @return string in Base64 format
     */
    public static native String encode(byte[] data);

    /**
     * Decodes string in Base64 to byte array.
     *
     * @param base64_data string to decode
     * @return byte array with decoded data
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