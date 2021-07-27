package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSON;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.response.SimpleHttpResponse;

import java.io.IOException;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 10:51 上午
 * @description some easy endpoint;
 * 这个类里封装了一些简单的API，这些API一般只需要使用简单的Get请求就能完成工作
 */
public class EasyEndpoint {

    /**
     * /api/dropcaches
     * This endpoint purges the in-memory data cached in OpenTSDB. This includes all UID to name and name to UID maps for metrics, tag names and tag values.
     * 清除TSD中metrics, tagName, tagValue, UID-Name的双向映射缓存
     * @param tsdAddress tsd server address
     * @return 200 = successful; 400 = failed;
     */
    public static Integer dropCaches(String tsdAddress){
        HttpClientImpl client = new HttpClientImpl(tsdAddress);

        try {
            SimpleHttpResponse response = client.pushDropCaches();
            return response.getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 400;
        }
    }

}
