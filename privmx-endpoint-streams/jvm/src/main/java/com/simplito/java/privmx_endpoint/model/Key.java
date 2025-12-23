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

public class Key {
    public String keyId;
    public byte[] key;
    public KeyType type;

    public Key(
            String keyId,
            byte[] key,
            KeyType type
    ) {
        this.keyId = keyId;
        this.key = key;
        this.type = type;
    }
}
