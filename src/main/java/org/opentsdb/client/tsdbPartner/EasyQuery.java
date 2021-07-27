package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.request.Filter;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.request.SubQueries;
import org.opentsdb.client.response.SimpleHttpResponse;
import org.opentsdb.client.util.TimeUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 4:41 下午
 * @description
 */
public class EasyQuery {

    /**
     * metric ✅
     * aggregator ✅
     * filters ❌
     * downSample ❌
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            String Aggregator,
            long startTime,
            long endTime,
            ExpectResponse responseType
    ) {
        List<Filter> filters = new ArrayList<>();
        return EasyBaseQuery.query(tsdAddress, metric, filters, Aggregator, startTime, endTime, false, responseType);
    }

    /**
     * metric ✅
     * aggregator ✅
     * filters ✅
     * downSample ❌
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            ExpectResponse responseType
    ) {
        return EasyBaseQuery.query(tsdAddress, metric, filters, Aggregator, startTime, endTime, false, responseType);
    }

    /**
     * metric ✅
     * aggregator ✅
     * filters ❌
     * downSample ✅
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            String Aggregator,
            long startTime,
            long endTime,
            String downSample,
            ExpectResponse responseType
    ) {
        List<Filter> filters = new ArrayList<>();
        return EasyBaseQuery.query(tsdAddress, metric, filters, Aggregator, startTime, endTime, false, downSample, responseType);
    }

    /**
     * metric ✅
     * aggregator ✅
     * filters ✅
     * downSample ✅
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            String downSample,
            ExpectResponse responseType
    ) {
        return EasyBaseQuery.query(tsdAddress, metric, filters, Aggregator, startTime, endTime, false, downSample, responseType);
    }

    /**
     * 查询后删除
     */
    public static JSONArray delete(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            ExpectResponse responseType
    ){
        return EasyBaseQuery.query(tsdAddress, metric, filters, Aggregator, startTime, endTime, true, responseType);
    }


    /**
     * 格式化打印查询结果
     * @param jsonArray 查询结果
     */
    public static void printDps(JSONArray jsonArray){
        for (Object object : jsonArray) {
            JSONObject json = (JSONObject) JSON.toJSON(object);
            System.out.println("Metric: " + json.getString("metric"));
            System.out.println("Tags: " + json.getString("tags"));
            System.out.println("AggregateTags: " + json.getString("aggregateTags"));
            String dps = json.getString("dps");
            Map<String, String> map = JSON.parseObject(dps, Map.class);
            List<Long> sortKeys = map
                    .keySet()
                    .stream()
                    .map(Long::parseLong)
                    .sorted()
                    .collect(Collectors.toList());
            for (Long timeStamp : sortKeys) {
                String time = TimeUtils.stampToDate(timeStamp.toString() + "000");
                String value = String.valueOf(map.get(timeStamp.toString()));
                System.out.println("Time:" + time + ", Value:" + value);
            }
            System.out.println("---------------------------------------------------\n");
        }
    }

}
