package com.tasfe.framework.crud.api.criteria;

import com.tasfe.framework.crud.api.enums.Operator;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Lait on 2017/8/11.
 */
@Data
public class Criteria {
    private Class<?> clazz;


    private Limit limit;
    private Where where = new Where(this);

    private List<String> selects = new ArrayList<>();

    private List<String> order = new ArrayList<>();

    private List<String> group = new ArrayList<>();

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
        return where;
    }

    public Where groupWhere(){
        where = new Where(this);
        return where;
    }


     public Criteria group(Where where){


        return  this;
    }



    public Criteria orderBy(String ...fileds){
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


    public Criteria groupBy(String ...fileds) {

        return this;
    }
}
