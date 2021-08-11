package org.opentsdb.client.request;

import lombok.Data;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
@Data
public class Filter {
    private String type;
    private String tagk;
    private String filter;
    private Boolean groupBy = false;

    public Filter() {}

    public Filter(String type, String tagk, String filter, Boolean groupBy) {
        this.type = type;
        this.tagk = tagk;
        this.filter = filter;
        this.groupBy = groupBy;
    }

    public Filter(String type, String tagk, String filter) {
        this.type = type;
        this.tagk = tagk;
        this.filter = filter;
    }
}
