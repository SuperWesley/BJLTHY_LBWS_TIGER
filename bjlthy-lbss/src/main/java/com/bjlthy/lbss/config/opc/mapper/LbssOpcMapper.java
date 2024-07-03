package com.bjlthy.lbss.config.opc.mapper;


import com.bjlthy.lbss.config.opc.domain.LbssOpc;

import java.util.List;

/**
 * opcua 配置Mapper接口
 * 
 * @author zhangning
 * @date 2022-05-10
 */
public interface LbssOpcMapper
{
    /**
     * 查询opcua 配置
     * 
     * @param id opcua 配置ID
     * @return opcua 配置
     */
    public LbssOpc selectLbssOpcById(Integer id);

    /**
     * 查询opcua 配置列表
     * 
     * @param lbssOpc opcua 配置
     * @return opcua 配置集合
     */
    public List<LbssOpc> selectLbssOpcList(LbssOpc lbssOpc);

    /**
     * 新增opcua 配置
     * 
     * @param lbssOpc opcua 配置
     * @return 结果
     */
    public int insertLbssOpc(LbssOpc lbssOpc);

    /**
     * 修改opcua 配置
     * 
     * @param lbssOpc opcua 配置
     * @return 结果
     */
    public int updateLbssOpc(LbssOpc lbssOpc);

    /**
     * 删除opcua 配置
     * 
     * @param id opcua 配置ID
     * @return 结果
     */
    public int deleteLbssOpcById(Integer id);

    /**
     * 批量删除opcua 配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssOpcByIds(String[] ids);
}
