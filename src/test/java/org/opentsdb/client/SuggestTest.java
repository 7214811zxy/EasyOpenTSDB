package org.opentsdb.client;

import org.junit.Test;
import org.opentsdb.client.tsdbPartner.EasySuggest;
import org.opentsdb.client.util.enumClass.TypeEnum;

import java.util.List;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-20 11:21 上午
 * @description
 */
public class SuggestTest {

    /**
     * 获得openTSDB中所有的Metrics
     */
    @Test
    public void getMetricsTest() {
        List<Object> res = EasySuggest.getMetrics("http://flink01:4242");
        System.out.println(res);
    }

    /**
     * 获得openTSDB中所有的TagNames
     */
    @Test
    public void getTagNamesTest() {
        List<Object> res = EasySuggest.getTagNames("http://flink01:4242");
        System.out.println(res);
    }

    /**
     * 获得openTSDB中所有的TagValues
     */
    @Test
    public void getTagValuesTest() {
        List<Object> res = EasySuggest.getTagValues("http://flink01:4242");
        System.out.println(res);
    }

    /**
     * 自定义查询
     */
    @Test
    public void customSuggest1(){
        List<Object> res = EasySuggest.getAnything("http://flink01:4242", TypeEnum.metrics, "test", 10);
        System.out.println(res);
    }

    /**
     * 自定义查询
     */
    @Test
    public void customSuggest2(){
        List<Object> res = EasySuggest.getAnything("http://flink01:4242", TypeEnum.tagk, "plant", 10);
        System.out.println(res);
    }

    @Test
    public void getAllUIDName(){
        System.out.println("\n----------------Metric----------------");
        getMetricsTest();

        System.out.println("\n----------------TagName----------------");
        getTagNamesTest();

        System.out.println("\n----------------TagValue----------------");
        getTagValuesTest();
    }

}
