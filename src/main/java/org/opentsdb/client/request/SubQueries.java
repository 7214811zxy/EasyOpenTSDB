package org.opentsdb.client.request;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
@Data
public class SubQueries {
    private String aggregator;
    private String metric;
    private Boolean rate = false;
    private Map<String, String> rateOptions;
    private String downsample;
    private List<Filter> filters = new ArrayList<>();

    public SubQueries addAggregator(String aggregator) {
        this.aggregator = aggregator;
        return this;
    }

    public SubQueries addMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public SubQueries addDownsample(String downsample) {
        this.downsample = downsample;
        return this;
    }

    public SubQueries addFilter(Filter filter){
        this.filters.add(filter);
        return this;
    }
}
