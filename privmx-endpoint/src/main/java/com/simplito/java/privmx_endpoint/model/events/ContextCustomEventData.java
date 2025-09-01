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

package com.simplito.java.privmx_endpoint.model.events;

/**
 * Holds information about emitted custom event.
 *
 * @category core
 * @group Events
 */
public class ContextCustomEventData {
    /**
     *  Context ID
     */
    public final String contextId;
    /**
     * User ID (event's sender)
     */
    public final String userId;
    /**
     * Event's actual payload
     */
    public final byte[] payload;
    /**
     * Payload decryption status
     */
    public Long statusCode;

    /**
     * Version of the event data structure and how it is encoded/encrypted
     */
    public Long schemaVersion;


    /**
     * Creates instance of {@code ContextCustomEventData}.
     *
     * @param contextId     Context ID
     * @param userId        User ID (event's sender)
     * @param payload          Event's actual payload
     * @param statusCode    Payload decryption status
     * @param schemaVersion Version of the event data structure and how it is encoded/encrypted
     */
    public ContextCustomEventData(
            String contextId,
            String userId,
            byte[] payload,
            Long statusCode,
            Long schemaVersion
    ) {
        this.contextId = contextId;
        this.userId = userId;
        this.payload = payload;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }

    /**
     * Creates instance of {@code ContextCustomEventData}.
     *
     * @param contextId     Context ID
     * @param userId        User ID (event's sender)
     * @param payload          Event's actual payload
     * @param statusCode    Payload decryption status
     */
    @Deprecated
    public ContextCustomEventData(
            String contextId,
            String userId,
            byte[] payload,
            Long statusCode
    ) {
        this.contextId = contextId;
        this.userId = userId;
        this.payload = payload;
        this.statusCode = statusCode;
        this.schemaVersion = 0L;
    }

    @Deprecated
    public ContextCustomEventData(
            String contextId,
            String userId,
            byte[] payload
    ) {
        this.contextId = contextId;
        this.userId = userId;
        this.payload = payload;
        this.statusCode = null;
        this.schemaVersion = 0L;
    }
}