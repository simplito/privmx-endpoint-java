//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model.exceptions;

/**
 * Thrown when a PrivMX Endpoint method encounters an unknown exception.
 *
 * @category errors
 */
public class NativeException extends RuntimeException {
    /**
     * Initialize exception with passed message.
     *
     * @param message information about the exception
     */
    NativeException(String message) {
        super(message);
    }
}