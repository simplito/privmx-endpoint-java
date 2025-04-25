package com.simplito.java.privmx_endpoint.model;

public class KeysPagingQuery {
    /**
     * number of elements to skip from result
     */
    long skip;

    /**
     * limit of elements to return for query
     */
    long limit;

    /**
     * order of elements in result ("asc" for ascending, "desc" for descending)
     */
    String sortOrder;

    /**
     * order of elements are sorted in result ("createDate" for createDate, "itemKey" for itemKey, "lastModificationDate" for itemKey)
     */
    String sortBy;

    /**
     * Key of the element from which query results should start
     */
    String lastKey;

    /**
     * prefix of the element from which query results should have
     */
    String prefix;

    public KeysPagingQuery(String sortOrder, long limit, long skip) {
        this.sortOrder = sortOrder;
        this.limit = limit;
        this.skip = skip;
    }

    public KeysPagingQuery(long skip, long limit, String sortOrder, String sortBy, String lastKey, String prefix) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.sortBy = sortBy;
        this.lastKey = lastKey;
        this.prefix = prefix;
    }
};