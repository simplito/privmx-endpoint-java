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

package com.simplito.java.privmx_endpoint.model.stream;

import java.util.List;

/**
 * Contains detailed information about a stream and its associated tracks.
 */
public class StreamInfo {
    /**
     * Unique identifier of the stream
     */
    public Long id;

    /**
     * Identifier of the user who published the stream
     */
    public String userId;

    /**
     * Information about the tracks within the stream
     */
    public List<StreamTrackInfo> tracks;

    /**
     * Additional metadata attached to the stream as a JSON string
     */
    public String metadata;     // optional

    public Boolean dummy;       // optional

    /**
     * Constructs a new {@link StreamInfo} instance.
     *
     * @param id     Unique identifier of the stream
     * @param userId Identifier of the user who published the stream
     * @param tracks Information about the tracks within the stream
     */
    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks) {
        this(id, userId, tracks, null, null);
    }

    /**
     * Constructs a new {@link StreamInfo} instance.
     *
     * @param id       Unique identifier of the stream
     * @param userId   Identifier of the user who published the stream
     * @param tracks   Information about the tracks within the stream
     * @param metadata Additional metadata attached to the stream as a JSON string
     */
    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, String metadata) {
        this(id, userId, tracks, metadata, null);
    }

    /**
     * Constructs a new {@link StreamInfo} instance.
     *
     * @param id       Unique identifier of the stream
     * @param userId   Identifier of the user who published the stream
     * @param tracks   Information about the tracks within the stream
     * @param metadata Additional metadata attached to the stream as a JSON string
     * @param dummy
     */
    public StreamInfo(Long id, String userId, List<StreamTrackInfo> tracks, String metadata, Boolean dummy) {
        this.id = id;
        this.userId = userId;
        this.tracks = tracks;
        this.metadata = metadata;
        this.dummy = dummy;
    }
}
