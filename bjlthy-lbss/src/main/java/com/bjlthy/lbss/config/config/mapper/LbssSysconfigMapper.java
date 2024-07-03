package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssSysconfig;

import java.util.List;

/**
 * 开关控制Mapper接口
 * 
 * @author zhangning
 * @date 2022-08-10
 */
public interface LbssSysconfigMapper 
{
    /**
     * 查询开关控制
     * 
     * @param id 开关控制ID
     * @return 开关控制
     */
    public LbssSysconfig selectLbssSysconfigById(Integer id);

    /**
     * 查询开关控制列表
     * 
     * @param lbssSysconfig 开关控制
     * @return 开关控制集合
     */
    public List<LbssSysconfig> selectLbssSysconfigList(LbssSysconfig lbssSysconfig);

    /**
     * 新增开关控制
     * 
     * @param lbssSysconfig 开关控制
     * @return 结果
     */
    public int insertLbssSysconfig(LbssSysconfig lbssSysconfig);

    /**
     * 修改开关控制
     * 
     * @param lbssSysconfig 开关控制
     * @return 结果
     */
    public int updateLbssSysconfig(LbssSysconfig lbssSysconfig);

    /**
     * 删除开关控制
     * 
     * @param id 开关控制ID
     * @return 结果
     */
    public int deleteLbssSysconfigById(Integer id);

    /**
     * 批量删除开关控制
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssSysconfigByIds(String[] ids);
}
