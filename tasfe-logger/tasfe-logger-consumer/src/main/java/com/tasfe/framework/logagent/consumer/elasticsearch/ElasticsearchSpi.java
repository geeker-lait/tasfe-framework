package com.tasfe.framework.logagent.consumer.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.logagent.consumer.annotation.Provider;
import com.tasfe.framework.logagent.consumer.api.AbstractConsumer;
import com.tasfe.framework.logagent.consumer.entity.LogConfigCache;
import com.tasfe.framework.logagent.consumer.entity.LogcenterConfig;
import com.tasfe.framework.logagent.consumer.utils.Constants;
import com.tasfe.framework.logagent.consumer.utils.LogConsumerException;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.settings.Settings;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lait on 2017/6/20.
 */
@Component
@Provider(value = "es")
public class ElasticsearchSpi extends AbstractConsumer {

    public void save(JSONObject jsonObject) throws LogConsumerException {
        if (jsonObject == null)
            return;
        LogcenterConfig config = LogConfigCache.getLogConfigCache(jsonObject);
        String indexName = buildIndexName(config);
        boolean isExists = indexIsExists(indexName);
        if (!isExists)
            crateIndex(config);
        String id = jsonObject.getString(Constants.PRIMARY_KEY);
        jsonObject.remove(Constants.PRIMARY_KEY);
        String source = "";
        switch (config.getContentType()) {
            case "0"://字符串
                source = jsonObject.toJSONString();
                break;
            case "1"://json
                source = ((JSONObject) jsonObject.get("msg")).toJSONString();
                break;
        }
        IndexRequestBuilder builder = ElasticsearchClient.getClient()
                .prepareIndex(indexName, config.getTypeName())
                .setId(String.valueOf(id))
                .setSource(source);

        ElasticsearchBulkTools.addUpdateBuilderToBulk(builder);
    }

    public boolean crateIndex(LogcenterConfig config) {
        String indexName = buildIndexName(config);
        CreateIndexResponse response = ElasticsearchClient.getClient().admin().indices().prepareCreate(indexName)
                .setSettings(Settings.builder()
                        .put("index.number_of_shards", 5)
                        .put("index.number_of_replicas", 1)
                )
                .addMapping(config.getTypeName(), config.getTypeMapping())
                .get();
        return response.isAcknowledged();
    }

    public String buildIndexName(LogcenterConfig config) {
        Date date = new Date();
        DateFormat sf = new SimpleDateFormat(config.getIndexFormat());
        String indexPattern = sf.format(date);
        return config.getProductLine() + "_" + config.getAppName() + "-" + indexPattern;
    }

    public boolean indexIsExists(String indexName) {
        IndicesExistsRequest request = new IndicesExistsRequest(indexName);
        IndicesExistsResponse response = ElasticsearchClient.getClient().admin().indices()
                .exists(request)
                .actionGet();
        return response.isExists();
    }

    public static void main(String[] args) {
        String json = "{\"productLine\":\"fx1\",\"timeStamp\":\"2017-06-20 15:43:50.870\",\"msg\":\"Running http netty server on 9999 ...\",\"logFormat\":\"logFormat_IS_UNDEFINED\",\"level\":\"INFO\",\"appName\":\"ee2.1\",\"ip\":\"192.168.25.67\",\"id\":\"b5693a357b6e4606a16114bd1b548a37\",\"thread\":\"main\"}";
        try {
            LogcenterConfig logcenterConfig = LogConfigCache.getLogConfigCache(JSONObject.parseObject(json));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
