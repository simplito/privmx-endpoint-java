//
// PrivMX Endpoint Java.
// Copyright © 2026 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.model.stream.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.EventSelectorType;

public enum StreamEventSelectorType implements EventSelectorType {
    CONTEXT_ID,
    STREAMROOM_ID,
    STREAM_ID
}
