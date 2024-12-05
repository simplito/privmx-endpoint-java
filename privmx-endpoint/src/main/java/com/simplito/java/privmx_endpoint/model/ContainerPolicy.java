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

/**
 * Contains container and its items policies.
 */
public class ContainerPolicy extends ContainerPolicyWithoutItem {

    /**
     * Policy for container's items.
     */
    public final ItemPolicy item;

    /**
     * Creates instance of {@link ContainerPolicy}.
     *
     * @param get                             determine who can get a container
     * @param update                          determine who can update a container
     * @param delete                          determine who can delete a container
     * @param updatePolicy                    determine who can update policy
     * @param updaterCanBeRemovedFromManagers determine whether the updater can be removed from the list of managers
     * @param ownerCanBeRemovedFromManagers   determine whether the owner can be removed from the list of managers
     * @param item                            policy for container's items
     */
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
