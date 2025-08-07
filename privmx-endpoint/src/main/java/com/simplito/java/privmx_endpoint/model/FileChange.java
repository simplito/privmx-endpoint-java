package com.simplito.java.privmx_endpoint.model;

/**
 * Holds information about the file change.
 *
 * @category store
 * @group Store
 */
public class FileChange {
    /**
     * Position of file change
     */
    public Long pos;
    /**
     * Length of file change
     */
    public Long length;
    /**
     * Remove all data
     */
    public boolean truncate;

    /**
     * Creates instance of {@code FileChange}.
     *
     * @param pos      Position of file change.
     * @param length   Length of file change.
     * @param truncate Remove all data.
     */
    public FileChange(Long pos, Long length, boolean truncate) {
        this.pos = pos;
        this.length = length;
        this.truncate = truncate;
    }
}
