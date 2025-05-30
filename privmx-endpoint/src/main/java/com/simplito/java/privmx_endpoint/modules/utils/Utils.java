package com.simplito.java.privmx_endpoint.modules.utils;

import java.util.List;

public class Utils {
    static {
        System.loadLibrary("privmx-endpoint-java");
    }

    /**
     * Removes all trailing whitespace.
     *
     * @param data
     * @return copy of string with removed trailing whitespace
     */
    public static native String trim(String data);

    /**
     * Splits a string using the provided delimiter.
     *
     * @param data      the string to be split
     * @param delimiter string which will be split
     * @return split parts
     */
    public static native List<String> split(String data, String delimiter);

    /**
     * Removes all whitespace from the left of given string.
     *
     * @param data reference to string
     * @return copy of string without whitespace at the beginning
     */
    public static native String ltrim(String data);

    /**
     * Removes all whitespace from the right of given string.
     *
     * @param data string to check
     * @return copy of string without whitespace at the end
     */
    public static native String rtrim(String data);
}