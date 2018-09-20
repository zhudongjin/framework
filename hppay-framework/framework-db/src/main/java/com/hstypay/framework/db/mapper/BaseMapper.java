package com.hstypay.framework.db.mapper;

import com.hstypay.mybatis.pagination.dto.Search;

import java.util.List;

/**
 * 基础mapper
 *
 * @author Vincent
 * @version 1.0 2017/03/07 22:12
 */
public interface BaseMapper<Entity, PrimaryKey> {

    /**
     * 分页查询
     *
     * @param search 查询对象
     * @return 查询结果
     */
    List<Entity> queryPageList(Search search);

    /**
     * 根据id查询
     *
     * @param paramPrimaryKey 主键
     * @return 结果详情
     */
    Entity queryById(PrimaryKey paramPrimaryKey);

    /**
     * 根据id查询,锁记录
     *
     * @param paramPrimaryKey 主键
     * @return 结果详情
     */
    Entity lockById(PrimaryKey paramPrimaryKey);

    /**
     * 插入数据
     *
     * @param paramEntity 详情
     * @return 影响行数
     */
    int insert(Entity paramEntity);

    /**
     * 根据主键更新
     *
     * @param paramEntity 详情
     * @return 影响行数
     */
    int updateById(Entity paramEntity);
}
