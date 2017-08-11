package com.tasfe.framework.crud.api.params;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Lait on 2017/8/11.
 */
@Data
public class Criteria {
    private Class<?> clazz;

    private List<String> selects;

    private Where where;

    private Limit limit;

    private List<String> order;

    private Criteria(){

    }


    public static <T>Criteria from(Class<T> clazz){
        Criteria criteria = new Criteria();
        criteria.setClazz(clazz);
        return criteria;
    }


    public Criteria select(String ...fileds){
        this.selects.addAll(Arrays.asList(fileds));
        return this;
    }

    public Where where(){
        where = new Where(this);
        return where;
    }



    public Criteria order(String ...fileds){
        this.order.addAll(Arrays.asList(fileds));
        return this;
    }


    public Criteria limit(int start,int offset){
        this.limit = new Limit(start,offset);
        return this;
    }


    public static void main(String[] args) {
        Criteria criteria = Criteria.from(Object.class).where().and("key", Operator.EQ).endWhere().limit(1,10);

    }


}
