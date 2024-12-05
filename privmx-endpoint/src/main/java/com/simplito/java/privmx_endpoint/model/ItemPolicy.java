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
 * Contains container items policies.
 */
public class ItemPolicy {

    /**
     * Determine who can get an item.
     */
    public final String get;

    /**
     * Determine who can list items created by me.
     */
    public final String listMy;

    /**
     * Determine who can list all items.
     */
    public final String listAll;

    /**
     * Determine who can create an item.
     */
    public final String create;

    /**
     * Determine who can update an item.
     */
    public final String update;

    /**
     * Determine who can delete an item.
     */
    public final String delete;

    /**
     * Creates instance of {@link ItemPolicy}.
     *
     * @param get     determine who can get an item
     * @param listMy  determine who can list items created by me
     * @param listAll determine who can list all items
     * @param create  determine who can create an item
     * @param update  determine who can update an item
     * @param delete  determine who can delete an item
     */
    public ItemPolicy(
            String get,
            String listMy,
            String listAll,
            String create,
            String update,
            String delete
    ) {
        this.get = get;
        this.listMy = listMy;
        this.listAll = listAll;
        this.create = create;
        this.update = update;
        this.delete = delete;
    }
}
