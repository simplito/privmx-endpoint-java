package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.KvdbEventType;

/**
 * The scope or area of the event you want to listen (subscribe) for.
 * Defines where the event specified by {@link KvdbEventType} happened.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum KvdbEventSelectorType implements EventSelectorType {
    CONTEXT_ID,
    KVDB_ID,
    ENTRY_ID
}
