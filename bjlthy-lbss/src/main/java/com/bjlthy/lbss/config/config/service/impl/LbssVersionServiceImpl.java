package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.common.utils.DateUtils;
import com.bjlthy.lbss.config.config.domain.LbssVersion;
import com.bjlthy.lbss.config.config.mapper.LbssVersionMapper;
import com.bjlthy.lbss.config.config.service.ILbssVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目版本号Service业务层处理
 * 
 * @author zhangning
 * @date 2023-11-27
 */
@Service
public class LbssVersionServiceImpl implements ILbssVersionService
{
    @Autowired
    private LbssVersionMapper lbssVersionMapper;

    /**
     * 查询项目版本号
     *
     * @param id 项目版本号ID
     * @return 项目版本号
     */
    @Override
    public LbssVersion selectLbssVersionById(Integer id)
    {
        return lbssVersionMapper.selectLbssVersionById(id);
    }

    /**
     * 查询项目版本号列表
     *
     * @param lbssVersion 项目版本号
     * @return 项目版本号
     */
    @Override
    public List<LbssVersion> selectLbssVersionList(LbssVersion lbssVersion)
    {
        return lbssVersionMapper.selectLbssVersionList(lbssVersion);
    }

    /**
     * 新增项目版本号
     *
     * @param lbssVersion 项目版本号
     * @return 结果
     */
    @Override
    public int insertLbssVersion(LbssVersion lbssVersion)
    {
        return lbssVersionMapper.insertLbssVersion(lbssVersion);
    }

    /**
     * 修改项目版本号
     *
     * @param lbssVersion 项目版本号
     * @return 结果
     */
    @Override
    public int updateLbssVersion(LbssVersion lbssVersion)
    {
        lbssVersion.setUpdateTime(DateUtils.getNowDate());
        return lbssVersionMapper.updateLbssVersion(lbssVersion);
    }

    /**
     * 删除项目版本号对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssVersionByIds(String ids)
    {
        return lbssVersionMapper.deleteLbssVersionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除项目版本号信息
     *
     * @param id 项目版本号ID
     * @return 结果
     */
    @Override
    public int deleteLbssVersionById(Integer id)
    {
        return lbssVersionMapper.deleteLbssVersionById(id);
    }
}
