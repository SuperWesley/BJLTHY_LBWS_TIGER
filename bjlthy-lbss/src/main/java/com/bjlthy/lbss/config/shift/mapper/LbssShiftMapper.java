package com.bjlthy.lbss.config.shift.mapper;


import com.bjlthy.lbss.config.shift.domain.LbssShift;

import java.util.List;


/**
 * 班次Mapper接口
 * 
 * @author zn
 * @date 2020-11-10
 */
public interface LbssShiftMapper 
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
     * 删除班次
     * 
     * @param id 班次ID
     * @return 结果
     */
    public int deleteLbssShiftById(Integer id);

    /**
     * 批量删除班次
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssShiftByIds(String[] ids);
    
    /**
     * 校验班次名称
     * 
     * @param name 班次名称
     * @return 结果
     */
    public LbssShift checkShiftNameUnique(String name);
}
