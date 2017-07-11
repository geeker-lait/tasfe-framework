package com.tasfe.framework.config.service;

import com.github.zkclient.IZkStateListener;
import com.github.zkclient.ZkClient;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZkClientFactory {
    private static ZkClient zkClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkClientFactory.class);

    public ZkClientFactory() {
    }

    public static synchronized ZkClient getZkClient() {
        if(zkClient == null) {
            try {
                String connectString = CacheManager.getStaticMapValue("zk.connectString");
                String sessionTimeout = CacheManager.getStaticMapValue("zk.sessionTimeout");
                String connectionTimeout = CacheManager.getStaticMapValue("zk.connectionTimeout");
                if(connectString == null || connectString.isEmpty() || sessionTimeout == null || sessionTimeout.isEmpty() || connectionTimeout == null || connectionTimeout.isEmpty()) {
                    throw new RuntimeException("配置服务连接失败,application.properties中找不到zk.connectString/zk.sessionTimeout/zk.connectionTimeout信息");
                }

                zkClient = new ZkClient(connectString, Integer.parseInt(sessionTimeout), Integer.parseInt(connectionTimeout));
            } catch (Exception var6) {
                LOGGER.error("ZOOKEEPER连接失败", var6);
                zkClient = null;
            } finally {
                if(zkClient != null) {
                    subscribeStateChanges();
                }

            }
        }

        return zkClient;
    }

    private static void subscribeStateChanges() {
        getZkClient().subscribeStateChanges(new IZkStateListener() {
            public void handleStateChanged(KeeperState state) throws Exception {
                if(state == null) {
                    ZkClientFactory.LOGGER.info("ZOOKEEPER连接状态改变");
                } else {
                    ZkClientFactory.LOGGER.info("ZOOKEEPER连接状态改变为" + state.name());
                }

            }

            public void handleNewSession() throws Exception {
                ZkClientFactory.LOGGER.info("SESSIONTIMEOUT,ZOOKEEPER重连,产生新SESSION");
            }
        });
    }
}
