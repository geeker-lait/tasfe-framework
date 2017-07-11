package com.tasfe.framework.crud.api;

import com.tasfe.framework.crud.api.dto.Pagination;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
public interface PagingOperator {
    /**
     * 分页查询，获取当前分页查询的总记录数
     *
     * @param pager
     * @param example 查询条件参数
     * @return 分页记录列表
     */
    <Entity> List<Entity> pagination(Pagination pager, Entity example);
}
