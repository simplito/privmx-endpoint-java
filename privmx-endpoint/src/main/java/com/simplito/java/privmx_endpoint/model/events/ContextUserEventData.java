package com.simplito.java.privmx_endpoint.model.events;

import com.simplito.java.privmx_endpoint.model.UserWithPubKey;

public class ContextUserEventData {
    /**
     * ID of the Context
     */
    public String contextId;

    /**
     * User
     */
    public UserWithPubKey user;
}