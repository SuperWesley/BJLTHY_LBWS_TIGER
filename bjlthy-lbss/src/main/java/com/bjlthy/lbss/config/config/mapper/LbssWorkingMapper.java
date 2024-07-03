package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssWorking;

import java.util.List;



/**
 * 其他配置Mapper接口
 * 
 * @author 张宁
 * @date 2021-03-27
 */
public interface LbssWorkingMapper 
{
	/**
     * 查询其他配置
     * 
     * @param id 其他配置ID
     * @return 其他配置
     */
    public LbssWorking selectLbssWorkingById(Integer id);

    /**
     * 查询其他配置列表
     * 
     * @param lbssWorking 其他配置
     * @return 其他配置集合
     */
    public List<LbssWorking> selectLbssWorkingList(LbssWorking lbssWorking);

    /**
     * 查询其他配置列表-不含working_area ==ALL
     *
     * @param lbssWorking 其他配置
     * @return 其他配置集合
     */
    public List<LbssWorking> selectLbssWorkingNotALLList(LbssWorking lbssWorking);

    /**
     * 新增其他配置
     * 
     * @param lbssWorking 其他配置
     * @return 结果
     */
    public int insertLbssWorking(LbssWorking lbssWorking);

    /**
     * 修改其他配置
     * 
     * @param lbssWorking 其他配置
     * @return 结果
     */
    public int updateLbssWorking(LbssWorking lbssWorking);

    /**
     * 删除其他配置
     * 
     * @param id 其他配置ID
     * @return 结果
     */
    public int deleteLbssWorkingById(Integer id);

    
}
