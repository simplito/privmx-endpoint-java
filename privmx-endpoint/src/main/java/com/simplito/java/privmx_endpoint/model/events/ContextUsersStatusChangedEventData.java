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

import com.simplito.java.privmx_endpoint.model.UserWithAction;

import java.util.List;

/**
 * Holds data of event that arrives when the statuses of users in the Context change.
 *
 * @category core
 * @group Events
 */
public class ContextUsersStatusChangedEventData {
    /**
     * ID of the Context.
     */
    public String contextId;

    /**
     * List of users with their changed statuses.
     */
    public List<UserWithAction> users;

    /**
     * Creates instance of {@code ContextUsersStatusChangedEventData}.
     *
     * @param contextId ID of the Context
     * @param users     List of users with their changed statuses
     */
    public ContextUsersStatusChangedEventData(String contextId, List<UserWithAction> users) {
        this.contextId = contextId;
        this.users = users;
    }
}