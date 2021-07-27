package org.opentsdb.client.request;

import lombok.Data;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-20 11:23 上午
 * @description
 */
@Data
public class Suggest {
    private String type;
    private String query;
    private Integer max;
}
