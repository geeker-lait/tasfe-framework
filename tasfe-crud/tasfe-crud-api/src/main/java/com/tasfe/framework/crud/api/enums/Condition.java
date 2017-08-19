package com.tasfe.framework.crud.api.enums;

/**
 * Created by Lait on 2017/8/11.
 */
public enum  Condition {

    AND("AND"),
    OR("OR");

    private String value;
    Condition(String value) {
        this.value = value;
    }

    public String value(){
        return this.value;
    }


    //case AND:return"and"; break;
        //case OR:return"or"; break;
}
