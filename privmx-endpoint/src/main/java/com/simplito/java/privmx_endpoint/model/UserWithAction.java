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
 * Contains the user with their status change action.
 *
 * @category core
 * @group Core
 */
public class UserWithAction {
    /**
     * User.
     */
    public UserWithPubKey user;

    /**
     * User status change action, which can be "login" or "logout".
     */
    public String action;

    /**
     * Creates instance of {@code UserWithAction}.
     *
     * @param user   User
     * @param action User status change action
     */
    public UserWithAction(UserWithPubKey user, String action) {
        this.user = user;
        this.action = action;
    }
}