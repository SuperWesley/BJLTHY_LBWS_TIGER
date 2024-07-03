package com.bjlthy.lbss.zero.info.service;


import com.bjlthy.lbss.zero.info.domain.LbssZeroinfo;

import java.util.List;

/**
 * 调零信息Service接口
 * 
 * @author zhangning
 * @date 2022-05-03
 */
public interface ILbssZeroinfoService 
{
    /**
     * 查询调零信息
     * 
     * @param id 调零信息ID
     * @return 调零信息
     */
    public LbssZeroinfo selectLbssZeroinfoById(Integer id);

    /**
     * 查询调零信息列表
     * 
     * @param lbssZeroinfo 调零信息
     * @return 调零信息集合
     */
    public List<LbssZeroinfo> selectLbssZeroinfoList(LbssZeroinfo lbssZeroinfo);

    /**
     * 新增调零信息
     * 
     * @param lbssZeroinfo 调零信息
     * @return 结果
     */
    public int insertLbssZeroinfo(LbssZeroinfo lbssZeroinfo);

}
