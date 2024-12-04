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

package com.simplito.java.privmx_endpoint_extra.policies.builders;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;
import com.simplito.java.privmx_endpoint.model.ContainerPolicyWithoutItem;
import com.simplito.java.privmx_endpoint.model.ItemPolicy;
import com.simplito.java.privmx_endpoint_extra.policies.ContainerPolicyValue;
import com.simplito.java.privmx_endpoint_extra.policies.SpecialPolicyValue;

public class ContainerPolicyBuilder {
    private String get;
    private String update;
    private String delete;
    private String updatePolicy;
    private String updaterCanBeRemovedFromManagers;
    private String ownerCanBeRemovedFromManagers;
    private ItemPolicy item;

    public ContainerPolicyBuilder() {
    }

    public ContainerPolicyBuilder(ContainerPolicy containerPolicy) {
        this.get = containerPolicy.get;
        this.update = containerPolicy.update;
        this.delete = containerPolicy.delete;
        this.updatePolicy = containerPolicy.updatePolicy;
        this.updaterCanBeRemovedFromManagers = containerPolicy.updaterCanBeRemovedFromManagers;
        this.ownerCanBeRemovedFromManagers = containerPolicy.ownerCanBeRemovedFromManagers;
        this.item = containerPolicy.item;
    }

    public ContainerPolicyBuilder(ContainerPolicyWithoutItem containerPolicyWithoutItem) {
        this.get = containerPolicyWithoutItem.get;
        this.update = containerPolicyWithoutItem.update;
        this.delete = containerPolicyWithoutItem.delete;
        this.updatePolicy = containerPolicyWithoutItem.updatePolicy;
        this.updaterCanBeRemovedFromManagers = containerPolicyWithoutItem.updaterCanBeRemovedFromManagers;
        this.ownerCanBeRemovedFromManagers = containerPolicyWithoutItem.ownerCanBeRemovedFromManagers;
    }

    ContainerPolicyBuilder setGet(ContainerPolicyValue policyValue) {
        this.get = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setUpdate(ContainerPolicyValue policyValue) {
        this.update = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setDelete(ContainerPolicyValue policyValue) {
        this.delete = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setUpdatePolicy(ContainerPolicyValue policyValue) {
        this.updatePolicy = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setUpdaterCanBeRemovedFromManagers(SpecialPolicyValue policyValue) {
        this.updaterCanBeRemovedFromManagers = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setOwnerCanBeRemovedFromManagers(SpecialPolicyValue policyValue) {
        this.ownerCanBeRemovedFromManagers = policyValue.value;
        return this;
    }

    ContainerPolicyBuilder setItem(ItemPolicy item) {
        this.item = item;
        return this;
    }

    ContainerPolicyWithoutItem buildWithoutItem() {
        return new ContainerPolicyWithoutItem(
                get,
                update,
                delete,
                updatePolicy,
                updaterCanBeRemovedFromManagers,
                ownerCanBeRemovedFromManagers
        );
    }

    ContainerPolicy build() {
        return new ContainerPolicy(
                get,
                update,
                delete,
                updatePolicy,
                updaterCanBeRemovedFromManagers,
                ownerCanBeRemovedFromManagers,
                item
        );
    }
}
