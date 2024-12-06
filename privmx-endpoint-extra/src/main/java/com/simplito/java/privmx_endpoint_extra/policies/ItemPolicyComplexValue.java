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
 * Represents a complex value for Container item policies, allowing logical combinations with other {@link ItemPolicyComplexValue} instances.
 * These complex values enable the creation of fine-grained access control rules by combining multiple policy criteria using logical operators.
 */
public class ItemPolicyComplexValue extends ItemPolicyValue {

    /**
     * Creates instance of {@link ItemPolicyComplexValue}
     *
     * @param value raw policy value
     */
    ItemPolicyComplexValue(String value) {
        super(value);
    }

    /**
     * Combines this policy with another policy using the logical OR operator.
     *
     * @param policy the policy to combine with this policy using OR.
     * @return A new {@link ItemPolicyComplexValue} representing the combined policy.
     */
    public ItemPolicyComplexValue OR(ItemPolicyComplexValue policy) {
        return new ItemPolicyComplexValue(value + "," + policy.value);
    }

    /**
     * Combines this policy with another policy using the logical AND operator.
     *
     * @param policy the policy to combine with this policy using AND.
     * @return A new {@link ItemPolicyComplexValue} representing the combined policy.
     */
    public ItemPolicyComplexValue AND(ItemPolicyComplexValue policy) {
        return new ItemPolicyComplexValue(value + "&" + policy.value);
    }
}
