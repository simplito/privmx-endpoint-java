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

package com.simplito.java.privmx_endpoint.modules.crypto;

import com.simplito.java.privmx_endpoint.model.BIP39_t;
import com.simplito.java.privmx_endpoint.model.exceptions.NativeException;
import com.simplito.java.privmx_endpoint.model.exceptions.PrivmxException;

/**
 * Defines cryptographic methods.
 *
 * @category crypto
 */
public class CryptoApi implements AutoCloseable {
    static {
        System.loadLibrary("crypto");
        System.loadLibrary("ssl");
        System.loadLibrary("privmx-endpoint-java");
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final Long api;

    /**
     * Create instance of {@code CryptoApi}.
     */
    public CryptoApi() {
        api = init();
    }

    private native Long init();

    private native void deinit() throws IllegalStateException;

    /**
     * Generates a new private ECC key.
     *
     * @param randomSeed optional string used as the base to generate the new key
     * @return Generated ECC key in WIF format
     */
    public native String generatePrivateKey(String randomSeed) throws PrivmxException, NativeException;

    /**
     * Generates a new private ECC key from a password using pbkdf2.
     *
     * @param password the password used to generate the new key
     * @param salt     random string (additional input for the hashing function)
     * @return Generated ECC key in WIF format
     * @deprecated Use {@link CryptoApi#derivePrivateKey2(String, String)} instead.
     */
    @Deprecated
    public native String derivePrivateKey(String password, String salt) throws PrivmxException, NativeException;

    /**
     * Generates a new private ECC key from a password using pbkdf2.
     * This version of the derive function has a rounds count increased to 200k.
     * This makes using this function a safer choice, but it makes the received key
     * different than in the original version.
     *
     * @param password the password used to generate the new key
     * @param salt     random string (additional input for the hashing function)
     * @return generated ECC key in WIF format
     */
    public native String derivePrivateKey2(String password, String salt) throws PrivmxException, NativeException;

    /**
     * Generates a new public ECC key as a pair for an existing private key.
     *
     * @param privateKey private ECC key in WIF format
     * @return Generated ECC key in BASE58DER format
     */
    public native String derivePublicKey(String privateKey) throws PrivmxException, NativeException;

    /**
     * Encrypts buffer with a given key using AES.
     *
     * @param data         buffer to encrypt
     * @param symmetricKey key used to encrypt data
     * @return Encrypted data buffer
     */
    public native byte[] encryptDataSymmetric(byte[] data, byte[] symmetricKey) throws PrivmxException, NativeException;

    /**
     * Decrypts buffer with a given key using AES.
     *
     * @param data         buffer to decrypt
     * @param symmetricKey key used to decrypt data
     * @return Plain (decrypted) data buffer
     */
    public native byte[] decryptDataSymmetric(byte[] data, byte[] symmetricKey) throws PrivmxException, NativeException;

    /**
     * Creates a signature of data using given key.
     *
     * @param data       data the buffer to sign
     * @param privateKey the key used to sign data
     * @return Signature of data
     */
    public native byte[] signData(byte[] data, String privateKey) throws PrivmxException, NativeException;

    /**
     * Validate a signature of data using given key.
     *
     * @param data      buffer
     * @param signature of data
     * @param publicKey public ECC key in BASE58DER format used to validate data
     * @return data validation result
     */
    public native boolean verifySignature(byte[] data, byte[] signature, String publicKey) throws PrivmxException, NativeException;

    /**
     * Converts given private key in PEM format to its WIF format.
     *
     * @param pemKey private key to convert
     * @return Private key in WIF format
     */
    public native String convertPEMKeyToWIFKey(String pemKey) throws PrivmxException, NativeException;

    /**
     * Converts given public key in PGP format to its base58DER format.
     * <p>
     * //     * @param pemKey public key to convert
     *
     * @return private key in base58DER format
     */
    public native String convertPGPAsn1KeyToBase58DERKey(String pgpKey) throws PrivmxException, NativeException;

    /**
     * Generates ECC key and BIP-39 mnemonic from a password using BIP-39.
     *
     * @param strength size of BIP-39 entropy, must be a multiple of 32
     * @param password the password used to generate the Key
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public native BIP39_t generateBip39(long strength, String password) throws PrivmxException, NativeException;

    /**
     * Generates ECC key and BIP-39 mnemonic from a password using BIP-39.
     *
     * @param strength size of BIP-39 entropy, must be a multiple of 32
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public BIP39_t generateBip39(long strength) throws PrivmxException, NativeException {
        return generateBip39(strength, null);
    }

    /**
     * Generates ECC key using BIP-39 mnemonic.
     *
     * @param mnemonic the BIP-39 entropy used to generate the Key
     * @param password the password used to generate the Key
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public native BIP39_t fromMnemonic(String mnemonic, String password) throws PrivmxException, NativeException;

    /**
     * Generates ECC key using BIP-39 mnemonic.
     *
     * @param mnemonic the BIP-39 entropy used to generate the Key
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public BIP39_t fromMnemonic(String mnemonic) throws PrivmxException, NativeException {
        return fromMnemonic(mnemonic, null);
    }

    /**
     * Generates ECC key using BIP-39 entropy.
     *
     * @param entropy  the BIP-39 entropy used to generate the Key
     * @param password the password used to generate the Key
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public native BIP39_t fromEntropy(byte[] entropy, String password) throws PrivmxException, NativeException;

    /**
     * Generates ECC key using BIP-39 entropy.
     *
     * @param entropy the BIP-39 entropy used to generate the Key
     * @return BIP39_t object containing ECC Key and associated with it BIP-39 mnemonic and entropy
     */
    public BIP39_t fromEntropy(byte[] entropy) throws PrivmxException, NativeException {
        return fromEntropy(entropy, null);
    }

    /**
     * Converts BIP-39 entropy to mnemonic.
     *
     * @param entropy BIP-39 entropy
     * @return BIP-39 mnemonic
     */
    public native String entropyToMnemonic(byte[] entropy) throws PrivmxException, NativeException;

    /**
     * Converts BIP-39 mnemonic to entropy.
     *
     * @param mnemonic BIP-39 mnemonic
     * @return BIP-39 entropy
     */
    public native byte[] mnemonicToEntropy(String mnemonic) throws PrivmxException, NativeException;

    /**
     * Generates a seed used to generate a key using BIP-39 mnemonic with PBKDF2.
     *
     * @param mnemonic BIP-39 mnemonic
     * @param password the password used to generate the seed
     * @return generated seed
     */
    public native byte[] mnemonicToSeed(String mnemonic, String password) throws PrivmxException, NativeException;

    /**
     * Generates a seed used to generate a key using BIP-39 mnemonic with PBKDF2.
     *
     * @param mnemonic BIP-39 mnemonic
     * @return generated seed
     */
    public byte[] mnemonicToSeed(String mnemonic) throws PrivmxException, NativeException {
        return mnemonicToSeed(mnemonic, null);
    }

    /**
     * Generates a new symmetric key.
     *
     * @return Generated key
     */
    public native byte[] generateKeySymmetric();

    @Override
    public void close() throws Exception {
        deinit();
    }
}
