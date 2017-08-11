package com.tasfe.framework.crud.api.params;

/**
 * Created by Lait on 2017/8/11.
 */
public enum  Condition {

    AND("and"),
    OR("or");

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
