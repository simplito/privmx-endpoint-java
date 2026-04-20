package com.simplito.java.privmx_endpoint.model.stream.events;

/**
 * Current state of a single publisher stream, received as part of {@code StreamsUpdatedData}.
 */
public class UpdatedStreamData {
    /**
     * Track type (e.g. "audio", "video", "data")
     */
    public String type;
    public Long mindex;
    public String mid;
    Boolean send;
    Boolean ready;

    /**
     * Codec used by the stream (e.g. "opus", "VP8")
     */
    public String codec = null;                // optional

    /**
     * Identifier of the publisher stream
     */
    public Long streamId = null;               // optional
    public String streamMid = null;            // optional
    public String streamDisplay = null;        // optional

    /**
     * Constructs a new {@link UpdatedStreamData} instance.
     *
     * @param type          Track type (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param send
     * @param ready
     * @param codec         Codec used by the stream (e.g. "opus", "VP8")
     * @param streamId      Identifier of the publisher stream
     * @param streamMid
     * @param streamDisplay
     */
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
}