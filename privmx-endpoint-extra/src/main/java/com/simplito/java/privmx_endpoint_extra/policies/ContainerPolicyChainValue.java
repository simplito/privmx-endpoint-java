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

public class ContainerPolicyChainValue extends ContainerPolicyValue {


    ContainerPolicyChainValue(String value) {
        super(value);
    }

    public ContainerPolicyChainValue OR(ContainerPolicyChainValue policy) {
        return new ContainerPolicyChainValue(value + "," + policy.value);
    }


    public ContainerPolicyChainValue AND(ContainerPolicyChainValue policy) {
        return new ContainerPolicyChainValue(value + "&" + policy.value);
    }
}
