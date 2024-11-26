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

public class ItemPolicyBuilder {
    private String get;
    private String listMy;
    private String listAll;
    private String create;
    private String update;
    private String delete;

    public ItemPolicyBuilder() {

    }

    ItemPolicyBuilder setGet(ItemPolicyValue policyValue) {
        this.get = policyValue.value;
        return this;
    }

    ItemPolicyBuilder setListMy(ItemPolicyValue policyValue) {
        this.listMy = policyValue.value;
        return this;
    }

    ItemPolicyBuilder setListAll(ItemPolicyValue policyValue) {

        this.listAll = policyValue.value;
        return this;
    }

    ItemPolicyBuilder setCreate(ItemPolicyValue policyValue) {
        this.create = policyValue.value;
        return this;
    }

    ItemPolicyBuilder setUpdate(ItemPolicyValue policyValue) {
        this.update = policyValue.value;
        return this;
    }

    ItemPolicyBuilder setDelete(ItemPolicyValue policyValue) {
        this.delete = policyValue.value;
        return this;
    }

    ItemPolicy build() {
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
