package org.opentsdb.client.builder;

import org.junit.Test;
import org.opentsdb.client.tsdbPartner.EasyUid;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 2:35 下午
 * @description
 */
public class UidTest {

    /**
     * 删除Uid(一般别删！！)
     */
    @Test
    public void deleteUid(){
        Map<String, String> params = new HashMap<>();
        params.put("uid","000004");
        params.put("type", "metric");
        Integer resCode = EasyUid.deleteUID("http://flink01:4242", params);
        System.out.println(resCode);
    }

}
