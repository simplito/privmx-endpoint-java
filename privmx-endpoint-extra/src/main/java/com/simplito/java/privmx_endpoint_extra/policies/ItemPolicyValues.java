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

public class ItemPolicyValues {
    public static final ItemPolicyValue DEFAULT = new ItemPolicyValue("default");
    public static final ItemPolicyValue NONE = new ItemPolicyValue("none");
    public static final ItemPolicyValue ALL = new ItemPolicyValue("all");
    public static final ItemPolicyChainValue USER = new ItemPolicyChainValue("user");
    public static final ItemPolicyChainValue MANAGER = new ItemPolicyChainValue("manager");
    public static final ItemPolicyChainValue OWNER = new ItemPolicyChainValue("owner");
    public static final ItemPolicyChainValue ITEM_OWNER = new ItemPolicyChainValue("itemOwner");
}
