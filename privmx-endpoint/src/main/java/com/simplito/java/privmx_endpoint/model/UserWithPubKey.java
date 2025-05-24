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

package com.simplito.java.privmx_endpoint.model;

/**
 * Contains ID of user and the corresponding public key.
 *
 * @category core
 * @group Core
 */
public class UserWithPubKey {

    /**
     * ID of the user.
     */
    public String userId;

    /**
     * User's public key.
     */
    public String pubKey;

    /**
     * Creates instance of {@code UserWithPubKey}.
     */
    public UserWithPubKey() {
    }

    /**
     * Creates instance of {@code UserWithPubKey}.
     *
     * @param userId ID of the user.
     * @param pubKey User's public key.
     */
    public UserWithPubKey(
            String userId,
            String pubKey
    ) {
        this.userId = userId;
        this.pubKey = pubKey;
    }
}