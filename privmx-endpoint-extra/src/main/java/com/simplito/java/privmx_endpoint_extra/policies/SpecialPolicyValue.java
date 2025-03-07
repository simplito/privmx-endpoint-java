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
 * Contains special policies values.
 */
public class SpecialPolicyValue extends PolicyValue {

    /**
     * Creates instance of {@link SpecialPolicyValue}.
     *
     * @param value raw policy value
     */
    SpecialPolicyValue(String value) {
        super(value);
    }

    /**
     * Uses the default value provided by the Bridge.
     */
    public static final SpecialPolicyValue DEFAULT = new SpecialPolicyValue("default");

    /**
     * Uses the inherited value.
     */
    public static final SpecialPolicyValue INHERIT = new SpecialPolicyValue("inherit");

    /**
     * Allows to perform an action.
     */
    public static final SpecialPolicyValue YES = new SpecialPolicyValue("yes");

    /**
     * Denies to perform an action.
     */
    public static final SpecialPolicyValue NO = new SpecialPolicyValue("no");
}
