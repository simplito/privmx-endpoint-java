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
 * Holds file's information created by server.
 *
 * @category store
 * @group Store
 */
@SuppressWarnings("CanBeFinal")
public class ServerFileInfo {

    /**
     * ID of the Store.
     */
    public String storeId;

    /**
     * ID of the file.
     */
    public String fileId;

    /**
     * File's creation timestamp.
     */
    public Long createDate;

    /**
     * ID of the user who created the file.
     */
    public String author;


    /**
     * Creates instance of {@code ServerFileInfo}.
     *
     * @param storeId    ID of the Store.
     * @param fileId     ID of the file.
     * @param createDate File's creation timestamp.
     * @param author     ID of the user who created the file.
     */
    public ServerFileInfo(
            String storeId,
            String fileId,
            Long createDate,
            String author
    ) {
        this.storeId = storeId;
        this.fileId = fileId;
        this.createDate = createDate;
        this.author = author;
    }
}