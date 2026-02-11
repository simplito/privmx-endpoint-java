//
// PrivMX Endpoint Java.
// Copyright © 2025 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint.streams.model;

public class SdpWithTypeModel {
    public String sdp;
    public String type;

    public SdpWithTypeModel(
            String sdp,
            String type
    ) {
        this.sdp = sdp;
        this.type = type;
    }
}
