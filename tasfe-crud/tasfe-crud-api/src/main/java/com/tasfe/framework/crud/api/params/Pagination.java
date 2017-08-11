package com.tasfe.framework.crud.api.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * Created by Lait on 2017/7/7.
 */
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pagination<T> {
    /**
     * 当前页数
     */
    private Integer curPage;
    /**
     * 每页展示条数
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer countPage;
    /**
     * 总数据条数
     */
    private Long totalSize;
    /**
     * 分页查询数据结果
     */
    private List<T> result;
}
