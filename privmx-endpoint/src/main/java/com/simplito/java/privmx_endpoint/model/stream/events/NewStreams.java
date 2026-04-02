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

package com.simplito.java.privmx_endpoint.model.stream.events;


import com.simplito.java.privmx_endpoint.model.stream.StreamInfo;

import java.util.List;

public class NewStreams {
    public String room;
    public List<StreamInfo> streams;
    public NewStreams(String room, List<StreamInfo> streams) {
        this.room = room;
        this.streams = streams;
    }
}
