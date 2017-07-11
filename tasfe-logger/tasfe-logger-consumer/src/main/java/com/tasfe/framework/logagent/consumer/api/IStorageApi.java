package com.tasfe.framework.logagent.consumer.api;

import com.alibaba.fastjson.JSONObject;
import com.tasfe.framework.logagent.consumer.utils.LogConsumerException;

/**
 * Created by lait on 2017/6/21.
 */
public interface IStorageApi {
    public void save(JSONObject jsonObject) throws LogConsumerException;
}
