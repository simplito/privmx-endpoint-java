package com.simplito.java.privmx_endpoint.model;

/**
 * Holds information about the file change.
 *
 * @category store
 * @group Store
 */
public class FileChange {
    /**
     * Position of the first changed chunk
     */
    public Long pos;
    /**
     * Length aligned to full chunks
     */
    public Long length;
    /**
     * Remove all data
     */
    public boolean truncate;

    /**
     * Creates instance of {@code FileChange}.
     *
     * @param pos      Position of the first changed chunk.
     * @param length   Length aligned to full chunks
     * @param truncate Remove all data.
     */
    public FileChange(Long pos, Long length, boolean truncate) {
        this.pos = pos;
        this.length = length;
        this.truncate = truncate;
    }
}
