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

package com.simplito.java.privmx_endpoint.model;

public class TurnCredentials {
    public String url;
    public String username;
    public String password;
    public Long expirationTime;

    public TurnCredentials(
            String url,
            String username,
            String password,
            Long expirationTime
    ) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.expirationTime = expirationTime;
    }
}
