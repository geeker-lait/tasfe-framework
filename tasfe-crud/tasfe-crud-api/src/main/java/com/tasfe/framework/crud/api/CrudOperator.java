package com.tasfe.framework.crud.api;

import com.tasfe.framework.crud.api.operator.*;

/**
 * Created by Lait on 2017/5/29.
 */
public interface CrudOperator extends
        InsertOperator,
        SelectOperator,
        UpdateOperator,
        DeleteOperator,
        PagingOperator,
        FunctionOperator{


}
