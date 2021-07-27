package org.opentsdb.client;

import java.io.IOException;

import org.opentsdb.client.builder.MetricBuilder;
import org.opentsdb.client.request.QueryBuilder;
import org.opentsdb.client.response.Response;
import org.opentsdb.client.response.SimpleHttpResponse;

public interface Client {

	String PUT_POST_API = "/api/put";

    String QUERY_POST_API = "/api/query";

    String SUGGEST_POST_API = "/api/suggest";

	String DROP_CACHES = "/api/dropcaches";

	String UID_META_DATA = "/api/uid/uidmeta";

	/**
	 * Sends metrics from the builder to the server.
	 *
	 * @param builder
	 *            metrics builder
	 * @return response from the server
	 * @throws IOException
	 *             problem occurred sending to the server
	 */
	Response pushMetrics(MetricBuilder builder) throws IOException;

	SimpleHttpResponse pushQueries(QueryBuilder builder) throws IOException;
}