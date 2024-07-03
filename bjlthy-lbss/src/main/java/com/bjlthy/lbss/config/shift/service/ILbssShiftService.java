package com.bjlthy.lbss.config.shift.service;


import com.bjlthy.lbss.config.shift.domain.LbssShift;

import java.util.List;

/**
 * 班次Service接口
 * 
 * @author zn
 * @date 2020-11-10
 */
public interface ILbssShiftService 
{

	/**
     * 查询班次
     * 
     * @param id 班次ID
     * @return 班次
     */
    public LbssShift selectLbssShiftById(Integer id);

    /**
     * 查询班次列表
     * 
     * @param coalShift 班次
     * @return 班次集合
     */
    public List<LbssShift> selectLbssShiftList(LbssShift coalShift);

    /**
     * 新增班次
     * 
     * @param coalShift 班次
     * @return 结果
     */
    public int insertLbssShift(LbssShift coalShift);

    /**
     * 修改班次
     * 
     * @param coalShift 班次
     * @return 结果
     */
    public int updateLbssShift(LbssShift coalShift);

    /**
     * 批量删除班次
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssShiftByIds(String ids);

    /**
     * 校验班次名称
     * 
     * @param coalShift 班次信息
     * @return 结果
     */
    public String checkShiftNameUnique(LbssShift coalShift);
}
