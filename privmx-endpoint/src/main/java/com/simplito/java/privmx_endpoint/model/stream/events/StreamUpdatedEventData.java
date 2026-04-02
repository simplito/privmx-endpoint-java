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
import com.simplito.java.privmx_endpoint.model.stream.StreamTrackModification;

import java.util.List;

public class StreamUpdatedEventData {
    public String streamRoomId;
    public List<StreamInfo> streamsAdded;
    public List<StreamInfo> streamsRemoved;
    public List<StreamTrackModification> streamsModified;

    public StreamUpdatedEventData(String streamRoomId, List<StreamInfo> streamsAdded, List<StreamInfo> streamsRemoved, List<StreamTrackModification> streamsModified) {
        this.streamRoomId = streamRoomId;
        this.streamsAdded = streamsAdded;
        this.streamsRemoved = streamsRemoved;
        this.streamsModified = streamsModified;
    }
}