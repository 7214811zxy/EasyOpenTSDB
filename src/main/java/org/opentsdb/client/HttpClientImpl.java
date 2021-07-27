package org.opentsdb.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.request.SuggestBuilder;
import org.opentsdb.client.response.ErrorDetail;
import org.opentsdb.client.response.Response;
import org.opentsdb.client.response.SimpleHttpResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021/7/20 11:18 上午
 * @description OpenTSDB HttpApi toolBox
 *
 */
@Slf4j
public class HttpClientImpl implements HttpClient {

    private String serviceUrl;

    private Gson mapper;

    private PoolingHttpClient httpClient = new PoolingHttpClient();

    public HttpClientImpl(String serviceUrl) {
        this.serviceUrl = serviceUrl;

        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();
    }

    /**
     * /api/put
     * @param builder request body object
     * @return response result
     * @throws IOException IOException
     */
    @Override
    public Response pushMetrics(MetricBuilder builder) throws IOException {
        return pushMetrics(builder, ExpectResponse.STATUS_CODE);

    }

    /**
     * /api/put
     * @param builder request body object
     * @param expectResponse return information type (summary、detail)
     * @return response result
     * @throws IOException IOException
     */
    @Override
    public Response pushMetrics(MetricBuilder builder, ExpectResponse expectResponse) throws IOException {
        checkNotNull(builder);

        // TODO 错误处理，比如IOException或者failed>0，写到队列或者文件后续重试。
        SimpleHttpResponse response = httpClient
                .doPost(buildUrl(serviceUrl, PUT_POST_API, expectResponse),
                        builder.build());

        return getResponse(response);
    }

    /**
     * /api/query
     * @param builder request body object
     * @return response result
     * @throws IOException IOException
     */
    public SimpleHttpResponse pushQueries(QueryBuilder builder) throws IOException {
        return pushQueries(builder, ExpectResponse.STATUS_CODE);
    }

    /**
     * /api/query
     * @param builder request body object
     * @param expectResponse return information type (summary、detail)
     * @return response result
     * @throws IOException IOException
     */
    public SimpleHttpResponse pushQueries(QueryBuilder builder, ExpectResponse expectResponse) throws IOException {
        checkNotNull(builder);

        SimpleHttpResponse response = httpClient
                .doPost(buildUrl(serviceUrl, QUERY_POST_API, expectResponse),
                        builder.build());

        return response;
    }

    /**
     * /api/suggest
     * @param builder request body object
     * @return The response is an array of strings of the given type that match the query. If nothing was found to match the query, an empty array will be returned.
     */
    public SimpleHttpResponse pushSuggest(SuggestBuilder builder) throws IOException {
        checkNotNull(builder);
        SimpleHttpResponse response = httpClient
                .doPost(buildUrl(serviceUrl, SUGGEST_POST_API, ExpectResponse.STATUS_CODE),
                        builder.build());
        return response;
    }

    /**
     * /api/dropcaches
     * @return The response is a hash map of information. Unless something goes wrong, this should always result in a status of 200 and a message of Caches dropped.
     */
    public SimpleHttpResponse pushDropCaches() throws IOException {
        SimpleHttpResponse response = httpClient
                .doGet(buildUrl(serviceUrl, DROP_CACHES, ExpectResponse.STATUS_CODE));
        return response;
    }

    /**
     * /api/uid/uidmeta
     * @return
     */
    public SimpleHttpResponse pushDeleteUid(Map<String, String> params) throws IOException {
        SimpleHttpResponse response = httpClient
                .doDelete(buildUrl(serviceUrl, UID_META_DATA, ExpectResponse.STATUS_CODE), params);
        return response;
    }

    /**
     * url builder
     * @param serviceUrl TSDB server address
     * @param postApiEndPoint endpoint(e.g /api/put, /api/suggest)
     * @param expectResponse return information type (summary、detail)
     * @return complete url
     */
    private String buildUrl(String serviceUrl, String postApiEndPoint,
                            ExpectResponse expectResponse) {
        String url = serviceUrl + postApiEndPoint;

        switch (expectResponse) {
            case SUMMARY:
                url += "?summary";
                break;
            case DETAIL:
                url += "?details";
                break;
            default:
                break;
        }
        return url;
    }

    /**
     * Parse request response
     * @param httpResponse http request object
     * @return TSDB response object
     */
    private Response getResponse(SimpleHttpResponse httpResponse) {
        Response response = new Response(httpResponse.getStatusCode());
        String content = httpResponse.getContent();
        if (StringUtils.isNotEmpty(content)) {
            if (response.isSuccess()) {
                ErrorDetail errorDetail = mapper.fromJson(content,
                        ErrorDetail.class);
                response.setErrorDetail(errorDetail);
            } else {
                log.error("request failed!" + httpResponse);
            }
        }
        return response;
    }
}