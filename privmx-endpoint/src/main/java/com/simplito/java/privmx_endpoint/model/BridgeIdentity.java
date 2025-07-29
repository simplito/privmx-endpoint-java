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
 * Bridge server identification details.
 *
 * @category core
 * @group Core
 */
public class BridgeIdentity {
    /**
     * Bridge URL.
     */
    public String url;

    /**
     * Bridge public Key.
     */
    public String pubKey;

    /**
     * Bridge instance Id given by PKI.
     */
    public String instanceId;

    /**
     * Creates instance of {@code BridgeIdentity}.
     *
     * @param url Bridge URL
     */
    public BridgeIdentity(String url) {
        this.url = url;
    }

    /**
     * Creates instance of {@code BridgeIdentity}.
     *
     * @param url        Bridge URL
     * @param pubKey     Bridge public Key
     * @param instanceId Bridge instance Id given by PKI
     */
    public BridgeIdentity(String url, String pubKey, String instanceId) {
        this.url = url;
        this.pubKey = pubKey;
        this.instanceId = instanceId;
    }
}