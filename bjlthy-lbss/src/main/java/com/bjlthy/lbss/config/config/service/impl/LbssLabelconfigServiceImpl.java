package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.common.utils.DateUtils;
import com.bjlthy.lbss.config.config.domain.LbssLabelconfig;
import com.bjlthy.lbss.config.config.mapper.LbssLabelconfigMapper;
import com.bjlthy.lbss.config.config.service.ILbssLabelconfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签配置Service业务层处理
 * 
 * @author zhangning
 * @date 2024-01-22
 */
@Service
public class LbssLabelconfigServiceImpl implements ILbssLabelconfigService
{
    @Autowired
    private LbssLabelconfigMapper lbssLabelconfigMapper;

    /**
     * 查询标签配置
     * 
     * @param id 标签配置ID
     * @return 标签配置
     */
    @Override
    public LbssLabelconfig selectLbssLabelconfigById(Integer id)
    {
        return lbssLabelconfigMapper.selectLbssLabelconfigById(id);
    }

    /**
     * 查询标签配置列表
     * 
     * @param lbssLabelconfig 标签配置
     * @return 标签配置
     */
    @Override
    public List<LbssLabelconfig> selectLbssLabelconfigList(LbssLabelconfig lbssLabelconfig)
    {
        return lbssLabelconfigMapper.selectLbssLabelconfigList(lbssLabelconfig);
    }

    /**
     * 新增标签配置
     * 
     * @param lbssLabelconfig 标签配置
     * @return 结果
     */
    @Override
    public int insertLbssLabelconfig(LbssLabelconfig lbssLabelconfig)
    {
        return lbssLabelconfigMapper.insertLbssLabelconfig(lbssLabelconfig);
    }

    /**
     * 修改标签配置
     * 
     * @param lbssLabelconfig 标签配置
     * @return 结果
     */
    @Override
    public int updateLbssLabelconfig(LbssLabelconfig lbssLabelconfig)
    {
        lbssLabelconfig.setUpdateTime(DateUtils.getNowDate());
        return lbssLabelconfigMapper.updateLbssLabelconfig(lbssLabelconfig);
    }

    /**
     * 删除标签配置对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssLabelconfigByIds(String ids)
    {
        return lbssLabelconfigMapper.deleteLbssLabelconfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除标签配置信息
     * 
     * @param id 标签配置ID
     * @return 结果
     */
    @Override
    public int deleteLbssLabelconfigById(Integer id)
    {
        return lbssLabelconfigMapper.deleteLbssLabelconfigById(id);
    }
}
