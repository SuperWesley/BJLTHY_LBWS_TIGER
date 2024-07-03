package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssLabelconfig;

import java.util.List;

/**
 * 标签配置Mapper接口
 * 
 * @author zhangning
 * @date 2024-01-22
 */
public interface LbssLabelconfigMapper 
{
    /**
     * 查询标签配置
     * 
     * @param id 标签配置ID
     * @return 标签配置
     */
    public LbssLabelconfig selectLbssLabelconfigById(Integer id);

    /**
     * 查询标签配置列表
     * 
     * @param lbssLabelconfig 标签配置
     * @return 标签配置集合
     */
    public List<LbssLabelconfig> selectLbssLabelconfigList(LbssLabelconfig lbssLabelconfig);

    /**
     * 新增标签配置
     * 
     * @param lbssLabelconfig 标签配置
     * @return 结果
     */
    public int insertLbssLabelconfig(LbssLabelconfig lbssLabelconfig);

    /**
     * 修改标签配置
     * 
     * @param lbssLabelconfig 标签配置
     * @return 结果
     */
    public int updateLbssLabelconfig(LbssLabelconfig lbssLabelconfig);

    /**
     * 删除标签配置
     * 
     * @param id 标签配置ID
     * @return 结果
     */
    public int deleteLbssLabelconfigById(Integer id);

    /**
     * 批量删除标签配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssLabelconfigByIds(String[] ids);
}
