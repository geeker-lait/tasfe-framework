package com.tasfe.framework.crud.api.dto;

import com.tasfe.framework.crud.api.CrudMethod;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by Lait on 2017/7/10.
 */
@Data
public class CrudParam {

    CrudMethod crudMethod;

    private Object object;

    private List<?> objectList;

    private Map<String,Object> params;
}
