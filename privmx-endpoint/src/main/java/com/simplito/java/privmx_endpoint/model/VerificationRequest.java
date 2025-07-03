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
 * Represents a request used for verifying a sender in a specific context.
 *
 * @category core
 * @group Core
 */
public class VerificationRequest {
    /**
     * Id of the Context
     */
    public String contextId;
    /**
     * id of the sender
     */
    public String senderId;
    /**
     * Public key of the sender
     */
    public String senderPubKey;
    /**
     * The data creation date
     */
    public Long date;

    /**
     * Bridge Identity
     */
    public BridgeIdentity bridgeIdentity;

    /**
     * Creates instance of {@code VerificationRequest}.
     *
     * @param contextId    ID of the Context
     * @param senderId     ID of the sender
     * @param senderPubKey Public key of the sender
     * @param date         Creation date of the data
     */
    public VerificationRequest(String contextId, String senderId, String senderPubKey, Long date) {
        this.contextId = contextId;
        this.senderId = senderId;
        this.senderPubKey = senderPubKey;
        this.date = date;
    }

    /**
     * Creates instance of {@code VerificationRequest}.
     *
     * @param contextId      ID of the Context
     * @param senderId       ID of the sender
     * @param senderPubKey   Public key of the sender
     * @param date           Creation date of the data
     * @param bridgeIdentity Bridge Identity
     */
    public VerificationRequest(String contextId, String senderId, String senderPubKey, Long date, BridgeIdentity bridgeIdentity) {
        this(contextId, senderId, senderPubKey, date);
        this.bridgeIdentity = bridgeIdentity;
    }
}