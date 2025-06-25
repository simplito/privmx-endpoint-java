package com.simplito.java.privmx_endpoint.model;

/**
 * Holds all available information about a Entry.
 *
 * @category kvdb
 */
public class KvdbEntry {
    /**
     * Entry information created by server
     */
    public ServerKvdbEntryInfo info;

    /**
     * Entry public metadata
     */
    public byte[] publicMeta;

    /**
     * Entry private metadata
     */
    public byte[] privateMeta;

    /**
     * Entry data
     */
    public byte[] data;

    /**
     * Public key of an author of the entry
     */
    public String authorPubKey;

    /**
     * Version number (changes on every on existing item)
     */
    public Long version;

    /**
     * Retrieval and decryption status code
     */
    public Long statusCode;

    /**
     * Version of the Entry data structure and how it is encoded/encrypted
     */
    public Long schemaVersion;

    /**
     * Creates instance of {@code KvdbEntry}.
     *
     * @param info          Entry information created by server
     * @param publicMeta    Entry public metadata
     * @param privateMeta   Entry private metadata
     * @param data          Entry data
     * @param authorPubKey  Public key of an author of the entry
     * @param version       Version number (changes on every on existing item)
     * @param statusCode    Retrieval and decryption status code
     * @param schemaVersion Version of the Entry data structure and how it is encoded/encrypted
     */
    public KvdbEntry(
            ServerKvdbEntryInfo info,
            byte[] publicMeta,
            byte[] privateMeta,
            byte[] data,
            String authorPubKey,
            Long version,
            Long statusCode,
            Long schemaVersion
    ) {
        this.info = info;
        this.publicMeta = publicMeta;
        this.privateMeta = privateMeta;
        this.data = data;
        this.authorPubKey = authorPubKey;
        this.version = version;
        this.statusCode = statusCode;
        this.schemaVersion = schemaVersion;
    }
}