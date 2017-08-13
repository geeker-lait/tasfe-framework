package com.tasfe.framework.crud.api.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Lait on 2017/8/6.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kvc {
    private String condition;

    private String key;

    private String symbol;

    private Object val;


    public Kvc(String condition, String key, String symbol) {
        this.condition = condition;
        this.key = key;
        this.symbol = symbol;
    }
}
