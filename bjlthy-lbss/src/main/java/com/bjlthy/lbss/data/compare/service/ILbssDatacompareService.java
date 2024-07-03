package com.bjlthy.lbss.data.compare.service;


import com.bjlthy.lbss.data.compare.domain.LbssDatacompare;

import java.util.List;

/**
 * 数据对比Service接口
 * 
 * @author zhangning
 * @date 2022-05-03
 */
public interface ILbssDatacompareService 
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
     * 批量删除数据对比
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssDatacompareByIds(String ids);

    /**
     * 删除数据对比信息
     * 
     * @param id 数据对比ID
     * @return 结果
     */
    public int deleteLbssDatacompareById(Integer id);
    
    /**
     * 
     * @author 张宁
     * @Description: 和矿方产量数据对比并比较
     * @return void    返回类型
     * @date 2022年6月1日 下午3:27:24
     * @throws
     */
	public void dataCompareAndSave();
}
