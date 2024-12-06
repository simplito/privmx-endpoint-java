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
     * Determines who can get an item.
     */
    public final String get;

    /**
     * Determines who can list items created by themselves.
     */
    public final String listMy;

    /**
     * Determines who can list all items.
     */
    public final String listAll;

    /**
     * Determines who can create an item.
     */
    public final String create;

    /**
     * Determines who can update an item.
     */
    public final String update;

    /**
     * Determines who can delete an item.
     */
    public final String delete;

    /**
     * Creates instance of {@link ItemPolicy}.
     *
     * @param get     determines who can get an item
     * @param listMy  determines who can list items created by themselves
     * @param listAll determines who can list all items
     * @param create  determines who can create an item
     * @param update  determines who can update an item
     * @param delete  determines who can delete an item
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
