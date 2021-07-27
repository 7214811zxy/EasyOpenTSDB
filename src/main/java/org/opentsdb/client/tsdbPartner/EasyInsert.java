package org.opentsdb.client.tsdbPartner;

import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.HttpClientImpl;
import org.opentsdb.client.builder.Metric;
import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.request.DataPoint;
import org.opentsdb.client.request.SuggestBuilder;
import org.opentsdb.client.response.Response;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-21 3:01 下午
 * @description
 */
public class EasyInsert {
    public static Integer insert(String tsdAddress, List<DataPoint> dataPoints){
        HttpClientImpl client = new HttpClientImpl(tsdAddress);

        MetricBuilder builder = MetricBuilder.getInstance();

        for(DataPoint dataPoint : dataPoints){
            builder
                .addMetric(dataPoint.getTagName())
                .setDataPoint(dataPoint.getTimeStamp(), dataPoint.getValue())
                .setTags(dataPoint.getTags());
        }

        try {
            Response response = client.pushMetrics(builder,
                    ExpectResponse.SUMMARY);
            return response.getStatusCode();
        } catch (IOException e) {
            return 400;
        }
    }
}
