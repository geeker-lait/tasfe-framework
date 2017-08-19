package com.tasfe.framework.crud.api.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Lait on 2017/8/11.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Limit {

    private int start;

    private int offset;

}
