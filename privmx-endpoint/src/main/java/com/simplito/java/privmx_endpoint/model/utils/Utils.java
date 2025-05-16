package com.simplito.java.privmx_endpoint.model.utils;

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
     * Splits string by given delimiter (delimiter is removed).
     *
     * @param data      string to split
     * @param delimiter string which will be split
     * @return List containing all split parts
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