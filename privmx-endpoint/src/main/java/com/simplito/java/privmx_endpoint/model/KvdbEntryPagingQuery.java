package com.simplito.java.privmx_endpoint.model;

public class KvdbEntryPagingQuery {
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
     * Extra query parameters in serialized JSON
     */
    String queryAsJson;

    public KvdbEntryPagingQuery(long skip, long limit, String sortOrder) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
    }

    public KvdbEntryPagingQuery(long skip, long limit, String sortOrder, String sortBy, String lastKey, String prefix, String queryAsJson) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
        this.lastKey = lastKey;
        this.prefix = prefix;
        this.queryAsJson = queryAsJson;
    }
}