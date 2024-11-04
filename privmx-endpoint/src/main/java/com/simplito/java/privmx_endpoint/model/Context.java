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
 *
 * Contains base Context information.
 * @category core
 * @group Core
 */
public class Context {
    /**
     * ID of the user requesting information.
     */
    public final String userId;

    /**
     *  ID of the Context.
     */
    public final String contextId;

    /**
     * Creates instance of {@code Context}
     * @param userId ID of the user requesting information.
     * @param contextId ID of Context.
     */
    public Context(String userId, String contextId){
        this.userId = userId;
        this.contextId = contextId;
    }
}
