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
    public native boolean verifySignature(byte[] data, byte[] signature, String publicKey);

    /**
     * Converts given private key in PEM format to its WIF format.
     *
     * @param pemKey private key to convert
     * @return Private key in WIF format
     */
    public native String convertPEMKeyToWIFKey(String pemKey) throws PrivmxException, NativeException;

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
