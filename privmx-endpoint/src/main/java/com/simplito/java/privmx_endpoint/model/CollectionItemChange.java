//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model;

/**
 * Contains information about the changed item in the collection.
 *
 * @category core
 * @group Core
 */
public class CollectionItemChange {
    /**
     * ID of the item.
     */
    public String itemId;

    /**
     * Item change action, which can be "create", "update" or "delete".
     */
    public String action;

    /**
     * Creates instance of {@code CollectionItemChange}.
     *
     * @param itemId ID of the item
     * @param action Item change action, which can be "create", "update" or "delete"
     */
    public CollectionItemChange(String itemId, String action) {
        this.itemId = itemId;
        this.action = action;
    }
}