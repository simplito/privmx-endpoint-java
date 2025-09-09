package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.File;
import com.simplito.java.privmx_endpoint.model.FileChange;

import java.util.List;

/**
 * Holds information about file updates.
 *
 * @category core
 * @group Events
 */
public class StoreFileUpdatedEventData {
    /**
     * File meta
     */
    public final File file;

    /**
     * List of file changes
     */
    public final List<FileChange> changes;

    /**
     * Creates instance of {@code StoreFileUpdatedEventData}.
     *
     * @param file    File meta
     * @param changes List of file changes
     */
    public StoreFileUpdatedEventData(File file, List<FileChange> changes) {
        this.file = file;
        this.changes = changes;
    }
}
