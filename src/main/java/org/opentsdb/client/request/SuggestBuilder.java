package org.opentsdb.client.request;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import static com.google.common.base.Preconditions.checkState;

/**
 * Created by shifeng on 2016/5/19.
 * MyProject
 */
public class SuggestBuilder {
    private Suggest suggest = new Suggest();
    private transient Gson mapper;

    private SuggestBuilder() {
        GsonBuilder builder = new GsonBuilder();
        mapper = builder.create();
    }

    public static SuggestBuilder getInstance() {
        return new SuggestBuilder();
    }

    public Suggest getSuggest() {
        return this.suggest;
    }

    public String build() throws IOException {
        checkState(suggest.getType() != null, " suggest request must contain type! ");
        return mapper.toJson(suggest);
    }
}
