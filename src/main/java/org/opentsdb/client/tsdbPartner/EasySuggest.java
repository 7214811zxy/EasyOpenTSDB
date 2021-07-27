package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSON;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.request.SuggestBuilder;
import org.opentsdb.client.response.SimpleHttpResponse;

import java.io.IOException;

import java.util.List;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-20 2:10 下午
 * @description
 */
public class EasySuggest {

    private static List<Object> getResponse(String tsdAddress, SuggestBuilder builder){
        HttpClientImpl client = new HttpClientImpl(tsdAddress);

        try {
            SimpleHttpResponse response = client.pushSuggest(builder);
            String content = response.getContent();
            int statusCode = response.getStatusCode();
            if (statusCode == 200) {
                return JSON.parseArray(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get all metrics list
     * @param tsdAddress tsd server address
     * @return metrics list
     */
    public static List<Object> getMetrics(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType("metrics");
        return getResponse(tsdAddress, builder);
    }

    /**
     * Get all tagNames list
     * @param tsdAddress tsd server address
     * @return tagNames list
     */
    public static List<Object> getTagNames(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType("tagk");
        return getResponse(tsdAddress, builder);
    }

    /**
     * Get all TagValues list
     * @param tsdAddress tsd server address
     * @return TagValues list
     */
    public static List<Object> getTagValues(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType("tagv");
        return getResponse(tsdAddress, builder);
    }

}
