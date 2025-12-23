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

package com.simplito.java.privmx_endpoint_streams.model;

import com.simplito.java.privmx_endpoint.model.StreamTrackInfo;

import java.util.List;

public class StreamInfo {
    public Long id;
    public String userId;
    public List<StreamTrackInfo> tracks;
    public Long streamId;
    public String metadata;     // optional
    public Boolean dummy;       // optional
    public Boolean talking;     // optional


    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, Long streamId) {
        this(id, userId, tracks, streamId, null, null, null);
    }

    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, Long streamId, String metadata) {
        this(id, userId, tracks, streamId, metadata, null, null);
    }

    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, Long streamId, String metadata, Boolean dummy) {
        this(id, userId, tracks, streamId, metadata, dummy, null);
    }

    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, Long streamId, String metadata, Boolean dummy, Boolean talking) {
        this.id = id;
        this.userId = userId;
        this.tracks = tracks;
        this.streamId = streamId;
        this.metadata = metadata;
        this.dummy = dummy;
        this.talking = talking;
    }
}
