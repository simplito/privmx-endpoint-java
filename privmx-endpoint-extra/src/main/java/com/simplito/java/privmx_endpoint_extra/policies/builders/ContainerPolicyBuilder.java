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

/**
 * Builder for creating instances of {@link ContainerPolicyWithoutItem} and {@link ContainerPolicy}.
 */
public class ContainerPolicyBuilder {
    private String get;
    private String update;
    private String delete;
    private String updatePolicy;
    private String updaterCanBeRemovedFromManagers;
    private String ownerCanBeRemovedFromManagers;
    private ItemPolicy item;

    /**
     * Creates instance of {@link ContainerPolicyBuilder} initialized with Bridge's default policy values.
     */
    public ContainerPolicyBuilder() {
    }

    /**
     * Creates instance of {@link ContainerPolicyBuilder}
     * initialized with policy values from existing {@link ContainerPolicy} instance.
     *
     * @param containerPolicy the existing {@link ContainerPolicy} instance to copy values from.
     */
    public ContainerPolicyBuilder(ContainerPolicy containerPolicy) {
        this.get = containerPolicy.get;
        this.update = containerPolicy.update;
        this.delete = containerPolicy.delete;
        this.updatePolicy = containerPolicy.updatePolicy;
        this.updaterCanBeRemovedFromManagers = containerPolicy.updaterCanBeRemovedFromManagers;
        this.ownerCanBeRemovedFromManagers = containerPolicy.ownerCanBeRemovedFromManagers;
        this.item = containerPolicy.item;
    }

    /**
     * Creates instance of {@link ContainerPolicyBuilder}
     * initialized with policy values from existing {@link ContainerPolicyWithoutItem} instance.
     *
     * @param containerPolicyWithoutItem the existing {@link ContainerPolicyWithoutItem} instance to copy values from.
     */
    public ContainerPolicyBuilder(ContainerPolicyWithoutItem containerPolicyWithoutItem) {
        this.get = containerPolicyWithoutItem.get;
        this.update = containerPolicyWithoutItem.update;
        this.delete = containerPolicyWithoutItem.delete;
        this.updatePolicy = containerPolicyWithoutItem.updatePolicy;
        this.updaterCanBeRemovedFromManagers = containerPolicyWithoutItem.updaterCanBeRemovedFromManagers;
        this.ownerCanBeRemovedFromManagers = containerPolicyWithoutItem.ownerCanBeRemovedFromManagers;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#get} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setGet(ContainerPolicyValue policyValue) {
        this.get = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#update} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setUpdate(ContainerPolicyValue policyValue) {
        this.update = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#delete} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setDelete(ContainerPolicyValue policyValue) {
        this.delete = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#updatePolicy} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setUpdatePolicy(ContainerPolicyValue policyValue) {
        this.updatePolicy = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#updaterCanBeRemovedFromManagers} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setUpdaterCanBeRemovedFromManagers(SpecialPolicyValue policyValue) {
        this.updaterCanBeRemovedFromManagers = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicyWithoutItem#ownerCanBeRemovedFromManagers} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setOwnerCanBeRemovedFromManagers(SpecialPolicyValue policyValue) {
        this.ownerCanBeRemovedFromManagers = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ContainerPolicy#item} items policy value.
     *
     * @param item policy value to set
     * @return {@link ContainerPolicyBuilder} instance to allow for method chaining.
     */
    public ContainerPolicyBuilder setItem(ItemPolicy item) {
        this.item = item;
        return this;
    }

    /**
     * Creates {@link ContainerPolicyWithoutItem} from current state.
     *
     * @return new {@link ContainerPolicyWithoutItem} instance created from this builder policies.
     */
    public ContainerPolicyWithoutItem buildWithoutItem() {
        return new ContainerPolicyWithoutItem(
                get,
                update,
                delete,
                updatePolicy,
                updaterCanBeRemovedFromManagers,
                ownerCanBeRemovedFromManagers
        );
    }

    /**
     * Creates {@link ContainerPolicy} from current state.
     *
     * @return new {@link ContainerPolicy} instance created from this builder policies.
     */
    public ContainerPolicy build() {
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
