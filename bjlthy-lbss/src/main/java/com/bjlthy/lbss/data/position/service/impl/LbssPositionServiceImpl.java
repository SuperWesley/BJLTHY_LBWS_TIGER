package com.bjlthy.lbss.data.position.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.data.position.domain.LbssPosition;
import com.bjlthy.lbss.data.position.mapper.LbssPositionMapper;
import com.bjlthy.lbss.data.position.service.ILbssPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 综采编码器数据Service业务层处理
 * 
 * @author zhangning
 * @date 2023-12-15
 */
@Service
public class LbssPositionServiceImpl implements ILbssPositionService
{
    @Autowired
    private LbssPositionMapper lbssPositionMapper;

    /**
     * 查询综采编码器数据
     * 
     * @param id 综采编码器数据ID
     * @return 综采编码器数据
     */
    @Override
    public LbssPosition selectLbssPositionById(Integer id)
    {
        return lbssPositionMapper.selectLbssPositionById(id);
    }

    /**
     * 查询综采编码器数据列表
     * 
     * @param lbssPosition 综采编码器数据
     * @return 综采编码器数据
     */
    @Override
    public List<LbssPosition> selectLbssPositionList(LbssPosition lbssPosition)
    {
        return lbssPositionMapper.selectLbssPositionList(lbssPosition);
    }

    /**
     * 新增综采编码器数据
     * 
     * @param lbssPosition 综采编码器数据
     * @return 结果
     */
    @Override
    public int insertLbssPosition(LbssPosition lbssPosition)
    {
        return lbssPositionMapper.insertLbssPosition(lbssPosition);
    }

    /**
     * 修改综采编码器数据
     * 
     * @param lbssPosition 综采编码器数据
     * @return 结果
     */
    @Override
    public int updateLbssPosition(LbssPosition lbssPosition)
    {
        return lbssPositionMapper.updateLbssPosition(lbssPosition);
    }

    /**
     * 删除综采编码器数据对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssPositionByIds(String ids)
    {
        return lbssPositionMapper.deleteLbssPositionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除综采编码器数据信息
     * 
     * @param id 综采编码器数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssPositionById(Integer id)
    {
        return lbssPositionMapper.deleteLbssPositionById(id);
    }
}
