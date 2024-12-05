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

public class ContainerPolicyComplexValue extends ContainerPolicyValue {


    ContainerPolicyComplexValue(String value) {
        super(value);
    }

    public ContainerPolicyComplexValue OR(ContainerPolicyComplexValue policy) {
        return new ContainerPolicyComplexValue(value + "," + policy.value);
    }


    public ContainerPolicyComplexValue AND(ContainerPolicyComplexValue policy) {
        return new ContainerPolicyComplexValue(value + "&" + policy.value);
    }
}
