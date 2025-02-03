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
 * Contains container's policies.
 *
 * @category core
 * @group Core
 */
public class ContainerPolicyWithoutItem {

    /**
     * Determines who can get a container.
     */
    public final String get;

    /**
     * Determines who can update a container.
     */
    public final String update;

    /**
     * Determines who can delete a container.
     */
    public final String delete;

    /**
     * Determines who can update policy.
     */
    public final String updatePolicy;

    /**
     * Determines whether the updater can be removed from the list of managers.
     */
    public final String updaterCanBeRemovedFromManagers;

    /**
     * Determines whether the owner can be removed from the list of managers.
     */
    public final String ownerCanBeRemovedFromManagers;


    /**
     * Creates instance of {@link ContainerPolicyWithoutItem}.
     *
     * @param get                             determines who can get a container
     * @param update                          determines who can update a container
     * @param delete                          determines who can delete a container
     * @param updatePolicy                    determines who can update policy
     * @param updaterCanBeRemovedFromManagers determines whether the updater can be removed from the list of managers
     * @param ownerCanBeRemovedFromManagers   determines whether the owner can be removed from the list of managers
     */
    public ContainerPolicyWithoutItem(
            String get,
            String update,
            String delete,
            String updatePolicy,
            String updaterCanBeRemovedFromManagers,
            String ownerCanBeRemovedFromManagers
    ) {
        this.get = get;
        this.update = update;
        this.delete = delete;
        this.updatePolicy = updatePolicy;
        this.updaterCanBeRemovedFromManagers = updaterCanBeRemovedFromManagers;
        this.ownerCanBeRemovedFromManagers = ownerCanBeRemovedFromManagers;
    }
}
