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

import com.simplito.java.privmx_endpoint.model.ItemPolicy;
import com.simplito.java.privmx_endpoint_extra.policies.ItemPolicyValue;

/**
 * Builder for creating instances of {@link ItemPolicy}.
 */
public class ItemPolicyBuilder {
    private String get;
    private String listMy;
    private String listAll;
    private String create;
    private String update;
    private String delete;

    /**
     * Creates instance of {@link ItemPolicyBuilder} initialized with Bridge's default policy values.
     */
    public ItemPolicyBuilder() {
    }

    /**
     * Creates instance of {@link ItemPolicyBuilder}
     * from existing {@link ItemPolicy} instance.
     *
     * @param itemPolicy the existing {@link ItemPolicy} instance to copy values from.
     */
    public ItemPolicyBuilder(ItemPolicy itemPolicy) {
        this.get = itemPolicy.get;
        this.listMy = itemPolicy.listMy;
        this.listAll = itemPolicy.listAll;
        this.create = itemPolicy.create;
        this.update = itemPolicy.update;
    }

    /**
     * Sets {@link ItemPolicy#get} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setGet(ItemPolicyValue policyValue) {
        this.get = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ItemPolicy#listMy} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setListMy(ItemPolicyValue policyValue) {
        this.listMy = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ItemPolicy#listAll} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setListAll(ItemPolicyValue policyValue) {

        this.listAll = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ItemPolicy#create} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setCreate(ItemPolicyValue policyValue) {
        this.create = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ItemPolicy#update} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setUpdate(ItemPolicyValue policyValue) {
        this.update = policyValue.value;
        return this;
    }

    /**
     * Sets {@link ItemPolicy#delete} policy value.
     *
     * @param policyValue policy value to set
     * @return {@link ItemPolicyBuilder} instance to allow for method chaining.
     */
    public ItemPolicyBuilder setDelete(ItemPolicyValue policyValue) {
        this.delete = policyValue.value;
        return this;
    }

    /**
     * Creates {@link ItemPolicy} from current state.
     *
     * @return new {@link ItemPolicy} instance created from this builder policies.
     */
    public ItemPolicy build() {
        return new ItemPolicy(
                get,
                listMy,
                listAll,
                create,
                update,
                delete
        );
    }
}
