package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.CollectionItemChange;

import java.util.List;

/**
 * Holds data of event that arrives when the collection is changed.
 *
 * @category core
 * @group Events
 */
public class CollectionChangedEventData {
    /**
     * Type of the module.
     */
    public String moduleType;

    /**
     * ID of the module.
     */
    public String moduleId;

    /**
     * Count of affected items.
     */
    public Long affectedItemsCount;

    /**
     * List of item changes.
     */
    public List<CollectionItemChange> items;

    /**
     * Creates instance of {@code CollectionChangedEventData}.
     *
     * @param moduleType         Type of the module
     * @param moduleId           ID of the module
     * @param affectedItemsCount Count of affected items
     * @param items              List of item changes
     */
    public CollectionChangedEventData(String moduleType, String moduleId, Long affectedItemsCount, List<CollectionItemChange> items) {
        this.moduleType = moduleType;
        this.moduleId = moduleId;
        this.affectedItemsCount = affectedItemsCount;
        this.items = items;
    }
}