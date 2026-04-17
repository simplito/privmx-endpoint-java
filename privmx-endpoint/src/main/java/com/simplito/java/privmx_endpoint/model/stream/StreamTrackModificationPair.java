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

package com.simplito.java.privmx_endpoint.model.stream;

public class StreamTrackModificationPair {
    public StreamTrackInfo before;
    public StreamTrackInfo after;

    public StreamTrackModificationPair(StreamTrackInfo before, StreamTrackInfo after) {
        this.before = before;
        this.after = after;
    }

    public StreamTrackModificationPair(StreamTrackInfo before) {
        this(before, null);
    }

    public StreamTrackModificationPair() {
        this(null, null);
    }
}
