package com.tasfe.framework.crud.api.operator.mysql;

import com.tasfe.framework.crud.api.Crudable;

/**
 * Created by Lait on 2017/7/10.
 */
public interface RdbOperator extends Crudable {
    String table = "_table_";
    String kvMapping = "_kv_mapping_";
    String cloums = "_columns_";
}
