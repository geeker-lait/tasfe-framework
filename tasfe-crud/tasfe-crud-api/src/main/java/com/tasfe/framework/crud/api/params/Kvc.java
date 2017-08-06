package com.tasfe.framework.crud.api.params;

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
    private String key;

    private Object val;

    private String condition;
}
