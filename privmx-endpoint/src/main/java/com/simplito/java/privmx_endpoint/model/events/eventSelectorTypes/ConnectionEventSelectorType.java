package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.ConnectionEventType;

/**
 * The scope or area of the event you want to listen (subscribe) for.
 * Defines where the event specified by {@link ConnectionEventType} happened.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum ConnectionEventSelectorType implements EventSelectorType {
    CONTEXT_ID
}