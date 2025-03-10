package com.simplito.java.privmx_endpoint.model;

public class UserInfo {
    /**
     * User publicKey and userId
     */
    public UserWithPubKey user;

    /**
     * is user connected to bridge
     */
    public boolean isActive;

    /**
     * Creates instance of {@code UserInfo}
     */
    public UserInfo(
            UserWithPubKey user,
            boolean isActive
    ) {
        this.user = user;
        this.isActive = isActive;
    }
}
