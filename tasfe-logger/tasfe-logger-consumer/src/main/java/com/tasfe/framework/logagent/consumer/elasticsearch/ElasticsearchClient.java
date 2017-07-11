package com.tasfe.framework.logagent.consumer.elasticsearch;

import com.tasfe.framework.logagent.consumer.utils.PropertyUtils;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by lait on 2017/6/19.
 */
@Component
public class ElasticsearchClient {
    private static Logger logger = LoggerFactory.getLogger(ElasticsearchClient.class);

    private static TransportClient transportClient;
    private static String clusterName = "my-application";
    private static String addresses = "192.168.33.117:9300";

    @PostConstruct
    public void init() {
        //Properties properties = PropertyUtils.getProperties();
        Properties properties = new Properties();
        clusterName = properties.getProperty("consumer.es.clusterName", clusterName);
        addresses = properties.getProperty("consumer.es.addresses", addresses);

        newClient();
    }

    private static void newClient() {
        logger.info("[elasticsearch.cluster.name -> " + clusterName + "]");
        logger.info("[elasticsearch.addresses -> " + addresses + "]");
        Settings settings = Settings.builder().put("client.transport.sniff", true).put("cluster.name", clusterName).build();
        transportClient = new PreBuiltTransportClient(settings);
        String[] addressArr = addresses.split(",");
        for (String address : addressArr) {
            String[] hostport = address.split(":");
            transportClient.addTransportAddress(new InetSocketTransportAddress(new InetSocketAddress(hostport[0], Integer.parseInt(hostport[1]))));
        }
        logger.info("elasticsearch client connection successful");
    }

    public static TransportClient getClient() {
        if (transportClient == null)
            newClient();
        return transportClient;
    }
}
