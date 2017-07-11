package com.tasfe.framework.config.service;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CacheManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManager.class);
    private static Map<String, Object> staticMap;
    private static Map<String, Map<String, Object>> activeMap;

    public CacheManager() {
    }

    public static Map<String, Object> getStaticMap() {
        if(staticMap == null) {
            staticMap = new HashMap();
        }

        return staticMap;
    }

    public static Map<String, Map<String, Object>> getActiveMap() {
        if(activeMap == null) {
            activeMap = new HashMap();
        }

        return activeMap;
    }

    public static String getStaticMapValue(String nid) {
        return (String)getStaticMap().get(nid);
    }

    public static String getActiveMapValue(String nid) {
        String value = null;
        Iterator i$ = getActiveMap().entrySet().iterator();

        while(i$.hasNext()) {
            Entry<String, Map<String, Object>> entry = (Entry)i$.next();
            Map<String, Object> map = (Map)entry.getValue();
            if(map != null && !map.isEmpty()) {
                value = (String)map.get(nid);
                if(value != null) {
                    break;
                }
            }
        }

        LOGGER.info("从CacheManager中获取ACTIVE数据,key为{},value为{}", nid, value);
        return value;
    }

    public static boolean modifyActiveValue(String nid, String value) throws IOException {
        boolean flag = false;
        if(nid != null && value != null && !nid.isEmpty() && !value.isEmpty()) {
            String activeNode = getStaticMapValue("zk.node.active");
            String url = getStaticMapValue("zk.disconnect.configServer");
            if(activeNode != null && !activeNode.isEmpty() && url != null && !url.isEmpty()) {
                String path = activeNode.substring(activeNode.lastIndexOf("/", activeNode.lastIndexOf("/", activeNode.lastIndexOf("/") - 1) - 1));
                String uri = url + path + "/" + nid + "&" + value;
                LOGGER.info("修改动态系统参数,路径为{}", uri);
                String result = HttpClientTemplate.getRestTemplate().getForObject(uri,String.class);
                LOGGER.info("修改动态系统参数,路径为{},返回值为{}", uri, result);
                if(result != null || !result.isEmpty()) {
                    JSONObject jsonObject = JSONObject.parseObject(result);
                    if(jsonObject != null && "1".equals((String)jsonObject.get("status")) && "000000".equals((String)jsonObject.get("error"))) {
                        flag = true;
                    }
                }

                return flag;
            } else {
                LOGGER.info("修改动态系统参数失败,application.properties中找不到zk.disconnect.configServer/zk.node.active信息");
                return flag;
            }
        } else {
            throw new RuntimeException("nid 或者 value 不得为空");
        }
    }
}
