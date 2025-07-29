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

package com.simplito.java.privmx_endpoint.model;

/**
 * PrivMX Bridge server instance verification options using a PKI server.
 *
 * @category core
 * @group Core
 */
public class PKIVerificationOptions {
    /**
     * Bridge public Key.
     */
    public String bridgePubKey;

    /**
     * Bridge instance Id given by PKI.
     */
    public String bridgeInstanceId;

    /**
     * Creates instance of {@code PKIVerificationOptions}.
     */
    public PKIVerificationOptions() {
    }

    /**
     * Creates instance of {@code PKIVerificationOptions}.
     *
     * @param bridgePubKey     Bridge public Key
     * @param bridgeInstanceId Bridge instance Id given by PKI
     */
    public PKIVerificationOptions(String bridgePubKey, String bridgeInstanceId) {
        this.bridgePubKey = bridgePubKey;
        this.bridgeInstanceId = bridgeInstanceId;
    }
}