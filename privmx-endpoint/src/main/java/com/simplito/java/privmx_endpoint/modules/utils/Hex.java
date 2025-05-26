package com.simplito.java.privmx_endpoint.modules.utils;

public class Hex {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    public Hex() {
    }

    /**
     * Encodes byte array to a string in Hex format.
     *
     * @param data as byte array to encode
     * @return string in Hex format
     */
    public static native String encode(byte[] data);

    /**
     * Decodes string in Hex to byte array.
     *
     * @param hex_data string to decode
     * @return byte array with decoded data
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