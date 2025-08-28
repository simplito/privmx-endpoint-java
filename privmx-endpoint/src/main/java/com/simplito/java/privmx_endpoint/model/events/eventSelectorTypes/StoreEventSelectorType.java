package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.StoreEventType;

/**
 * The scope or area of the event you want to listen (subscribe) for.
 * Defines where the event specified by {@link StoreEventType} happened.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum StoreEventSelectorType implements EventSelectorType {
    CONTEXT_ID,
    STORE_ID,
    FILE_ID
}
