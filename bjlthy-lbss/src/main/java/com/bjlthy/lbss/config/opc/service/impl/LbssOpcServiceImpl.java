package com.bjlthy.lbss.config.opc.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;
import com.bjlthy.lbss.config.opc.mapper.LbssOpcMapper;
import com.bjlthy.lbss.config.opc.service.ILbssOpcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * opcua 配置Service业务层处理
 * 
 * @author zhangning
 * @date 2022-05-10
 */
@Service
public class LbssOpcServiceImpl implements ILbssOpcService
{
    @Autowired
    private LbssOpcMapper lbssOpcMapper;

    /**
     * 查询opcua 配置
     * 
     * @param id opcua 配置ID
     * @return opcua 配置
     */
    @Override
    public LbssOpc selectLbssOpcById(Integer id)
    {
        return lbssOpcMapper.selectLbssOpcById(id);
    }

    /**
     * 查询opcua 配置列表
     * 
     * @param lbssOpc opcua 配置
     * @return opcua 配置
     */
    @Override
    public List<LbssOpc> selectLbssOpcList(LbssOpc lbssOpc)
    {
        return lbssOpcMapper.selectLbssOpcList(lbssOpc);
    }

    /**
     * 新增opcua 配置
     * 
     * @param lbssOpc opcua 配置
     * @return 结果
     */
    @Override
    public int insertLbssOpc(LbssOpc lbssOpc)
    {
        return lbssOpcMapper.insertLbssOpc(lbssOpc);
    }

    /**
     * 修改opcua 配置
     * 
     * @param lbssOpc opcua 配置
     * @return 结果
     */
    @Override
    public int updateLbssOpc(LbssOpc lbssOpc)
    {
        return lbssOpcMapper.updateLbssOpc(lbssOpc);
    }

    /**
     * 删除opcua 配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssOpcByIds(String ids)
    {
        return lbssOpcMapper.deleteLbssOpcByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除opcua 配置信息
     * 
     * @param id opcua 配置ID
     * @return 结果
     */
    @Override
    public int deleteLbssOpcById(Integer id)
    {
        return lbssOpcMapper.deleteLbssOpcById(id);
    }
}
