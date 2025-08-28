package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

import com.simplito.java.privmx_endpoint.model.events.eventTypes.InboxEventType;

/**
 * The scope or area of the event you want to listen (subscribe) for.
 * Defines where the event specified by {@link InboxEventType} happened.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum InboxEventSelectorType implements EventSelectorType {
    CONTEXT_ID,
    INBOX_ID,
    ENTRY_ID
}