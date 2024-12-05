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
 * Provides a set of predefined values for configuring container policies.
 */
public class ContainerPolicyValues {

    /**
     * Uses the default value provided by the Bridge.
     */
    public static final ContainerPolicyValue DEFAULT = new ContainerPolicyValue("default");

    /**
     * Uses the value inherited from the Context.
     */
    public static final ContainerPolicyValue INHERIT = new ContainerPolicyValue("inherit");

    /**
     * Prevents actions from being performed on container.
     */
    public static final ContainerPolicyValue NONE = new ContainerPolicyValue("none");

    /**
     * Grants access to the all Context users, allowing them to perform actions on container.
     */
    public static final ContainerPolicyValue ALL = new ContainerPolicyValue("all");

    /**
     * Grants access to the users, allowing them to perform actions on container.
     */
    public static final ContainerPolicyComplexValue USER = new ContainerPolicyComplexValue("user");

    /**
     * Grants access to the managers, allowing them to perform actions on container.
     */
    public static final ContainerPolicyComplexValue MANAGER = new ContainerPolicyComplexValue("manager");

    /**
     * Grants access to the container's owner, allowing them to perform actions on container.
     */
    public static final ContainerPolicyComplexValue OWNER = new ContainerPolicyComplexValue("owner");
}
