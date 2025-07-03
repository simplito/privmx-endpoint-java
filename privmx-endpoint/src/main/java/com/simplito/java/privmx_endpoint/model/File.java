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

/**
 * Holds information about the file.
 *
 * @category store
 * @group Store
 */
@SuppressWarnings("CanBeFinal")
public class File {

    /**
     * File's information created by server.
     */
    public ServerFileInfo info;

    /**
     * File's public metadata.
     */
    public byte[] publicMeta;

    /**
     * File's private metadata.
     */
    public byte[] privateMeta;

    /**
     * File's size.
     */
    public Long size;

    /**
     * Public key of the author of the file.
     */
    public String authorPubKey;

    /**
     * Status code of retrieval and decryption of the file.
     */
    public Long statusCode;

    /**
     * Version of the file data structure and how it is encoded/encrypted.
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code File}.
     *
     * @param info          File's information created by server.
     * @param publicMeta    File's public metadata.
     * @param privateMeta   File's private metadata.
     * @param size          File's size.
     * @param authorPubKey  Public key of the author of the file.
     * @param statusCode    Status code of retrieval and decryption of the file.
     * @param schemaVersion Version of the file data structure and how it is encoded/encrypted.
     */
    public File(
            ServerFileInfo info,
            byte[] publicMeta,
            byte[] privateMeta,
            Long size,
            String authorPubKey,
            Long statusCode,
            Long schemaVersion
    ) {
        this.info = info;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.size = size;
        this.authorPubKey = authorPubKey;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }

    /**
     * Creates instance of {@code File}.
     *
     * @param info         File's information created by server.
     * @param publicMeta   File's public metadata.
     * @param privateMeta  File's private metadata.
     * @param size         File's size.
     * @param authorPubKey Public key of the author of the file.
     * @param statusCode   Status code of retrieval and decryption of the file.
     */
    @Deprecated
    public File(
            ServerFileInfo info,
            byte[] publicMeta,
            byte[] privateMeta,
            Long size,
            String authorPubKey,
            Long statusCode
    ) {
        this(info, publicMeta, privateMeta, size, authorPubKey, statusCode, null);
    }
}