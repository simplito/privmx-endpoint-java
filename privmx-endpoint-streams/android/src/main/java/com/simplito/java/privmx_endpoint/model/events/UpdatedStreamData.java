package com.simplito.java.privmx_endpoint.model.events;

public class UpdatedStreamData {
    public String type;
    public String codec = null;                // optional
    public Long streamId = null;               // optional
    public String streamMid = null;            // optional
    public String streamDisplay = null;        // optional
    public Long mindex;
    public String mid;
    Boolean send;
    Boolean ready;

    public UpdatedStreamData(String type, Long mindex, String mid, Boolean send, Boolean ready, String codec, Long streamId, String streamMid, String streamDisplay) {
        this.type = type;
        this.codec = codec;
        this.streamId = streamId;
        this.streamMid = streamMid;
        this.streamDisplay = streamDisplay;
        this.mindex = mindex;
        this.mid = mid;
        this.send = send;
        this.ready = ready;
    }

    public UpdatedStreamData(String type, Long mindex, String mid, Boolean send, Boolean ready, String codec, Long streamId, String streamMid) {
        this(type, mindex, mid, send, ready, codec, streamId, streamMid, null);
    }

    public UpdatedStreamData(String type, Long mindex, String mid, Boolean send, Boolean ready, String codec, Long streamId) {
        this(type, mindex, mid, send, ready, codec, streamId, null, null);
    }

    public UpdatedStreamData(String type, Long mindex, String mid, Boolean send, Boolean ready, String codec) {
        this(type, mindex, mid, send, ready, codec, null, null, null);
    }

    public UpdatedStreamData(String type, Long mindex, String mid, Boolean send, Boolean ready) {
        this(type, mindex, mid, send, ready, null, null, null, null);
    }
}