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
     *
     * @param user     User publicKey and userId
     * @param isActive is user connected to bridge
     */
    public UserInfo(
            UserWithPubKey user,
            boolean isActive
    ) {
        this.user = user;
        this.isActive = isActive;
    }
}