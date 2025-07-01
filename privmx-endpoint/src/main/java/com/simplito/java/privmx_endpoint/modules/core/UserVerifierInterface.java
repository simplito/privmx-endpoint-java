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

package com.simplito.java.privmx_endpoint.modules.core;

import com.simplito.java.privmx_endpoint.model.VerificationRequest;

import java.util.List;

/**
 * UserVerifierInterface - an interface consisting of a single verify() method, which - when implemented - should perform verification of the provided data using an external service verification
 * should be done using an external service such as an application server or a PKI server.
 */
public interface UserVerifierInterface {
    /**
     * Verifies whether the specified users are valid.
     * Checks if each user belonged to the Context and if this is their key in `date` and return `true` or `false` otherwise.
     *
     * @param request List of user data to verification
     * @return List of verification results whose items correspond to the items in the input list
     */
    List<Boolean> verify(List<VerificationRequest> request);
}