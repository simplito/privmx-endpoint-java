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

import com.simplito.java.privmx_endpoint.modules.crypto.ExtKey;

/**
 * Class containing ECC generated key using BIP-39.
 *
 * @category crypto
 * @group Crypto
 */
public class BIP39 {
    /**
     * BIP-39 mnemonic.
     */
    public String mnemonic;
    /**
     * Ecc Key.
     */
    public ExtKey extKey;
    /**
     * BIP-39 entropy.
     */
    public byte[] entropy;

    /**
     * Creates instance of {@code BIP39}.
     *
     * @param mnemonic BIP-39 mnemonic
     * @param extKey   BIP-39 Ecc Key
     * @param entropy  BIP-39 entropy
     */
    public BIP39(String mnemonic, ExtKey extKey, byte[] entropy) {
        this.mnemonic = mnemonic;
        this.extKey = extKey;
        this.entropy = entropy;
    }
}