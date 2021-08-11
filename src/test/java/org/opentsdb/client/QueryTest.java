package org.opentsdb.client;

import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.opentsdb.client.request.Filter;
import org.opentsdb.client.tsdbPartner.EasyQuery;
import org.opentsdb.client.util.enumClass.AggregatorEnum;
import org.opentsdb.client.util.enumClass.FilterType;
import org.opentsdb.client.util.TimeUtils;

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
                AggregatorEnum.none.toString(),
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
                AggregatorEnum.sum.toString(),
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
                AggregatorEnum.sum.toString(),
                1627002000,
                1633046400,
                "30s-sum",
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
                AggregatorEnum.sum.toString(),
                1627002000,
                1633046400,
                "0all-sum",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 通用测试例
     */
    @Test
    public void queryTest05() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF"));
            add(new Filter(FilterType.wildcard.toString(), "plant", "*"));
            add(new Filter(FilterType.wildcard.toString(), "class", "*"));
            add(new Filter(FilterType.wildcard.toString(), "price", "*", true));
        }};
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test724315",
                filters,
                AggregatorEnum.sum.toString(),
                1627002000,
                1627005600,
                "1h-last",
                ExpectResponse.STATUS_CODE
        );
        EasyQuery.printDps(queryRes);
    }

    /**
     * 具有降采用的通用测试例
     */
    @Test
    public void queryTest07() {
        List<Filter> filters = new ArrayList<Filter>(){{
            add(new Filter(FilterType.literal_or.toString(), "group", "SHNF", true));
            add(new Filter(FilterType.wildcard.toString(), "plant", "HK1", true));
        }};
        JSONArray queryRes = EasyQuery.query(
                "http://flink01:4242",
                "test951753",
                filters,
                AggregatorEnum.sum.toString(),
                1627004548,
                1627004561,
                "10s-first",
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
