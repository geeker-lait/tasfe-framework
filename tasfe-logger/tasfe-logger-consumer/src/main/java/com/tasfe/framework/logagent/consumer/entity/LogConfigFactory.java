package com.tasfe.framework.logagent.consumer.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.logagent.consumer.elasticsearch.ElasticsearchClient;
import com.tasfe.framework.logagent.consumer.utils.Constants;
import com.tasfe.framework.logagent.consumer.utils.LogConsumerException;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

/**
 * Created by lait on 2017/6/21.
 */
public class LogConfigFactory {

    public static LogcenterConfig createConfig(JSONObject jsonInput) throws LogConsumerException {
        String productLine = String.valueOf(jsonInput.get("productLine"));
        String appName = String.valueOf(jsonInput.get("appName"));
        QueryBuilder qb = QueryBuilders.queryStringQuery("productLine:'" + productLine + "' AND appName:'" + appName + "'");
        SearchResponse response = ElasticsearchClient.getClient()
                .prepareSearch(Constants.METADATA_INDEX)
                .setTypes(Constants.METADATA_TYPE)
                .setQuery(qb)
                .get();
        JSONObject jsonObject = JSON.parseObject(response.toString());
        JSONArray hitArray = (JSONArray) jsonObject.getJSONObject("hits").get("hits");
        if (hitArray.size() == 0) {
            throw new LogConsumerException("index does not exist,please check the configuration of the .logcenter index");
        }
        JSONObject document = (JSONObject) hitArray.get(0);
        String jsonStr = document.get("_source").toString();

        return JSONObject.parseObject(jsonStr, LogcenterConfig.class);
    }
}
