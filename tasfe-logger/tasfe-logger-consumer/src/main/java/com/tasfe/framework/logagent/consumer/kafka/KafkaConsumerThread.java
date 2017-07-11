package com.tasfe.framework.logagent.consumer.kafka;

import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.logagent.consumer.annotation.ServiceRegister;
import com.tasfe.framework.logagent.consumer.api.IStorageApi;
import com.tasfe.framework.logagent.consumer.elasticsearch.ElasticsearchClient;
import com.tasfe.framework.logagent.consumer.entity.LogConfigCache;
import com.tasfe.framework.logagent.consumer.entity.LogcenterConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by lait on 2017/6/19.
 */
public class KafkaConsumerThread implements Runnable {
    private Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    private KafkaStream<byte[], byte[]> stream;
    private Properties properties;

    public KafkaConsumerThread(KafkaStream<byte[], byte[]> stream, Properties properties) {
        this.stream = stream;
        this.properties = properties;
    }


    @Override
    public void run() {
        ConsumerIterator<byte[], byte[]> it = stream.iterator();
        while (it.hasNext()) {
            MessageAndMetadata<byte[], byte[]> mam = it.next();
            String jsonStr = "";
            try {
                jsonStr = new String(mam.message());
                JSONObject jsonObject = JSONObject.parseObject(jsonStr);
                LogcenterConfig config = LogConfigCache.getLogConfigCache(jsonObject);
                IStorageApi iStorageApi = ServiceRegister.getInstance().getProvider(config.getStorageType());
                iStorageApi.save(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("partition[" + mam.partition() + "]," + "offset[" + mam.offset() + "], " + jsonStr, e);
                continue;
            }
        }
    }
}
