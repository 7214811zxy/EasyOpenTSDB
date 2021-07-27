package org.opentsdb.client.request;

import lombok.Data;

import java.util.Map;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 3:04 下午
 * @description
 */
@Data
public class DataPoint {
    private String tagName;
    private Long timeStamp;
    private Double value;
    private Map<String, String> tags;

    public DataPoint() {}

    public DataPoint(String tagName, Long timeStamp, Double value, Map<String, String> tags) {
        this.tagName = tagName;
        this.timeStamp = timeStamp;
        this.value = value;
        this.tags = tags;
    }
}
