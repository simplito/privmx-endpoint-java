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

public class DataChannelMessage {
    public final byte[] data;
    public final long seq;

    public DataChannelMessage(
            byte[] data,
            long seq
    ){
        this.data = data;
        this.seq = seq;
    }
}
