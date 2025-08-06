package com.simplito.java.privmx_endpoint.model;

import com.simplito.java.privmx_endpoint.model.events.eventSelectorTypes.EventSelectorType;
import com.simplito.java.privmx_endpoint.model.events.eventTypes.EventType;

import java.util.Objects;

public class EventSelector {
    public EventType eventType;
    public EventSelectorType eventSelectorType;
    public String eventSelectorId;
    public String subscriptionId;

    public EventSelector(EventType eventType, EventSelectorType eventSelectorType, String eventSelectorId) {
        this.eventType = eventType;
        this.eventSelectorType = eventSelectorType;
        this.eventSelectorId = eventSelectorId;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == this.getClass()) {
            EventSelector eventSelector = (EventSelector) o;

            return eventSelector.eventType == this.eventType &&
                    eventSelector.eventSelectorType == this.eventSelectorType &&
                    Objects.equals(eventSelector.eventSelectorId, this.eventSelectorId);
        } else return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventType, eventSelectorType, eventSelectorId);
    }
}