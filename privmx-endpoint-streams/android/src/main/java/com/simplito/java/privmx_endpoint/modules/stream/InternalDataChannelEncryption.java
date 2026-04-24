//
// PrivMX Endpoint Streams Java Android.
// Copyright © 2026 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.stream.DataChannelMessage;
import com.simplito.java.privmx_endpoint.model.stream.DecryptedDataChannelMessage;

import java.nio.ByteBuffer;

class InternalDataChannelEncryption {
    final StreamApiLow streamApiLow;
    final String roomId;

    InternalDataChannelEncryption(String roomId, StreamApiLow streamApiLow) {
        this.roomId = roomId;
        this.streamApiLow = streamApiLow;
    }

    DecryptedDataChannelMessage decryptDataChannelMessage(ByteBuffer dataToDecrypt) {
        return streamApiLow.decryptDataChannelMessage(roomId, dataToDecrypt);
    }

    byte[] encryptDataChannelMessage(DataChannelMessage dataChannelMessage) {
        return streamApiLow.encryptDataChannelMessage(roomId, dataChannelMessage.data, dataChannelMessage.seq);
    }
}
