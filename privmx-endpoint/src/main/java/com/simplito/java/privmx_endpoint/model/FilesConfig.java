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
 * Holds Inbox files configuration.
 * @category inbox
 * @group Inbox
 */
public class FilesConfig {

    /**
     * Minimum number of files required when sending Inbox entry.
     */
    public Long minCount;

    /**
     * Maximum number of files allowed when sending Inbox entry.
     */
    public Long maxCount;

    /**
     * Maximum file size allowed when sending Inbox entry.
     */
    public Long maxFileSize;

    /**
     * Maximum size of all files in total allowed when sending Inbox entry.
     */
    public Long maxWholeUploadSize;

    /**
     * Creates instance of {@code FilesConfig}.
     * @param minCount Minimum number of files required when sending Inbox entry.
     * @param maxCount Maximum number of files allowed when sending Inbox entry.
     * @param maxFileSize Maximum file size allowed when sending Inbox entry.
     * @param maxWholeUploadSize Maximum size of all files in total allowed when sending Inbox entry.
     */
    public FilesConfig(Long minCount, Long maxCount, Long maxFileSize, Long maxWholeUploadSize) {
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.maxFileSize = maxFileSize;
        this.maxWholeUploadSize = maxWholeUploadSize;
    }
}
