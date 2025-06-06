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

package com.simplito.java.privmx_endpoint.modules.core;

import com.simplito.java.privmx_endpoint.model.Event;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;

/**
 * Defines methods to working with Events queue.
 */
public class EventQueue {
    private EventQueue() {
    }

    /**
     * Puts the break event on the events queue.
     * You can use it to break the {@link #waitEvent()}.
     *
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     */
    public static native void emitBreakEvent() throws PrivmxException, NativeException;

    /**
     * Waits for event on current thread.
     *
     * @return Caught event
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     */
    public static native Event<?> waitEvent() throws PrivmxException, NativeException;

    /**
     * Gets the first event from the events queue.
     *
     * @return Event data if any available otherwise return null
     * @throws PrivmxException thrown when method encounters an exception.
     * @throws NativeException thrown when method encounters an unknown exception.
     */
    public static native Event<?> getEvent() throws PrivmxException, NativeException;
}