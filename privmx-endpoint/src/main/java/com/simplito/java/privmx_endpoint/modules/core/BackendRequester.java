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

package com.simplito.java.privmx_endpoint.modules.core;

import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;

/**
 * Defines methods sending requests to PrivMX Bridge API.
 */
public class BackendRequester {
    private BackendRequester() {
    }

    /**
     * Sends a request to PrivMX Bridge API using access token for authorization.
     *
     * @param serverUrl    PrivMX Bridge server URL
     * @param accessToken  token for authorization (see PrivMX Bridge API for more details)
     * @param method       API method to call
     * @param paramsAsJson API method's parameters in JSON format
     * @return JSON string representing raw server response
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     */
    public static native String backendRequest(
            String serverUrl,
            String accessToken,
            String method,
            String paramsAsJson
    ) throws PrivmxException, NativeException;

    /**
     * Sends request to PrivMX Bridge API.
     *
     * @param serverUrl    PrivMX Bridge server URL
     * @param method       API method to call
     * @param paramsAsJson API method's parameters in JSON format
     * @return JSON string representing raw server response
     */
    public static native String backendRequest(
            String serverUrl,
            String method,
            String paramsAsJson
    );

    /**
     * Sends a request to PrivMX Bridge API using pair of API KEY ID and API KEY SECRET for authorization.
     *
     * @param serverUrl    PrivMX Bridge server URL
     * @param apiKeyId     API KEY ID (see PrivMX Bridge API for more details)
     * @param apiKeySecret API KEY SECRET (see PrivMX Bridge API for more details)
     * @param mode         allows you to set whether the request should be signed (mode = 1) or plain (mode = 0)
     * @param method       API method to call
     * @param paramsAsJson API method's parameters in JSON format
     * @return JSON string representing raw server response
     */
    public static native String backendRequest(
            String serverUrl,
            String apiKeyId,
            String apiKeySecret,
            long mode,
            String method,
            String paramsAsJson
    );
}