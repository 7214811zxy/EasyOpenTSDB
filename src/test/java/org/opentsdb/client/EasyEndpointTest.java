package org.opentsdb.client;

import org.junit.Test;
import org.opentsdb.client.tsdbPartner.EasyEndpoint;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 11:02 上午
 */
public class EasyEndpointTest {

    /**
     * 清除TSD的缓存
     */
    @Test
    public void dropCachesTest(){
        Integer resCode = EasyEndpoint.dropCaches("http://flink01:4242");
        System.out.println(resCode);
    }
}
