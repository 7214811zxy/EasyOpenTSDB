package org.opentsdb.client;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.opentsdb.client.request.Filter;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.request.SubQueries;
import org.opentsdb.client.response.SimpleHttpResponse;
import org.opentsdb.client.tsdbPartner.EasyBaseQuery;
import org.opentsdb.client.tsdbPartner.EasyQuery;
import org.opentsdb.client.util.Aggregator;
import org.opentsdb.client.util.FilterType;
import org.opentsdb.client.util.TimeUtils;

import java.io.IOException;
import java.util.*;

/**
 *
 * 设存在如下标签：
 *
 *  假设"test724315"表示从dcs获取到的"窑头煤称"的增量数据
 *  假设"test951753"表示从dcs获取到的"电表读数"的增量数据
 *
 *  数据集的详细内容见 👉 "https://kdocs.cn/l/ccQRimYq2LbW[金山文档] openTSDB测试方案.xlsx"
 *  关于openTSDB的查询逻辑 👉  http://opentsdb.net/docs/build/html/user_guide/query/index.html
 *
 *  部分测试用例中使用的时间戳映射关系
 *      946684800 = 2000-01-01 08:00:00
 *      1625097600 = 2021-07-01 08:00:00
 *      1627002000 = 2021-07-23 09:00:00
 *      1627005600 = 2021-07-23 10:00:00
 *      1633046400 = 2021-10-01 08:00:00
 *
 */
public class QueryTest {

    /**
     *
     * 获取"窑头煤称"从 "2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的所有数据
     *
     * 注：类似于"模糊查询"，只不过会自动分组
     */
    @Test
    public void queryTest01() {
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test724315",
                Aggregator.none.toString(),
                1627002000,
                1627005600,
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     *
     * 计算"上海南方，HK1线，甲班，峰电"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的总煤耗
     * res = 75
     *
     * 注：类似于"条件查询"
     */
    @Test
    public void queryTest02() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.literal_or.toString(), "price", "peak", false));
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF", false));
            add(new Filter(FilterType.literal_or.toString(), "class", "Jia", false));
            add(new Filter(FilterType.literal_or.toString(), "plant", "HK1", false));
        }};

        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test724315",
                filters,
                Aggregator.sum.toString(),
                1627002000,
                1633046400,
                "0all-sum",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 计算"上海南方，HK1线"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的总煤耗
     * res = 21 + 75 + 147 = 243
     *
     * 关于聚合 👉 http://opentsdb.net/docs/build/html/user_guide/query/aggregators.html
     */
    @Test
    public void queryTest03() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.wildcard.toString(), "price", "*", false));
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF", false));
            add(new Filter(FilterType.wildcard.toString(), "class", "*", false));
            add(new Filter(FilterType.literal_or.toString(), "plant", "HK1", false));
        }};

        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test724315",
                filters,
                Aggregator.sum.toString(),
                1627002000,
                1633046400,
                "0all-sum",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 计算"上海南方, HK1线"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的总煤耗，按照班次分组返回
     * res = 21 + 75 = 96(甲班)
     * res = 147(乙班)
     *
     * 关于聚合 👉 http://opentsdb.net/docs/build/html/user_guide/query/aggregators.html
     */
    @Test
    public void queryTest04() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF", false));
            add(new Filter(FilterType.wildcard.toString(), "class", "*", true));
            add(new Filter(FilterType.literal_or.toString(), "plant", "HK1", false));
        }};

        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test724315",
                filters,
                Aggregator.sum.toString(),
                1627002000,
                1633046400,
                "0all-sum",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 插值获取"上海南方"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的电价变化数据
     * res 👉 "https://kdocs.cn/l/ccQRimYq2LbW[金山文档] openTSDB测试方案.xlsx"中的sheet2
     *
     * 关于插值 👉 http://opentsdb.net/docs/build/html/user_guide/query/aggregators.html
     */
    @Test
    public void queryTest05() {
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test951753",
                Aggregator.sum.toString(),
                1627004548,
                1627004561,
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 降采样获取"上海南方"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的电价变化数据
     * res 👉 "https://kdocs.cn/l/ccQRimYq2LbW[金山文档] openTSDB测试方案.xlsx"中的"降采样查询测试1"
     *
     * 关于降采样 👉http://opentsdb.net/docs/build/html/user_guide/query/downsampling.html
     */
    @Test
    public void queryTest06() {
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test951753",
                Aggregator.sum.toString(),
                1627004548,
                1627004561,
                "3s-avg-zero",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 降采样获取"上海南方"，从"2021-07-23 09:00:00" 到 "2021-07-23 10:00:00"的电价变化数据
     * res 👉 "https://kdocs.cn/l/ccQRimYq2LbW[金山文档] openTSDB测试方案.xlsx"中的"降采样查询测试2"
     *
     * 关于降采样 👉http://opentsdb.net/docs/build/html/user_guide/query/downsampling.html
     */
    @Test
    public void queryTest07() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF", false));
            add(new Filter(FilterType.literal_or.toString(), "plant", "HK1", false));
        }};
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test951753",
                filters,
                Aggregator.sum.toString(),
                1627004548,
                1627004561,
                "10s-avg",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 计算降采样查询中的归一化时间
     */
    @Test
    public void downSampleTimeCal(){
        Long timestamp = 1627004548000L;
        Long interval_ms = 10000L;
        Long downSampleTimeStamp = timestamp - (timestamp % interval_ms);
        System.out.println("EventTime = " + TimeUtils.stampToDate(String.valueOf(timestamp)));
        System.out.println("Normalized Time = " + TimeUtils.stampToDate(String.valueOf(downSampleTimeStamp)));
    }

}
