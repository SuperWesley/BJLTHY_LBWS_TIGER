package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssIpconfig;

import java.util.List;

/**
 * IP配置Mapper接口
 * 
 * @author zhangning
 * @date 2022-08-10
 */
public interface LbssIpconfigMapper 
{
    /**
     * 查询IP配置
     * 
     * @param id IP配置ID
     * @return IP配置
     */
    public LbssIpconfig selectLbssIpconfigById(Integer id);

    /**
     * 查询IP配置列表
     * 
     * @param lbssIpconfig IP配置
     * @return IP配置集合
     */
    public List<LbssIpconfig> selectLbssIpconfigList(LbssIpconfig lbssIpconfig);

    /**
     * 新增IP配置
     * 
     * @param lbssIpconfig IP配置
     * @return 结果
     */
    public int insertLbssIpconfig(LbssIpconfig lbssIpconfig);

    /**
     * 修改IP配置
     * 
     * @param lbssIpconfig IP配置
     * @return 结果
     */
    public int updateLbssIpconfig(LbssIpconfig lbssIpconfig);

    /**
     * 删除IP配置
     * 
     * @param id IP配置ID
     * @return 结果
     */
    public int deleteLbssIpconfigById(Integer id);

    /**
     * 批量删除IP配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssIpconfigByIds(String[] ids);
}
