//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.modules.core.utils;

import com.simplito.java.privmx_endpoint.LibLoader;

public class Hex {
    static {
        LibLoader.loadPrivmxLibraries();
    }

    public Hex() {
    }

    /**
     * Encodes byte array to a string in Hex format.
     *
     * @param data byte array to encode
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