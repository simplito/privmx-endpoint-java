//
// PrivMX Endpoint Java Extra.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.policies;

/**
 * Provides a set of predefined values for configuring item policies within a Container.
 */
public class ItemPolicyValues {

    /**
     * Uses the default value provided by the Bridge.
     */
    public static final ItemPolicyValue DEFAULT = new ItemPolicyValue("default");

    /**
     * Prevents actions from being performed on item.
     */
    public static final ItemPolicyValue NONE = new ItemPolicyValue("none");

    /**
     * Grants access to the all Context users, allowing them to perform actions on item.
     */
    public static final ItemPolicyValue ALL = new ItemPolicyValue("all");

    /**
     * Grants access to the users, allowing them to perform actions on item.
     */
    public static final ItemPolicyComplexValue USER = new ItemPolicyComplexValue("user");

    /**
     * Grants access to the managers, allowing them to perform actions on item.
     */
    public static final ItemPolicyComplexValue MANAGER = new ItemPolicyComplexValue("manager");

    /**
     * Grants access to the container's owner, allowing them to perform actions on item.
     */
    public static final ItemPolicyComplexValue OWNER = new ItemPolicyComplexValue("owner");

    /**
     * Grants access to the item's owner, allowing them to perform actions on item.
     */
    public static final ItemPolicyComplexValue ITEM_OWNER = new ItemPolicyComplexValue("itemOwner");
}
