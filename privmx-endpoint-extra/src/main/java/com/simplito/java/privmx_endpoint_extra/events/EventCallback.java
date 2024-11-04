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

package com.simplito.java.privmx_endpoint_extra.events;

/**
 * Represents a callback for catching events data.
 * @param <T> type of the caught event data
 *
 * @category core
 */
public interface EventCallback<T> {

    /**
     * Called to pass data from a caught event.
     * @param event caught event data
     */
    void call(T event);

}
