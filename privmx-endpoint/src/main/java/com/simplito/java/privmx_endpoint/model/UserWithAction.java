package com.simplito.java.privmx_endpoint.model;

/**
 * Contains the user with their status change action.
 *
 * @category core
 * @group Core
 */
public class UserWithAction {
    /**
     * User.
     */
    public UserWithPubKey user;

    /**
     * User status change action, which can be "login" or "logout".
     */
    public String action;

    /**
     * Creates instance of {@code UserWithAction}.
     *
     * @param user   User
     * @param action User status change action
     */
    public UserWithAction(UserWithPubKey user, String action) {
        this.user = user;
        this.action = action;
    }
}