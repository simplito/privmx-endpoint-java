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

package com.simplito.java.privmx_endpoint.modules.stream;

import com.simplito.java.privmx_endpoint.model.Key;

import java.util.List;

public interface WebRTCInterface {
    String createOfferAndSetLocalDescription();

    String createAnswerAndSetDescriptions(String sdp, String type);

    void setAnswerAndSetRemoteDescription(String sdp, String type);

    void close();

    void updateKeys(List<Key> keys);
}
