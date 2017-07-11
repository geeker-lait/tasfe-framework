package com.tasfe.framework.logagent.consumer.elasticsearch;

import com.tasfe.framework.logagent.consumer.utils.PropertyUtils;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;

import java.util.Properties;

/**
 * Created by lait on 2016/12/28.
 */
public class ElasticsearchBulkTools {

    private static BulkProcessor bulkProcessor = null;

    static {
        //Properties properties = PropertyUtils.getProperties();
        Properties properties = new Properties();
        String maxBulkCount = properties.getProperty("logcenter.elasticsearch.maxBulkCount", "1000");
        String maxCommitInterval = properties.getProperty("logcenter.elasticsearch.maxCommitInterval", "10");

        bulkProcessor = BulkProcessor.builder(
                ElasticsearchClient.getClient(),
                new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId,
                                           BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          BulkResponse response) {
                    }

                    @Override
                    public void afterBulk(long executionId,
                                          BulkRequest request,
                                          Throwable failure) {
                        System.out.println("id=" + executionId + ",cause=" + failure + "");
                    }
                }
        ).setBulkActions(Integer.valueOf(maxBulkCount))
                .setBulkSize(new ByteSizeValue(20, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(Long.valueOf(maxCommitInterval)))
                .setConcurrentRequests(1)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }

    /**
     * 加入索引请求到缓冲池
     *
     * @param builder
     */
    public static void addUpdateBuilderToBulk(IndexRequestBuilder builder) {
        try {
            bulkProcessor.add(builder.request());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
    }
}