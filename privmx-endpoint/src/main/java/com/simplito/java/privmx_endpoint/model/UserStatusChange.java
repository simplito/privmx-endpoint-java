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
 * Contains information about the change of user status.
 */
public class UserStatusChange {
    /**
     * User status change action, which can be "login" or "logout".
     */
    public String action;

    /**
     * Timestamp of the change.
     */
    public Long timestamp;

    /**
     * Creates instance of {@code UserStatusChange}
     *
     * @param action    User status change action, which can be "login" or "logout"
     * @param timestamp Timestamp of the change
     */
    public UserStatusChange(String action, Long timestamp) {
        this.action = action;
        this.timestamp = timestamp;
    }
}