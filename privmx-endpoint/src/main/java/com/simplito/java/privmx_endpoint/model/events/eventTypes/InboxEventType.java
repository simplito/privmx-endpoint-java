package com.simplito.java.privmx_endpoint.model.events.eventTypes;

/**
 * The type of event you want to listen (subscribe) for.
 * Determines kind of action performed on an Inbox or on an InboxEntry.
 *
 * @category core
 * @group EventTypes
 */
public enum InboxEventType implements EventType {
    INBOX_CREATE,
    INBOX_UPDATE,
    INBOX_DELETE,
    ENTRY_CREATE,
    ENTRY_DELETE
}