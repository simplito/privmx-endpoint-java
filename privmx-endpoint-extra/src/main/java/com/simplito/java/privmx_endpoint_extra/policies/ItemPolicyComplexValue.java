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

public class ItemPolicyComplexValue extends ItemPolicyValue {

    ItemPolicyComplexValue(String value) {
        super(value);
    }

    public ItemPolicyComplexValue OR(ItemPolicyComplexValue policy) {
        return new ItemPolicyComplexValue(value + "," + policy.value);
    }

    public ItemPolicyComplexValue AND(ItemPolicyComplexValue policy) {
        return new ItemPolicyComplexValue(value + "&" + policy.value);
    }
}
