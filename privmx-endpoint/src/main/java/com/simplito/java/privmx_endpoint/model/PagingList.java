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

import java.util.List;

/**
 * Contains results of listing methods.
 *
 * @param <T> type of items stored in list.
 * @category core
 * @group Core
 */
@SuppressWarnings("CanBeFinal")
public class PagingList<T> {
    /**
     * Total items available to get.
     */
    public Long totalAvailable;

    /**
     * List of items read during single method call.
     */
    public List<T> readItems;

    /**
     * Creates instance of {@code PagingList}.
     *
     * @param totalAvailable Total items available to get.
     * @param items          List of items read during single method call.
     */
    public PagingList(Long totalAvailable, List<T> items) {
        this.totalAvailable = totalAvailable;
        this.readItems = items;
    }
}
