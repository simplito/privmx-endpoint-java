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

package com.simplito.java.privmx_endpoint.model.stream.events.eventTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType;

public enum StreamEventType implements EventType {
    STREAMROOM_CREATE,
    STREAMROOM_UPDATE,
    STREAMROOM_DELETE,
    EMPTY,
    STREAM_JOIN,
    STREAM_LEAVE,
    STREAM_PUBLISH,
    STREAM_UNPUBLISH
}


