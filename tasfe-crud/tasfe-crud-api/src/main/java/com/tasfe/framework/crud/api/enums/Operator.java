package com.tasfe.framework.crud.api.enums;

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
    LIKE("LIKE"),
    LLIKE("LLIKE"),
    RLIKE("RLIKE"),
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

    public static void main(String[] args) {
        System.out.printf(Operator.LLIKE.toString());
    }

}
