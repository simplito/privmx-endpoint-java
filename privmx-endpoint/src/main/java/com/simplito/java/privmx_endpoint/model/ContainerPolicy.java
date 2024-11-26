//
// PrivMX Endpoint Java.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model;

public class ContainerPolicy extends ContainerPolicyWithoutItem {
    public final ItemPolicy item;
    
    public ContainerPolicy(
            String get,
            String update,
            String delete,
            String updatePolicy,
            String updaterCanBeRemovedFromManagers,
            String ownerCanBeRemovedFromManagers,
            ItemPolicy item
    ) {
        super(
                get,
                update,
                delete,
                updatePolicy,
                updaterCanBeRemovedFromManagers,
                ownerCanBeRemovedFromManagers
        );
        this.item = item;
    }
}
