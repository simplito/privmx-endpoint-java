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

package com.simplito.java.privmx_endpoint_streams.android.model;


import org.webrtc.AudioTrack;
import org.webrtc.PmxFrameCryptor;
import org.webrtc.RtpSender;

public class AudioTrackInfo {
    public AudioTrack track;
    public RtpSender sender;
    public PmxFrameCryptor frameCryptor;

    public AudioTrackInfo(
            AudioTrack track,
            RtpSender sender,
            PmxFrameCryptor frameCryptor
    ) {
        this.track = track;
        this.sender = sender;
        this.frameCryptor = frameCryptor;
    }
}