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
 * Thrown when a PrivMX Endpoint method encounters an exception.
 *
 * @category errors
 */
public class PrivmxException extends RuntimeException {

    /**
     * Scope of the exception.
     */
    public final String scope;
    /**
     * Detailed description of the exception.
     */
    public final String description;
    /**
     * Native Exception name.
     */
    public final String name;
    /**
     * Code of the exception.
     */
    private final int code;

    /**
     * Creates instance of {@code PrivmxException}.
     *
     * @param message short information about exception
     * @param scope   scope of this exception
     * @param code    unique code of this exception
     */
    PrivmxException(String message, String scope, int code) {
        this(message, null, scope, code, "");
    }

    /**
     * Creates instance of {@code PrivmxException}.
     *
     * @param message     short information about exception
     * @param description information about exception
     * @param scope       scope of this exception
     * @param code        unique code of this exception
     */
    PrivmxException(String message, String description, String scope, int code) {
        this(message, description, scope, code, "");
    }

    /**
     * Creates instance of {@code PrivmxException}.
     *
     * @param message     brief  information about exception
     * @param description detailed information about exception
     * @param scope       scope of this exception
     * @param code        unique code of this exception
     * @param name        special name for this exception
     */
    @SuppressWarnings("SameParameterValue")
    PrivmxException(String message, String description, String scope, int code, String name) {
        super(message);
        this.scope = scope;
        this.code = code;
        this.description = description;
        this.name = name;
    }

    /**
     * Returns full information about the exception.
     * <p>
     * See: {@link #getFull}.
     *
     * @return Full information about exception
     */
    @Override
    public String toString() {
        return getFull();
    }

    /**
     * Returns exception code as {@code unsigned int} converted to {@code long}.
     *
     * @return Exception code
     */
    public long getCode() {
        return Integer.toUnsignedLong(code);
    }

    /**
     * Returns full information about exception.
     *
     * @return Full information about exception
     */
    public String getFull() {
        return "{\n" +
                "\"name\" : \"" + name + "\",\n" +
                "\"scope\" : \"" + scope + "\",\n" +
                "\"msg\" : \"" + getMessage() + "\",\n" +
                "\"code\" : " + getCode() + ",\n" +
                "\"description\" : \"" + description + "\"\n" +
                "}\n";
    }
}