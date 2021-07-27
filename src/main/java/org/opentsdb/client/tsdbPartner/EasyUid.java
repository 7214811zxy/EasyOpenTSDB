package org.opentsdb.client.tsdbPartner;

import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.response.SimpleHttpResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 1:49 下午
 * @description
 */
public class EasyUid {

    public static Integer deleteUID(String tsdAddress, Map<String, String> params){
        HttpClientImpl client = new HttpClientImpl(tsdAddress);
        try {
            SimpleHttpResponse response = client.pushDeleteUid(params);
            return response.getStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
            return 400;
        }
    }

}
