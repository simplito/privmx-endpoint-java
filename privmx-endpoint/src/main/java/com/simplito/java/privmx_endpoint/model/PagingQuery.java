package com.simplito.java.privmx_endpoint.model;

public class PagingQuery {
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
     * ID of the element from which query results should start
     */
    String lastId;

    /**
     * extra query parameters in serialized JSON
     */
    String queryAsJson;

    public PagingQuery(long skip, long limit, String sortOrder) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
    }

    public PagingQuery(long skip, long limit, String sortOrder, String lastId, String queryAsJson) {
        this.skip = skip;
        this.limit = limit;
        this.sortOrder = sortOrder;
        this.lastId = lastId;
        this.queryAsJson = queryAsJson;
    }
};