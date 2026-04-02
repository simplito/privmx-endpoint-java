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

package com.simplito.java.privmx_endpoint.model;

public class MediaDevice {
    public String name;
    public String id;
    public DeviceType type;

    public MediaDevice(String name, String id, DeviceType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }
}