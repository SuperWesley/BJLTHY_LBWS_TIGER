package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.config.config.domain.LbssIpconfig;
import com.bjlthy.lbss.config.config.mapper.LbssIpconfigMapper;
import com.bjlthy.lbss.config.config.service.ILbssIpconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * IP配置Service业务层处理
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Service
public class LbssIpconfigServiceImpl implements ILbssIpconfigService
{
    @Autowired
    private LbssIpconfigMapper lbssIpconfigMapper;

    /**
     * 查询IP配置
     * 
     * @param id IP配置ID
     * @return IP配置
     */
    @Override
    public LbssIpconfig selectLbssIpconfigById(Integer id)
    {
        return lbssIpconfigMapper.selectLbssIpconfigById(id);
    }

    /**
     * 查询IP配置列表
     * 
     * @param lbssIpconfig IP配置
     * @return IP配置
     */
    @Override
    public List<LbssIpconfig> selectLbssIpconfigList(LbssIpconfig lbssIpconfig)
    {
        return lbssIpconfigMapper.selectLbssIpconfigList(lbssIpconfig);
    }

    /**
     * 新增IP配置
     * 
     * @param lbssIpconfig IP配置
     * @return 结果
     */
    @Override
    public int insertLbssIpconfig(LbssIpconfig lbssIpconfig)
    {
        return lbssIpconfigMapper.insertLbssIpconfig(lbssIpconfig);
    }

    /**
     * 修改IP配置
     * 
     * @param lbssIpconfig IP配置
     * @return 结果
     */
    @Override
    public int updateLbssIpconfig(LbssIpconfig lbssIpconfig)
    {
        return lbssIpconfigMapper.updateLbssIpconfig(lbssIpconfig);
    }

    /**
     * 删除IP配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssIpconfigByIds(String ids)
    {
        return lbssIpconfigMapper.deleteLbssIpconfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除IP配置信息
     * 
     * @param id IP配置ID
     * @return 结果
     */
    @Override
    public int deleteLbssIpconfigById(Integer id)
    {
        return lbssIpconfigMapper.deleteLbssIpconfigById(id);
    }
}
