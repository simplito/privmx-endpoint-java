package com.simplito.java.privmx_endpoint.model.streams;

import com.simplito.java.privmx_endpoint.model.ContainerPolicy;

import java.util.List;

public class StreamRoom {
    public String contextId;
    public String streamRoomId;
    public Long createDate; 
    public String creator;
    public Long lastModificationDate; 
    public String lastModifier;
    public List<String> users;
    public List<String> managers;
    public Long version;
    public byte[] publicMeta;
    public byte[] privateMeta;
    public ContainerPolicy policy;
    public Long statusCode;
    public Long schemaVersion;

    public StreamRoom(
            String contextId,
            String streamRoomId,
            Long createDate,
            String creator,
            Long lastModificationDate,
            String lastModifier,
            List<String> users,
            List<String> managers,
            Long version,
            byte[] publicMeta,
            byte[] privateMeta,
            ContainerPolicy policy,
            Long statusCode,
            Long schemaVersion
    ) {
        this.contextId = contextId;
        this.streamRoomId = streamRoomId;
        this.createDate = createDate;
        this.creator = creator;
        this.lastModificationDate = lastModificationDate;
        this.lastModifier = lastModifier;
        this.users = users;
        this.managers = managers;
        this.version = version;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.policy = policy;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }
}