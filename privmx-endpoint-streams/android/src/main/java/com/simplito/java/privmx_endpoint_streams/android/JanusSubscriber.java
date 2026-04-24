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

package com.simplito.java.privmx_endpoint_streams.android;

import com.simplito.java.privmx_endpoint_streams.android.model.ConnectionType;

import org.webrtc.MediaConstraints;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.PmxKeyStore;
import org.webrtc.SessionDescription;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class JanusSubscriber extends JanusConnection {
    public JanusSubscriber(
            PeerConnectionFactory pcFactory,
            PmxKeyStore keyStore,
            TrackObserver observer,
            BiConsumer<Long, String> onTrickle
    ) {
        super(pcFactory, keyStore, ConnectionType.Subscriber, observer, onTrickle,null);
    }

    public String createAnswer(String offerSdp, String type) {
        CompletableFuture<SessionDescription> res = new CompletableFuture<>();
        peerConnection.setRemoteDescription(new SdpObserver(null), new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), offerSdp));
        peerConnection.createAnswer(new SdpObserver(res), new MediaConstraints());
        try {
            SessionDescription answer = res.get();
            peerConnection.setLocalDescription(new SdpObserver(null), answer);
            return answer.description;
        } catch (Exception e) {
            throw new RuntimeException("Cannot create answer");
        }
    }
}
