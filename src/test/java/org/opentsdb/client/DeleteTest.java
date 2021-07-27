package org.opentsdb.client;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import org.junit.Test;
import org.opentsdb.client.ExpectResponse;
import org.opentsdb.client.tsdbPartner.EasyDelete;
import org.opentsdb.client.tsdbPartner.EasyQuery;
import org.opentsdb.client.util.Aggregator;

/**
 * @author Peter Zhu (～￣(OO)￣)ブ
 * @date 2021-07-22 11:22 上午
 * @description
 */
public class DeleteTest {

    // 1625097600 = 2021-07-01 08:00:00
    // 946684800 = 2000-01-01 08:00:00
    // 1633046400 = 2021-10-01 08:00:00

    /**
     * 删除查询到的数据
     */
    @Test
    public void deleteTest(){
        JSONArray deleteRes = EasyDelete.delete(
                "http://flink01:4242",
                "test724315",
                Aggregator.none.toString(),
                946684800,
                1633046400,
                ExpectResponse.STATUS_CODE
        );

        System.out.println(JSONUtil.toJsonPrettyStr(deleteRes));
    }
}
