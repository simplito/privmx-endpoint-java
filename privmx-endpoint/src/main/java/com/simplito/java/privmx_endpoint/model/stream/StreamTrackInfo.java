package com.simplito.java.privmx_endpoint.model.stream;

/**
 * Represents metadata about a single media track within a stream.
 */
public class StreamTrackInfo {
    /**
     * Type of the track (e.g. "audio", "video", "data")
     */
    public String type;

    public Long mindex;

    public String mid;

    /**
     * Indicates if the track is disabled.
     * Tracks are never truly removed from the stream - they are marked as disabled instead.
     */
    public Boolean disabled;        // optional

    /**
     * Codec used by the track (e.g. "opus", "VP8")
     */
    public String codec;            // optional

    /**
     * Description of the track
     */
    public String description;      // optional

    public Boolean moderated;       // optional

    /**
     * Indicates if simulcast is enabled for the track
     */
    public Boolean simulcast;       // optional

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type   Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid
    ) {
        this(type, mindex, mid, null, null, null, null, null);
    }

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type     Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param disabled Indicates if the track is disabled
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled
    ) {
        this(type, mindex, mid, disabled, null, null, null, null);
    }

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type     Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param disabled Indicates if the track is disabled
     * @param codec    Codec used by the track (e.g. "opus", "VP8")
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec
    ) {
        this(type, mindex, mid, disabled, codec, null, null, null);
    }

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type        Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param disabled    Indicates if the track is disabled
     * @param codec       Codec used by the track (e.g. "opus", "VP8")
     * @param description Description of the track
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description
    ) {
        this(type, mindex, mid, disabled, codec, description, null, null);
    }

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type        Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param disabled    Indicates if the track is disabled
     * @param codec       Codec used by the track (e.g. "opus", "VP8")
     * @param description Description of the track
     * @param moderated
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description,
            Boolean moderated
    ) {
        this(type, mindex, mid, disabled, codec, description, moderated, null);
    }

    /**
     * Constructs a new {@link StreamTrackInfo} instance.
     *
     * @param type        Type of the track (e.g. "audio", "video", "data")
     * @param mindex
     * @param mid
     * @param disabled    Indicates if the track is disabled
     * @param codec       Codec used by the track (e.g. "opus", "VP8")
     * @param description Description of the track
     * @param moderated
     * @param simulcast   Indicates if simulcast is enabled for the track
     */
    public StreamTrackInfo(
            String type,
            Long mindex,
            String mid,
            Boolean disabled,
            String codec,
            String description,
            Boolean moderated,
            Boolean simulcast
    ) {
        this.type = type;
        this.mindex = mindex;
        this.mid = mid;
        this.disabled = disabled;
        this.codec = codec;
        this.description = description;
        this.moderated = moderated;
        this.simulcast = simulcast;
    }
}
