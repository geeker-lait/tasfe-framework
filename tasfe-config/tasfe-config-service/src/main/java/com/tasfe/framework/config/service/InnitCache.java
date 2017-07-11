package com.tasfe.framework.config.service;

import com.alibaba.fastjson.JSONObject;
import com.github.zkclient.IZkDataListener;
import com.github.zkclient.ZkClient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnitCache {
    private static final Logger LOGGER = LoggerFactory.getLogger(InnitCache.class);

    public InnitCache() {
    }

    public Map<String, Object> innitParam(Properties pro) throws Exception {
        Iterator i$ = pro.entrySet().iterator();

        while (i$.hasNext()) {
            Entry<Object, Object> entry = (Entry) i$.next();
            CacheManager.getStaticMap().put((String) entry.getKey(), entry.getValue());
        }

        if (ZkClientFactory.getZkClient() != null) {
            this.innitStaticMapByZk();
            this.innitActiveMapByZk();
        } else {
            this.innitStaticMapByServer();
            this.innitActiveMapByServer();
        }

        return CacheManager.getStaticMap();
    }

    public void innitActiveMapByZk() throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("tasfeSystemActiveMapDataFrom", "zkData");
        CacheManager.getActiveMap().put("tasfeSystemActiveMapDataFrom", map);
        LOGGER.info("ZOOKEEPER 初始化动态系统参数");
        String activeNode = CacheManager.getStaticMapValue("zk.node.active");
        if (activeNode != null && !activeNode.isEmpty()) {
            ZkClient zkClient = ZkClientFactory.getZkClient();
            if (zkClient == null) {
                throw new RuntimeException("ZOOKEEPER 初始化动态系统参数失败,ZKCLIENT对象获取失败");
            } else if (!zkClient.exists(activeNode)) {
                throw new RuntimeException("节点" + activeNode + "不存在");
            } else {
                List<String> activeList = zkClient.getChildren(activeNode);
                if (activeList != null && !activeList.isEmpty()) {
                    Iterator i$ = activeList.iterator();

                    while (i$.hasNext()) {
                        String node = (String) i$.next();
                        this.bindWatcher(activeNode, node);
                        byte[] data = zkClient.readData(activeNode + "/" + node, true);
                        if (data != null && data.length > 0) {
                            String result = new String(data, "UTF-8");
                            if (result != null && !result.isEmpty()) {
                                Map<String, Object> zMap = (Map) JSONObject.parseObject(result, Map.class);
                                LOGGER.debug("ZOOKEEPER 初始化动态系统参数,节点为{},数据为{}", activeNode + "/" + node, JSONObject.toJSONString(zMap));
                                CacheManager.getActiveMap().put(node.toLowerCase(), zMap);
                            }
                        }
                    }
                }

            }
        } else {
            LOGGER.info("本系统zk.node.active信息为空,不存在动态系统参数");
        }
    }

    private void bindWatcher(String parentNode, final String node) throws Exception {
        LOGGER.debug("ZOOKEEPER 动态参数节点绑定WATCHER");
        ZkClientFactory.getZkClient().subscribeDataChanges(parentNode + "/" + node, new IZkDataListener() {
            public void handleDataDeleted(String dataPath) throws Exception {
                InnitCache.LOGGER.info("ZOOKEEPER 节点{}删除,同步至此项目", dataPath);
                CacheManager.getActiveMap().remove(node.toLowerCase());
            }

            public void handleDataChange(String dataPath, byte[] data) throws Exception {
                InnitCache.LOGGER.info("ZOOKEEPER 动态参数节点{}修改,同步至此项目", dataPath);

                try {
                    if (data != null && data.length > 0) {
                        String result = new String(data, "UTF-8");
                        if (result != null && !result.isEmpty()) {
                            Map<String, Object> zMap = (Map) JSONObject.parseObject(result, Map.class);
                            CacheManager.getActiveMap().put(node.toLowerCase(), zMap);
                            InnitCache.LOGGER.debug("ZOOKEEPER 动态参数节点{}修改,修改后内容为{}", dataPath, JSONObject.toJSONString(zMap));
                        }
                    }
                } catch (Exception var5) {
                    InnitCache.LOGGER.error("ZOOKEEPER更新缓存数据失败", var5);
                }

            }
        });
    }

    private void innitStaticMapByZk() throws Exception {
        LOGGER.info("从ZOOKEEPER中初始化静态系统参数");
        ZkClient zkClient = ZkClientFactory.getZkClient();
        String staticNode = CacheManager.getStaticMapValue("zk.node.static");
        if (zkClient != null && staticNode != null && !staticNode.isEmpty()) {
            if (!zkClient.exists(staticNode)) {
                throw new RuntimeException("节点" + staticNode + "不存在");
            } else {
                List<String> staticList = zkClient.getChildren(staticNode);
                if (staticList != null && !staticList.isEmpty()) {
                    Iterator i$ = staticList.iterator();

                    while (true) {
                        Map zMap;
                        do {
                            String node;
                            String result;
                            do {
                                do {
                                    byte[] data;
                                    do {
                                        do {
                                            if (!i$.hasNext()) {
                                                return;
                                            }

                                            node = (String) i$.next();
                                            data = zkClient.readData(staticNode + "/" + node, true);
                                        } while (data == null);
                                    } while (data.length <= 0);

                                    result = new String(data, "UTF-8");
                                } while (result == null);
                            } while (result.isEmpty());

                            zMap = (Map) JSONObject.parseObject(result, Map.class);
                            LOGGER.debug("从ZOOKEEPER中初始化静态系统参数,节点为{},数据为{}", staticNode + "/" + node, JSONObject.toJSONString(zMap));
                        } while (zMap == null && zMap.isEmpty());

                        //Iterator i$ = zMap.entrySet().iterator();

                        while (i$.hasNext()) {
                            Entry<String, Object> entry = (Entry) i$.next();
                            CacheManager.getStaticMap().put(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        } else {
            throw new RuntimeException("ZOOKEEPER 初始化动态系统参数失败,application.properties中找不到zk.node.static信息");
        }
    }

    private void innitActiveMapByServer() throws Exception {
        Map<String, Object> map = new HashMap();
        map.put("tasfeSystemActiveMapDataFrom", "serverData");
        CacheManager.getActiveMap().put("tasfeSystemActiveMapDataFrom", map);
        LOGGER.info("ZOOKEEPER连接失败,从配置服务中获取动态资源");
        String activeNode = CacheManager.getStaticMapValue("zk.node.active");
        if (activeNode != null && !activeNode.isEmpty()) {
            String url = CacheManager.getStaticMapValue("zk.disconnect.configServer");
            if (url != null && !url.isEmpty()) {
                String path = activeNode.substring(activeNode.lastIndexOf("/", activeNode.lastIndexOf("/", activeNode.lastIndexOf("/") - 1) - 1));
                LOGGER.debug("从配置服务中初始化动态系统参数,路径为{}", url + path);
                String result = HttpClientTemplate.getRestTemplate().getForObject(url + path, String.class);
                LOGGER.debug("从配置服务中初始化动态系统参数,路径为{},返回数据为{}", url + path, result);
                boolean flag = false;
                if (result != null || !result.isEmpty()) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    Map zMap;
                    Iterator i$;
                    Entry entry;
                    if (jsonObject != null && "1".equals(jsonObject.get("status")) && "000000".equals(jsonObject.get("error"))) {
                        flag = true;
                        zMap = (Map) jsonObject.getObject("data", Map.class);
                        if (zMap != null && !zMap.isEmpty()) {
                            i$ = zMap.entrySet().iterator();
                            while (i$.hasNext()) {
                                entry = (Entry) i$.next();
                                //CacheManager.getActiveMap().put(entry.getKey(), entry.getValue());
                            }
                        }
                    } else if (jsonObject != null && "1".equals(jsonObject.get("status"))) {
                        jsonObject = jsonObject.getJSONObject("data");
                        if (jsonObject != null && "1".equals(jsonObject.get("resultStatus"))) {
                            flag = true;
                            zMap = (Map) jsonObject.getObject("value", Map.class);
                            if (zMap != null && !zMap.isEmpty()) {
                                i$ = zMap.entrySet().iterator();

                                while (i$.hasNext()) {
                                    entry = (Entry) i$.next();
                                    //CacheManager.getActiveMap().put(entry.getKey(), entry.getValue());
                                }
                            }
                        }
                    }
                }

                if (flag) {
                    LOGGER.info("从配置服务中获取动态资源成功");
                } else {
                    LOGGER.info("从配置服务中获取动态资源失败");
                    throw new RuntimeException("ZOOKEEPER连接失败,从配置服务中获取动态资源失败");
                }
            } else {
                throw new RuntimeException("配置服务连接失败,application.properties中找不到zk.disconnect.configServer信息");
            }
        } else {
            LOGGER.info("本系统zk.node.active信息为空,不存在动态系统参数");
            map = new HashMap();
            map.put("tasfeSystemActiveMapDataFrom", "zkData");
            CacheManager.getActiveMap().put("tasfeSystemActiveMapDataFrom", map);
        }
    }

    private void innitStaticMapByServer() throws Exception {
        LOGGER.info("ZOOKEEPER连接失败,从配置服务中获取静态资源");
        String url = CacheManager.getStaticMapValue("zk.disconnect.configServer");
        String staticNode = CacheManager.getStaticMapValue("zk.node.static");
        if (url != null && staticNode != null && !url.isEmpty() && !staticNode.isEmpty()) {
            String path = staticNode.substring(staticNode.lastIndexOf("/", staticNode.lastIndexOf("/", staticNode.lastIndexOf("/") - 1) - 1));
            LOGGER.debug("从配置服务中初始化静态系统参数,路径为{}", url + path);
            String result = HttpClientTemplate.getRestTemplate().getForObject(url + path, String.class);
            LOGGER.debug("从配置服务中初始化静态系统参数,路径为{},返回数据为{}", url + path, result);
            boolean flag = false;
            if (result != null || !result.isEmpty()) {
                JSONObject jsonObject = JSONObject.parseObject(result);
                Map zMap;
                Iterator i$;
                Entry entry;
                if (jsonObject != null && "1".equals(jsonObject.get("status")) && "000000".equals(jsonObject.get("error"))) {
                    flag = true;
                    zMap = (Map) jsonObject.getObject("data", Map.class);
                    if (zMap != null && !zMap.isEmpty()) {
                        i$ = zMap.entrySet().iterator();

                        while (i$.hasNext()) {
                            entry = (Entry) i$.next();
                            CacheManager.getStaticMap().put((String) entry.getKey(), entry.getValue());
                        }
                    }
                } else if (jsonObject != null && "1".equals(jsonObject.get("status"))) {
                    jsonObject = jsonObject.getJSONObject("data");
                    if (jsonObject != null && "1".equals(jsonObject.get("resultStatus"))) {
                        flag = true;
                        zMap = (Map) jsonObject.getObject("value", Map.class);
                        if (zMap != null && !zMap.isEmpty()) {
                            i$ = zMap.entrySet().iterator();

                            while (i$.hasNext()) {
                                entry = (Entry) i$.next();
                                CacheManager.getStaticMap().put((String) entry.getKey(), entry.getValue());
                            }
                        }
                    }
                }
            }

            if (flag) {
                LOGGER.info("从配置服务中获取静态资源成功");
            } else {
                LOGGER.info("从配置服务中获取静态资源失败");
                throw new RuntimeException("ZOOKEEPER连接失败,从配置服务中获取静态资源失败");
            }
        } else {
            throw new RuntimeException("配置服务连接失败,application.properties中找不到zk.disconnect.configServer/zk.node.static信息");
        }
    }
}