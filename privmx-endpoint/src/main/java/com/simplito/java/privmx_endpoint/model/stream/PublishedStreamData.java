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

public class PublishedStreamData {
    public String streamRoomId;
    public StreamInfo stream;
    public String userId;

    public PublishedStreamData(String streamRoomId, StreamInfo stream, String userId) {
        this.streamRoomId = streamRoomId;
        this.stream = stream;
        this.userId = userId;
    }
}
