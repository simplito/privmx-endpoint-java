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

public final class BasePolicyValue extends PolicyValue {

    BasePolicyValue(String value) {
        super(value);
    }

    public static final BasePolicyValue DEFAULT = new BasePolicyValue("default");
    public static final BasePolicyValue INHERIT = new BasePolicyValue("inherit");
    public static final BasePolicyValue YES = new BasePolicyValue("yes");
    public static final BasePolicyValue NO = new BasePolicyValue("no");
}
