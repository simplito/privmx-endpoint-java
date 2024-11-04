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
 * Represents a generic event caught by PrivMX Endpoint.
 * @param <T> The type of data associated with the event.
 *
 * @category core
 * @group Events
 */
public class Event<T> {
    /**
     * Type of the event.
     */
    public String type;

    /**
     * The event channel.
     */
    public String channel;


    /**
     * ID of connection for this event.
     */
    public Long connectionId;

    /**
     * The data payload associated with the event.
     * The type of this data is determined by the generic type parameter {@code T}.
     */
    public T data;

    /**
     * Creates instance of Event model.
     */
    Event() {
    }

    /**
     * Creates instance of Event model.
     *
     * @param type type of event as text
     * @param channel event channel
     * @param connectionId ID of connection for this event
     * @param data event data
     */
    public Event(
            String type,
            String channel,
            Long connectionId,
            T data
    ) {
        this.type = type;
        this.channel = channel;
        this.connectionId = connectionId;
        this.data = data;
    }
}
