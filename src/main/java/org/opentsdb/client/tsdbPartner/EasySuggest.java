package org.opentsdb.client.tsdbPartner;

import com.alibaba.fastjson.JSON;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.request.SuggestBuilder;
import org.opentsdb.client.response.SimpleHttpResponse;
import org.opentsdb.client.util.enumClass.TypeEnum;

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
     *
     * This endpoint provides a means of implementing an “auto-complete” call that can be accessed repeatedly as a user types a request in a GUI.
     *
     * @param tsdAddress tsd server address
     * @param type The type of data to auto complete on. Must be one of the following: metrics, tagk or tagv
     * @param queryString A string to match on for the given type
     * @param max The maximum number of suggested results to return. Must be greater than 0
     * @return String[]
     */
    public static List<Object> getAnything(String tsdAddress, TypeEnum type, String queryString, Integer max){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType(type.toString());
        builder.getSuggest().setMax(max);
        builder.getSuggest().setQuery(queryString);
        return getResponse(tsdAddress, builder);
    }

    /**
     * Get all metrics list
     * @param tsdAddress tsd server address
     * @return metrics list
     */
    public static List<Object> getMetrics(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType(TypeEnum.metrics.toString());
        return getResponse(tsdAddress, builder);
    }

    /**
     * Get all tagNames list
     * @param tsdAddress tsd server address
     * @return tagNames list
     */
    public static List<Object> getTagNames(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType(TypeEnum.tagk.toString());
        return getResponse(tsdAddress, builder);
    }

    /**
     * Get all TagValues list
     * @param tsdAddress tsd server address
     * @return TagValues list
     */
    public static List<Object> getTagValues(String tsdAddress){
        SuggestBuilder builder = SuggestBuilder.getInstance();
        builder.getSuggest().setType(TypeEnum.tagv.toString());
        return getResponse(tsdAddress, builder);
    }

}
