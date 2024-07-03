package com.bjlthy.lbss.data.position.service;


import com.bjlthy.lbss.data.position.domain.LbssPosition;

import java.util.List;

/**
 * 综采编码器数据Service接口
 * 
 * @author zhangning
 * @date 2023-12-15
 */
public interface ILbssPositionService 
{
    /**
     * 查询综采编码器数据
     * 
     * @param id 综采编码器数据ID
     * @return 综采编码器数据
     */
    public LbssPosition selectLbssPositionById(Integer id);

    /**
     * 查询综采编码器数据列表
     * 
     * @param lbssPosition 综采编码器数据
     * @return 综采编码器数据集合
     */
    public List<LbssPosition> selectLbssPositionList(LbssPosition lbssPosition);

    /**
     * 新增综采编码器数据
     * 
     * @param lbssPosition 综采编码器数据
     * @return 结果
     */
    public int insertLbssPosition(LbssPosition lbssPosition);

    /**
     * 修改综采编码器数据
     * 
     * @param lbssPosition 综采编码器数据
     * @return 结果
     */
    public int updateLbssPosition(LbssPosition lbssPosition);

    /**
     * 批量删除综采编码器数据
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssPositionByIds(String ids);

    /**
     * 删除综采编码器数据信息
     * 
     * @param id 综采编码器数据ID
     * @return 结果
     */
    public int deleteLbssPositionById(Integer id);
}
