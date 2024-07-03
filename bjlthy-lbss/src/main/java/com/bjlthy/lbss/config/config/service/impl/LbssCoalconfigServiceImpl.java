package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.lbss.config.config.domain.LbssCoalconfig;
import com.bjlthy.lbss.config.config.mapper.LbssCoalconfigMapper;
import com.bjlthy.lbss.config.config.service.ILbssCoalconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 煤量数据对比配置Service业务层处理
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Service
public class LbssCoalconfigServiceImpl implements ILbssCoalconfigService
{
    @Autowired
    private LbssCoalconfigMapper lbssCoalconfigMapper;

    /**
     * 查询煤量数据对比配置
     * 
     * @param id 煤量数据对比配置ID
     * @return 煤量数据对比配置
     */
    @Override
    public LbssCoalconfig selectLbssCoalconfigById(Integer id)
    {
        return lbssCoalconfigMapper.selectLbssCoalconfigById(id);
    }

    /**
     * 查询煤量数据对比配置列表
     * 
     * @param lbssCoalconfig 煤量数据对比配置
     * @return 煤量数据对比配置
     */
    @Override
    public List<LbssCoalconfig> selectLbssCoalconfigList(LbssCoalconfig lbssCoalconfig)
    {
        return lbssCoalconfigMapper.selectLbssCoalconfigList(lbssCoalconfig);
    }


    /**
     * 修改煤量数据对比配置
     * 
     * @param lbssCoalconfig 煤量数据对比配置
     * @return 结果
     */
    @Override
    public int updateLbssCoalconfig(LbssCoalconfig lbssCoalconfig)
    {
        return lbssCoalconfigMapper.updateLbssCoalconfig(lbssCoalconfig);
    }

}
