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
 * Represents a complex value for Container policies, allowing logical combinations with other {@link ContainerPolicyComplexValue} instances.
 * These complex values enable the creation of fine-grained access control rules by combining multiple policy criteria using logical operators.
 */
public class ContainerPolicyComplexValue extends ContainerPolicyValue {

    /**
     * Creates instance of {@link ContainerPolicyComplexValue}.
     *
     * @param value raw policy value
     */
    ContainerPolicyComplexValue(String value) {
        super(value);
    }

    /**
     * Combines this policy with another policy using the logical OR operator.
     *
     * @param policy the policy to combine with this policy using OR.
     * @return A new {@link ContainerPolicyComplexValue} representing the combined policy.
     */
    public ContainerPolicyComplexValue OR(ContainerPolicyComplexValue policy) {
        return new ContainerPolicyComplexValue(value + "," + policy.value);
    }

    /**
     * Combines this policy with another policy using the logical AND operator.
     *
     * @param policy the policy to combine with this policy using AND.
     * @return A new {@link ContainerPolicyComplexValue} representing the combined policy.
     */
    public ContainerPolicyComplexValue AND(ContainerPolicyComplexValue policy) {
        return new ContainerPolicyComplexValue(value + "&" + policy.value);
    }
}
