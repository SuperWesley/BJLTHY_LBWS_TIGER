package com.bjlthy.lbss.data.compare.mapper;


import com.bjlthy.lbss.data.compare.domain.LbssDatacompare;

import java.util.List;

/**
 * 数据对比Mapper接口
 * 
 * @author zhangning
 * @date 2022-05-03
 */
public interface LbssDatacompareMapper 
{
    /**
     * 查询数据对比
     * 
     * @param id 数据对比ID
     * @return 数据对比
     */
    public LbssDatacompare selectLbssDatacompareById(Integer id);

    /**
     * 查询数据对比列表
     * 
     * @param lbssDatacompare 数据对比
     * @return 数据对比集合
     */
    public List<LbssDatacompare> selectLbssDatacompareList(LbssDatacompare lbssDatacompare);

    /**
     * 新增数据对比
     * 
     * @param lbssDatacompare 数据对比
     * @return 结果
     */
    public int insertLbssDatacompare(LbssDatacompare lbssDatacompare);

    /**
     * 修改数据对比
     * 
     * @param lbssDatacompare 数据对比
     * @return 结果
     */
    public int updateLbssDatacompare(LbssDatacompare lbssDatacompare);

    /**
     * 删除数据对比
     * 
     * @param id 数据对比ID
     * @return 结果
     */
    public int deleteLbssDatacompareById(Integer id);

    /**
     * 批量删除数据对比
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssDatacompareByIds(String[] ids);
}
