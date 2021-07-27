package org.opentsdb.client;

import org.junit.Test;
import org.opentsdb.client.request.DataPoint;
import org.opentsdb.client.tsdbPartner.EasyInsert;
import org.opentsdb.client.util.DataPointMaker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试用例中使用的时间戳映射关系
 * 1625097600 = 2021-07-01 08:00:00
 * 946684800 = 2000-01-01 08:00:00
 * 1633046400 = 2021-10-01 08:00:00
 */
public class InsertDataTest {

    /**
     * 插入一个数据点
     */
    @Test
    public void insertOneDataTest(){
        Map<String, String> tags = new HashMap<String, String>(){{
            put("group", "SHNF");
            put("plant", "HK1");
            put("class", "Jia");
            put("price", "peak");
        }};
        List<DataPoint> dataPoints = DataPointMaker.makeOne("test724315", 30.0, tags);
        Integer resCode = EasyInsert.insert("http://flink01:4242", dataPoints);
        System.out.println(resCode);
    }

    /**
     * 插入固定数值的批量数据
     */
    @Test
    public void insertBatchDataTest(){
        Map<String, String> tags = new HashMap<String, String>(){{
            put("group", "SHNF");
            put("plant", "HK1");
            put("class", "Yi");
            put("price", "peak");
        }};

        List<DataPoint> dataPoints = DataPointMaker.makeBatch("test724315", 20.0, tags, 1625097600L,10, 10);
        Integer resCode = EasyInsert.insert("http://flink01:4242", dataPoints);
        System.out.println(resCode);
    }

    /**
     * 插入批量随机数据
     */
    @Test
    public void insertBatchRandomDataTest(){
        Map<String, String> tags = new HashMap<String, String>(){{
            put("group", "SHNF");
            put("plant", "HK1");
            put("class", "Jia");
            put("price", "normal");
        }};
        List<DataPoint> dataPoints = DataPointMaker.makeBatchRandomValue("test724315", tags, 10, 100);
        Integer resCode = EasyInsert.insert("http://flink01:4242", dataPoints);
        System.out.println(resCode);
    }

    /**
     * 批量插入递增数据
     * 1627004514 = 2021-07-23 09:41:54
     */
    @Test
    public void insertBatchStepDataTest(){
        Map<String, String> tags = new HashMap<String, String>(){{
//            put("price", "peak");
            put("group", "SHNF");
//            put("class", "Yi");
            put("plant", "HK2");
        }};
        List<DataPoint> dataPoints = DataPointMaker.makeBatchStepValue(
                "test951753",
                tags,
                1627004547L,
                2,
                32.0,
                1.0,
                7
        );
        Integer resCode = EasyInsert.insert("http://flink01:4242", dataPoints);
        System.out.println(resCode);
    }
}
