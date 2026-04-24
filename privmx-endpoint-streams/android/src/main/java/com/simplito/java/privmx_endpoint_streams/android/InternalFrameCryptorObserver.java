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

import androidx.annotation.Nullable;

import org.webrtc.AudioTrack;
import org.webrtc.MediaStreamTrack;
import org.webrtc.PmxFrameCryptor;

class InternalFrameCryptorObserver implements PmxFrameCryptor.Observer{
    final @Nullable String streamId;
    final boolean isAudio;
    final RmsObserver rmsObserver;

    InternalFrameCryptorObserver(
            MediaStreamTrack track,
            @Nullable String streamId,
            @Nullable RmsObserver rmsObserver
    ){
        this.streamId = streamId;
        this.rmsObserver = rmsObserver;
        isAudio = track instanceof AudioTrack;
    }

    @Override
    public void onFrameCryptionStateChanged(PmxFrameCryptor.PmxFrameCryptionState newState) {

    }

    @Override
    public void onFrameRms(byte rms) {
        if(isAudio && rmsObserver != null){
            rmsObserver.onRms(streamId,rms,System.currentTimeMillis());
        }
    }
}
