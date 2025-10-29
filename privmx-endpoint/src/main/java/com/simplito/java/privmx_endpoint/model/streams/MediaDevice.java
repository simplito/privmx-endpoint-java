package com.simplito.java.privmx_endpoint.model.streams;

public class MediaDevice {
    public String name;
    public String id;
    public DeviceType type;

    public MediaDevice(String name, String id, DeviceType type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }
}