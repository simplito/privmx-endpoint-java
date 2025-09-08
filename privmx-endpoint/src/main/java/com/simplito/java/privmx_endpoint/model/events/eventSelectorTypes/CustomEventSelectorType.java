package com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes;

/**
 * Specifies the type of identifier used to select an custom event.
 * This enum defines the possible types of selectors for custom events.
 *
 * @category core
 * @group EventSelectorTypes
 */
public enum CustomEventSelectorType implements EventSelectorType {
    /**
     * Selects events based on the ID of the context.
     */
    CONTEXT_ID
}