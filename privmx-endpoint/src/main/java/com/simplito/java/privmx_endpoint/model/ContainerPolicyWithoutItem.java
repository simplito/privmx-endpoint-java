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

public class ContainerPolicyWithoutItem {
    public final String get;
    public final String update;
    public final String delete;
    public final String updatePolicy;
    public final String updaterCanBeRemovedFromManagers;
    public final String ownerCanBeRemovedFromManagers;


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
