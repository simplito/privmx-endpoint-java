package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;


/**
 * Specifies the type of identifier used to select an KVDB event.
 * KVDB events can be targeted based on different levels of granularity within the KVDB structure.
 * This enum defines the possible types of selectors for these events.
 *
 * @category kvdb
 */
public enum KvdbEventSelectorType implements EventSelectorType {
    /**
     * Selects events based on the ID of the context.
     */
    CONTEXT_ID,
    /**
     * Selects events based on the ID of the KVDB.
     */
    KVDB_ID
}
