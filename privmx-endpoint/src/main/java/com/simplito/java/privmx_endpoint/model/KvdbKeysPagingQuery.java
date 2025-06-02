package com.simplito.java.privmx_endpoint.model;

public class KvdbKeysPagingQuery {
    /**
     * Number of elements to skip from result
     */
    long skip;

    /**
     * Limit of elements to return for query
     */
    long limit;

    /**
     * Order of elements in result ("asc" for ascending, "desc" for descending)
     */
    String sortOrder;

    /**
     * Order of elements are sorted in result ("createDate" for createDate, "itemKey" for itemKey, "lastModificationDate" for itemKey)
     */
    String sortBy;

    /**
     * Key of the element from which query results should start
     */
    String lastKey;

    /**
     * Prefix of the element from which query results should have
     */
    String prefix;

    /**
     * Creates instance of {@code KvdbKeysPagingQuery}.
     *
     * @param skip      Number of elements to skip from result
     * @param limit     Limit of elements to return for query
     * @param sortOrder Order of elements in result ("asc" for ascending, "desc" for descending)
     */
    public KvdbKeysPagingQuery(long skip, long limit, String sortOrder) {
        this.sortOrder = sortOrder;
        this.limit = limit;
        this.skip = skip;
    }

    /**
     * Creates instance of {@code KvdbKeysPagingQuery}.
     *
     * @param skip      Number of elements to skip from result
     * @param limit     Limit of elements to return for query
     * @param sortOrder Order of elements in result ("asc" for ascending, "desc" for descending)
     * @param sortBy    Order of elements are sorted in result ("createDate" for createDate, "itemKey" for itemKey, "lastModificationDate" for itemKey)
     * @param lastKey   Key of the element from which query results should start
     * @param prefix    Prefix of the element from which query results should have
     */
    public KvdbKeysPagingQuery(long skip, long limit, String sortOrder, String sortBy, String lastKey, String prefix) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
        this.lastKey = lastKey;
        this.prefix = prefix;
    }
}