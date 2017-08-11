package com.tasfe.framework.crud.api.params;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lait on 2017/8/6.
 */
@Getter
public class Where {
    private List<Kvc> kvcs = new ArrayList<>();

    private String prefix  = "1=1";

    private Criteria criteria;

    public Where() {
    }

    public Where(Criteria criteria) {
        this.criteria = criteria;
    }


    public Where(String prefix) {
        this.prefix = prefix;
    }


    public Where and(String key,Operator symbol){
        Kvc kvc = new Kvc(Condition.AND.value(),key,symbol.value());
        kvcs.add(kvc);
        return this;
    }


    public Where and(String key, Operator symbol, Object val){
        Kvc kvc = new Kvc(Condition.AND.value(),key,symbol.value(),val);
        kvcs.add(kvc);
        return this;
    }


    public Where or(String key,Operator symbol){
        Kvc kvc = new Kvc(Condition.OR.value(),key,symbol.value());
        kvcs.add(kvc);
        return this;
    }


    public Where or(String key, Operator symbol, Object val){
        Kvc kvc = new Kvc(Condition.OR.value(),key,symbol.value(),val);
        kvcs.add(kvc);
        return this;
    }


/*    public Where like(String key,Operator symbol,Object val){
        Kvc kvc = new Kvc(Condition.LIKE.value(),key,symbol.value(),val);
        kvcs.add(kvc);
        return this;
    }*/

    public Criteria endWhere(){
        return criteria;
    }


}
