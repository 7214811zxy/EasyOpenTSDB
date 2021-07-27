package org.opentsdb.client.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
@Data
public class Query {
    private long start;
    private long end;
    private List<SubQueries> queries = new ArrayList<>();
    private Boolean noAnnotations = false;
    private Boolean globalAnnotations = false;
    private Boolean msResolution = false;
    private Boolean showTSUIDs = false;
    private Boolean showSummary = false;
    private Boolean showQuery = false;
    private Boolean delete = false;

    public Query addSubQuery(SubQueries subQueries) {
        this.queries.add(subQueries);
        return this;
    }

    public Query addStart(long start) {
        this.start = start;
        return this;
    }

    public Query addEnd(long end) {
        this.end = end;
        return this;
    }
}
