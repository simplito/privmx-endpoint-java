package com.simplito.java.privmx_endpoint.model.events.eventTypes;

/**
 * The type of event you want to listen (subscribe) for.
 * Determines kind of action performed on a Connection.
 *
 * @category core
 * @group EventTypes
 */
public enum ConnectionEventType implements EventType{
    USER_ADD,
    USER_REMOVE,
    USER_STATUS
}