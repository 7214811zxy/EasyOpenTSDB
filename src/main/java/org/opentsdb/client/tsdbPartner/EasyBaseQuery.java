package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.request.Filter;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.request.SubQueries;
import org.opentsdb.client.response.SimpleHttpResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-23 3:28 下午
 * @description
 */
public class EasyBaseQuery {

    /**
     * 查询接口
     * @param tsdAddress TSD server地址
     * @param metric metric
     * @param Aggregator 聚合方式(从enum Aggregator中获取）
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param delete 是否删除匹配到的datapoint
     * @return 查询成功则返回查询结果，查询失败返回null
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            boolean delete,
            String downSample,
            ExpectResponse responseType
    ) {
        HttpClientImpl client = new HttpClientImpl(tsdAddress);

        QueryBuilder builder = QueryBuilder.getInstance();
        builder.getQuery().setDelete(delete);

        SubQueries subQueries = new SubQueries();

        subQueries
                .addMetric(metric)
                .addAggregator(Aggregator);

        if(downSample != null){
            subQueries.addDownsample(downSample);
        }

        if(filters.size() > 0){
            subQueries.setFilters(filters);
        }

        builder.getQuery().addStart(startTime).addEnd(endTime).addSubQuery(subQueries);

        try {
            SimpleHttpResponse response = client.pushQueries(builder, responseType);
            String content = response.getContent();
            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                return JSON.parseArray(content);
            }else {
                System.out.println(content);
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 查询接口(不需指定降采样)
     * @param tsdAddress TSD server地址
     * @param metric metric
     * @param Aggregator 聚合方式(从enum Aggregator中获取）
     * @param startTime 起始时间
     * @param endTime 结束时间
     * @param delete 是否删除匹配到的datapoint
     * @return 查询成功则返回查询结果，查询失败返回null
     */
    public static JSONArray query(
            String tsdAddress,
            String metric,
            List<Filter> filters,
            String Aggregator,
            long startTime,
            long endTime,
            boolean delete,
            ExpectResponse responseType
    ) {
        return query(tsdAddress, metric, filters, Aggregator, startTime, endTime, delete, null, responseType);
    }
}
