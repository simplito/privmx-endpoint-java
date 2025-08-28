package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.ThreadEventType;

/**
 * The scope or area of the event you want to listen (subscribe) for.
 * Defines where the event specified by {@link ThreadEventType} happened.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum ThreadEventSelectorType implements EventSelectorType {
    CONTEXT_ID,
    THREAD_ID,
    MESSAGE_ID
}
