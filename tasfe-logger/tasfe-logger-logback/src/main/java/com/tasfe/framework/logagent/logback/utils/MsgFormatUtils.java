package com.tasfe.framework.logagent.logback.utils;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * Created by lait on 2017/6/19.
 */
public class MsgFormatUtils {

    public static String recombineLogContent(String patternType, String message) {
        JSONObject jsonObject = null;
        try {
            String[] messageArr = message.split(" ");
            Map<String, String> appenderPatternMap = ParseLogbackXml.getAppenderPatternMap();
            String[] kafkaPattern = appenderPatternMap.get(patternType).split(" ");

            jsonObject = new JSONObject();
            for (int i = 0; i < kafkaPattern.length; i++) {
                String pattern = kafkaPattern[i];
                String str = pattern.replaceAll("%", "").replaceAll("\\$", "").replaceAll("\\{", "").replaceAll("\\}", "");
                jsonObject.put(str, messageArr[i].replaceAll(Constants.SYMBOL_SPACE, " "));
            }
        } catch (Exception e) {
        }
        return jsonObject.toJSONString();
    }
}
