package com.tasfe.framework.logagent.consumer.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.logagent.consumer.elasticsearch.ElasticsearchClient;
import com.tasfe.framework.logagent.consumer.utils.Constants;
import com.tasfe.framework.logagent.consumer.utils.LogConsumerException;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by lait on 2017/6/21.
 */
@Component
public class LogConfigCache {
    private static Map<String, LogcenterConfig> logconfigMap;

    @PostConstruct
    private void initialize() {
        logconfigMap = new ConcurrentHashMap<>();
        SearchResponse response = ElasticsearchClient.getClient()
                .prepareSearch(Constants.METADATA_INDEX)
                .setTypes(Constants.METADATA_TYPE)
                .get();
        JSONObject jsonObject = JSON.parseObject(response.toString());
        JSONArray hitArray = (JSONArray) jsonObject.getJSONObject("hits").get("hits");

        for (int i = 0; i < hitArray.size(); i++) {
            JSONObject document = (JSONObject) hitArray.get(i);
            String jsonStr = document.get("_source").toString();

            LogcenterConfig config = JSONObject.parseObject(jsonStr, LogcenterConfig.class);
            String key = buildCacheMapKey(config.getProductLine(), config.getAppName());
            logconfigMap.put(key, config);

        }
    }

    private static String buildCacheMapKey(String productLine, String appName) {
        return productLine + "_" + appName;
    }

    public static LogcenterConfig getLogConfigCache(JSONObject jsonObject) throws LogConsumerException {
        String productLine = String.valueOf(jsonObject.get("productLine"));
        String appName = String.valueOf(jsonObject.get("appName"));

        String key = buildCacheMapKey(productLine, appName);
        if (logconfigMap.get(key) == null) {
            LogcenterConfig config = LogConfigFactory.createConfig(jsonObject);
            logconfigMap.put(key, config);
        }
        return logconfigMap.get(key);
    }

}
