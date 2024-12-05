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


public class ContainerPolicyValues {
    public static final ContainerPolicyValue DEFAULT = new ContainerPolicyValue("default");
    public static final ContainerPolicyValue INHERIT = new ContainerPolicyValue("inherit");
    public static final ContainerPolicyValue NONE = new ContainerPolicyValue("none");
    public static final ContainerPolicyValue ALL = new ContainerPolicyValue("all");
    public static final ContainerPolicyComplexValue USER = new ContainerPolicyComplexValue("user");
    public static final ContainerPolicyComplexValue MANAGER = new ContainerPolicyComplexValue("manager");
    public static final ContainerPolicyComplexValue OWNER = new ContainerPolicyComplexValue("owner");
}
