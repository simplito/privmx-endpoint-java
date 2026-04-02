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

public class StreamTrackInfo {
    public String type;
    public Long mindex;
    public String mid;

    public Boolean disabled;        // optional
    public String codec;            // optional
    public String description;      // optional
    public Boolean moderated;       // optional
    public Boolean simulcast;       // optional
    public Boolean talking;         // optional

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid
    ) {
        this(type, mindex, mid, null, null, null, null, null, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled
    ) {
        this(type, mindex, mid, disabled, null, null, null, null, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec
    ) {
        this(type, mindex, mid, disabled, codec, null, null, null, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description
    ) {
        this(type, mindex, mid, disabled, codec, description, null, null, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description,
            Boolean moderated
    ) {
        this(type, mindex, mid, disabled, codec, description, moderated, null, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description,
            Boolean moderated,
            Boolean simulcast
    ) {
        this(type, mindex, mid, disabled, codec, description, moderated, simulcast, null);
    }

    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description,
            Boolean moderated,
            Boolean simulcast,
            Boolean talking
    ) {
        this.type = type;
        this.mindex = mindex;
        this.mid = mid;
        this.disabled = disabled;
        this.codec = codec;
        this.description = description;
        this.moderated = moderated;
        this.simulcast = simulcast;
        this.talking = talking;
    }
}
