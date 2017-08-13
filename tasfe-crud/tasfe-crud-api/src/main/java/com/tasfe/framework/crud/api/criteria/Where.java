package com.tasfe.framework.crud.api.criteria;

import com.tasfe.framework.crud.api.configs.Configs;
import com.tasfe.framework.crud.api.enums.Condition;
import com.tasfe.framework.crud.api.enums.Operator;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/8/6.
 */
@Getter
public class Where implements Configs {
    private List<Kvc> kvcs = new ArrayList<>();

    private String prefix = "1=1";

    private Criteria criteria;

    // 是否驼峰转下划线
    private boolean camelCase = true;

    // 增加组
    private boolean group;


    public Where() {
    }

    public Where(Criteria criteria) {
        this.criteria = criteria;
    }


    public Where(Criteria criteria, boolean group) {
        this.criteria = criteria;
        this.group = group;
    }


    public Where(String prefix) {
        this.prefix = prefix;
    }


    public Where and(String key, Operator symbol) {
        // 如果需要转换驼峰
        if(camelCase){
            key = camelToUnderline(key);
        }
        Kvc kvc = new Kvc(Condition.AND.value(), key, symbol.value());
        kvcs.add(kvc);
        return this;
    }


    public Where and(String key, Operator symbol, Object val) {
        // 如果需要转换驼峰
        if(camelCase){
            key = camelToUnderline(key);
        }
        Kvc kvc = new Kvc(Condition.AND.value(), key, symbol.value(), val);
        kvcs.add(kvc);
        return this;
    }


    public Where or(String key, Operator symbol) {
        // 如果需要转换驼峰
        if(camelCase){
            key = camelToUnderline(key);
        }
        Kvc kvc = new Kvc(Condition.OR.value(), key, symbol.value());
        kvcs.add(kvc);
        return this;
    }


    public Where or(String key, Operator symbol, Object val) {
        // 如果需要转换驼峰
        if(camelCase){
            key = camelToUnderline(key);
        }
        Kvc kvc = new Kvc(Condition.OR.value(), key, symbol.value(), val);
        kvcs.add(kvc);
        return this;
    }



    /**
     * 将下划线标识转换为驼峰
     *
     * @param text
     * @return underline
     */
    private  String camelToUnderline(String text) {
        if (text == null || "".equals(text.trim())) {
            return "";
        }
        StringBuffer result = new StringBuffer(text.length() + 1);
        result.append(text.substring(0, 1));
        for (int i = 1; i < text.length(); i++) {
            if (!Character.isLowerCase(text.charAt(i))) {
                result.append('_');
            }
            result.append(text.substring(i, i + 1));
        }
        return result.toString().toLowerCase();
    }

/*    public Where like(String key,Operator symbol,Object val){
        Kvc kvc = new Kvc(Condition.LIKE.value(),key,symbol.value(),val);
        kvcs.add(kvc);
        return this;
    }*/

    public Criteria endWhere() {
        return criteria;
    }


}
