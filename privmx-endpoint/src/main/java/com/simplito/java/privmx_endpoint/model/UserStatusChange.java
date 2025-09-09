package com.simplito.java.privmx_endpoint.model;

/**
 * Contains information about the change of user status.
 */
public class UserStatusChange {
    /**
     * User status change action, which can be "login" or "logout".
     */
    public String action;

    /**
     * Timestamp of the change.
     */
    public Long timestamp;

    /**
     * Creates instance of {@code UserStatusChange}
     *
     * @param timestamp User status change action, which can be "login" or "logout"
     * @param action    Timestamp of the change
     */
    public UserStatusChange(Long timestamp, String action) {
        this.timestamp = timestamp;
        this.action = action;
    }
}