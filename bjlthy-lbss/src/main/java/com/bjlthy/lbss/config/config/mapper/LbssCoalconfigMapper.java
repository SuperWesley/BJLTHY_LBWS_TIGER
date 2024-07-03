package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssCoalconfig;

import java.util.List;

/**
 * 煤量数据对比配置Mapper接口
 * 
 * @author zhangning
 * @date 2022-08-10
 */
public interface LbssCoalconfigMapper 
{
    /**
     * 查询煤量数据对比配置
     * 
     * @param id 煤量数据对比配置ID
     * @return 煤量数据对比配置
     */
    public LbssCoalconfig selectLbssCoalconfigById(Integer id);

    /**
     * 查询煤量数据对比配置列表
     * 
     * @param lbssCoalconfig 煤量数据对比配置
     * @return 煤量数据对比配置集合
     */
    public List<LbssCoalconfig> selectLbssCoalconfigList(LbssCoalconfig lbssCoalconfig);


    /**
     * 修改煤量数据对比配置
     * 
     * @param lbssCoalconfig 煤量数据对比配置
     * @return 结果
     */
    public int updateLbssCoalconfig(LbssCoalconfig lbssCoalconfig);

}
