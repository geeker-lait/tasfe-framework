package com.tasfe.framework.crud.mysql.impls.mybatis.provider;

import java.util.Map;

import org.apache.ibatis.jdbc.SQL;

/**
 * Created by Lait on 2017/4/15
 * 基于注解的方式，此防方式将废弃，因为每个对应的Entity还需要些一个mapper.xml
 */
public class CrudDaoProvider {

    @Deprecated
    public String insertSelectiveSql(final Map<String, Object> param) {
        return new SQL() {
            {
                INSERT_INTO((String) param.get("tableName"));

                @SuppressWarnings("unchecked")
                Map<String, String> mapping = (Map<String, String>) param.get("columnValueMapping");
                for (String key : mapping.keySet()) {
                    VALUES(key, "#{columnValueMapping." + key + "}");
                }

            }
        }.toString();
    }

    @Deprecated
    public String updateByPrimaryKeySql(final Map<String, Object> param) {
        return new SQL() {
            {
                UPDATE((String) param.get("tableName"));

                @SuppressWarnings("unchecked")
                Map<String, String> mapping = (Map<String, String>) param.get("columnValueMapping");
                for (String key : mapping.keySet()) {
                    SET(key + "=" + "#{columnValueMapping." + key + "}");
                }

                WHERE((String) param.get("primaryKey") + "=" + param.get("primaryValue"));

            }
        }.toString();
    }




}
