package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSONArray;
import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.request.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-22 11:02 上午
 * @description
 */
public class EasyDelete {

    /**
     * 根据metric名和时间区间，删除数据
     */
    public static JSONArray delete(
            String tsdAddress,
            String metric,
            String Aggregator,
            long startTime,
            long endTime,
            ExpectResponse responseType
    ) {
        List<Filter> filters = new ArrayList<>();
        return EasyQuery.delete(tsdAddress, metric, filters, Aggregator, startTime, endTime, responseType);
    }

    /**
     * 根据过滤结果，删除数据
     */
    public static JSONArray delete(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            ExpectResponse responseType
    ) {
        return EasyQuery.delete(tsdAddress, metric, filters, Aggregator, startTime, endTime, responseType);
    }
}
