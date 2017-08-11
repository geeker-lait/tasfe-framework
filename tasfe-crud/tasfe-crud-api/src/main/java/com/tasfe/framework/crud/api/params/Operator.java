package com.tasfe.framework.crud.api.params;

/**
 * Created by Lait on 2017/8/11.
 */
public enum Operator {

    EQ("="),
    NEQ("!="),
    GT(">"),
    GTE(">="),
    LT("<"),
    LTE("<="),
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%");

    private String value;
    Operator(String value) {
        this.value = value;
    }
    public String value(){
        return this.value;
    }

}
