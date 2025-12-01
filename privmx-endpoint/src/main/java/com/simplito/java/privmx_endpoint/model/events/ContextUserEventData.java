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

import com.simplito.java.privmx_endpoint.model.UserWithPubKey;

/**
 * Contains information about a user who was added to or removed from the Context.
 *
 * @category core
 * @group Events
 */
public class ContextUserEventData {
    /**
     * ID of the Context.
     */
    public String contextId;

    /**
     * User.
     */
    public UserWithPubKey user;

    /**
     * Creates instance of {@code ContextUserEventData}.
     *
     * @param contextId ID of the Context
     * @param user      User
     */
    public ContextUserEventData(String contextId, UserWithPubKey user) {
        this.contextId = contextId;
        this.user = user;
    }
}