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

package com.simplito.java.privmx_endpoint.model;

public class Stream {
    public Long streamId;
    public String userId;

    public Stream(
            Long streamId,
            String userId
    ) {
        this.streamId = streamId;
        this.userId = userId;
    }
}
