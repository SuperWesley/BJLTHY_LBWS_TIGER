package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssServerIpconfig;

import java.util.List;

/**
 * 服务器ip配置Mapper接口
 * 
 * @author zhangning
 * @date 2023-11-05
 */
public interface LbssServerIpconfigMapper 
{
    /**
     * 查询服务器ip配置
     * 
     * @param id 服务器ip配置ID
     * @return 服务器ip配置
     */
    public LbssServerIpconfig selectLbssServerIpconfigById(Integer id);

    /**
     * 查询服务器ip配置列表
     * 
     * @param lbssServerIpconfig 服务器ip配置
     * @return 服务器ip配置集合
     */
    public List<LbssServerIpconfig> selectLbssServerIpconfigList(LbssServerIpconfig lbssServerIpconfig);

    /**
     * 新增服务器ip配置
     * 
     * @param lbssServerIpconfig 服务器ip配置
     * @return 结果
     */
    public int insertLbssServerIpconfig(LbssServerIpconfig lbssServerIpconfig);

    /**
     * 修改服务器ip配置
     * 
     * @param lbssServerIpconfig 服务器ip配置
     * @return 结果
     */
    public int updateLbssServerIpconfig(LbssServerIpconfig lbssServerIpconfig);

    /**
     * 删除服务器ip配置
     * 
     * @param id 服务器ip配置ID
     * @return 结果
     */
    public int deleteLbssServerIpconfigById(Integer id);

    /**
     * 批量删除服务器ip配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssServerIpconfigByIds(String[] ids);
}
