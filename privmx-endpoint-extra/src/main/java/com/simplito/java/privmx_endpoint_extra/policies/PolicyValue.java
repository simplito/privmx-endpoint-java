//
// PrivMX Endpoint Java Extra.
// Copyright © 2024 Simplito sp. z o.o.
//
// This file is part of the PrivMX Platform (https://privmx.dev).
// This software is Licensed under the MIT License.
//
// See the License for the specific language governing permissions and
// limitations under the License.
//

package com.simplito.java.privmx_endpoint_extra.policies;

/**
 * Base class for all policies values.
 */
public class PolicyValue {

    /**
     * Raw policy value.
     */
    public final String value;

    /**
     * Creates instance of {@link PolicyValue}.
     *
     * @param value raw policy value
     */
    PolicyValue(String value) {
        this.value = value;
    }
}
