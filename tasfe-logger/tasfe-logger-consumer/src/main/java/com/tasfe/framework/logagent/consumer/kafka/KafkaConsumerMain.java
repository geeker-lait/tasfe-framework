package com.tasfe.framework.logagent.consumer.kafka;

import com.tasfe.framework.logagent.consumer.utils.PropertyUtils;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lait on 2017/6/19.
 */
@Component
public class KafkaConsumerMain {
    private Logger logger = LoggerFactory.getLogger(KafkaConsumerMain.class);
    private static Properties properties;

    private static Properties props;
    private static String zookeeperConnect = "192.168.33.117:2181";
    private static String groupId = "logcenter";
    private static String kafkaTopic = "tasfe.log4j";
    private static String consumerThreadNum = "";

    private static String zookeeperConnectiontimeoutMs = "30000";
    private static String zookeeperSessionTimeoutMs = "400";
    private static String zookeeperSyncTimeMs = "200";
    private static String autoCommitIntervalMs = "1000";

    @PostConstruct
    public void init() {
        //Properties properties = PropertyUtils.getProperties();
        Properties properties = new Properties();
        zookeeperConnect = properties.getProperty("consumer.zookeeper.connect", zookeeperConnect);
        groupId = properties.getProperty("consumer.group.id", groupId);
        kafkaTopic = properties.getProperty("consumer.topicName", kafkaTopic);
        consumerThreadNum = properties.getProperty("consumer.threadNum", consumerThreadNum);

        logger.info("[consumer.zookeeper.connect -> " + zookeeperConnect + "] " +
                "[consumer.group.id -> " + groupId + "] " +
                "[consumer.topicName -> " + kafkaTopic + "] " +
                "[consumer.threadNum -> " + consumerThreadNum + "]");

        props = new Properties();
        props.put("zookeeper.connect", zookeeperConnect);
        props.put("group.id", groupId);
        props.put("zookeeper.connectiontimeout.ms", zookeeperConnectiontimeoutMs);
        props.put("zookeeper.session.timeout.ms", zookeeperSessionTimeoutMs);
        props.put("zookeeper.sync.time.ms", zookeeperSyncTimeMs);
        props.put("auto.commit.interval.ms", autoCommitIntervalMs);
        props.put("auto.offset.reset", "smallest");
        this.run();
    }

    public void run() {
        logger.info("========启动消费者客户端=========");
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(kafkaTopic, new Integer(consumerThreadNum));
        ConsumerConfig consumerConfig = new ConsumerConfig(props);
        ConsumerConnector consumer = kafka.consumer.Consumer.createJavaConsumerConnector(consumerConfig);
        Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> streams = consumerMap.get(kafkaTopic);
        ExecutorService executor = Executors.newFixedThreadPool(Integer.parseInt(consumerThreadNum));
        for (final KafkaStream stream : streams) {
            executor.submit(new KafkaConsumerThread(stream, properties));
        }

    }
}
