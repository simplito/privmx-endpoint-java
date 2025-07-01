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
 * Contains Information about user.
 *
 * @category core
 * @group Core
 */
public class UserInfo {
    /**
     * User publicKey and userId
     */
    public UserWithPubKey user;

    /**
     * is user connected to bridge
     */
    public boolean isActive;

    /**
     * Creates instance of {@code UserInfo}
     *
     * @param user     User publicKey and userId
     * @param isActive is user connected to bridge
     */
    public UserInfo(
            UserWithPubKey user,
            boolean isActive
    ) {
        this.user = user;
        this.isActive = isActive;
    }
}