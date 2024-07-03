package com.bjlthy.lbss.zero.info.service.impl;

import com.bjlthy.lbss.zero.info.domain.LbssZeroinfo;
import com.bjlthy.lbss.zero.info.mapper.LbssZeroinfoMapper;
import com.bjlthy.lbss.zero.info.service.ILbssZeroinfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 调零信息Service业务层处理
 * 
 * @author zhangning
 * @date 2022-05-03
 */
@Service
public class LbssZeroinfoServiceImpl implements ILbssZeroinfoService
{
    @Autowired
    private LbssZeroinfoMapper lbssZeroinfoMapper;

    /**
     * 查询调零信息
     * 
     * @param id 调零信息ID
     * @return 调零信息
     */
    @Override
    public LbssZeroinfo selectLbssZeroinfoById(Integer id)
    {
        return lbssZeroinfoMapper.selectLbssZeroinfoById(id);
    }

    /**
     * 查询调零信息列表
     * 
     * @param lbssZeroinfo 调零信息
     * @return 调零信息
     */
    @Override
    public List<LbssZeroinfo> selectLbssZeroinfoList(LbssZeroinfo lbssZeroinfo)
    {
        return lbssZeroinfoMapper.selectLbssZeroinfoList(lbssZeroinfo);
    }

    /**
     * 新增调零信息
     * 
     * @param lbssZeroinfo 调零信息
     * @return 结果
     */
    @Override
    public int insertLbssZeroinfo(LbssZeroinfo lbssZeroinfo)
    {
        return lbssZeroinfoMapper.insertLbssZeroinfo(lbssZeroinfo);
    }

}
