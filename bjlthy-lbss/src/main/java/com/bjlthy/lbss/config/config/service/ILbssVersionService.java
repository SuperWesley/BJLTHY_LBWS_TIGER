package com.bjlthy.lbss.config.config.service;


import com.bjlthy.lbss.config.config.domain.LbssVersion;

import java.util.List;

/**
 * 项目版本号Service接口
 * 
 * @author zhangning
 * @date 2023-11-27
 */
public interface ILbssVersionService 
{

    /**
     * 查询项目版本号
     *
     * @param id 项目版本号ID
     * @return 项目版本号
     */
    public LbssVersion selectLbssVersionById(Integer id);

    /**
     * 查询项目版本号列表
     *
     * @param lbssVersion 项目版本号
     * @return 项目版本号集合
     */
    public List<LbssVersion> selectLbssVersionList(LbssVersion lbssVersion);

    /**
     * 新增项目版本号
     *
     * @param lbssVersion 项目版本号
     * @return 结果
     */
    public int insertLbssVersion(LbssVersion lbssVersion);

    /**
     * 修改项目版本号
     *
     * @param lbssVersion 项目版本号
     * @return 结果
     */
    public int updateLbssVersion(LbssVersion lbssVersion);

    /**
     * 批量删除项目版本号
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssVersionByIds(String ids);

    /**
     * 删除项目版本号信息
     *
     * @param id 项目版本号ID
     * @return 结果
     */
    public int deleteLbssVersionById(Integer id);

}
