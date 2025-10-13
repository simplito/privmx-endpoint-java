//
// PrivMX Endpoint Java Extra.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.events;

public class CallbackRegistration<T> {
    public Object callbackGroup;
    public EventCallback<T> callback;
    public EventType<T> eventType;

    public CallbackRegistration(
            Object callbackGroup,
            EventType<T> eventType,
            EventCallback<T> callback
    ) {
        this.callbackGroup = callbackGroup;
        this.callback = callback;
        this.eventType = eventType;
    }
}
