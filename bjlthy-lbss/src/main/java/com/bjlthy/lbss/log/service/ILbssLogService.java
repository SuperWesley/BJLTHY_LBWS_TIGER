package com.bjlthy.lbss.log.service;


import com.bjlthy.lbss.log.domain.LbssLog;

import java.util.List;

/**
 * 日志信息Service接口
 * 
 * @author 张宁
 * @date 2021-08-09
 */
public interface ILbssLogService 
{
    /**
     * 查询日志信息
     * 
     * @param id 日志信息ID
     * @return 日志信息
     */
    public LbssLog selectLbssLogById(Integer id);

    /**
     * 查询日志信息列表
     * 
     * @param lbssLog 日志信息
     * @return 日志信息集合
     */
    public List<LbssLog> selectLbssLogList(LbssLog lbssLog);


    /**
     * 批量删除日志信息
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssLogByIds(String ids);

}
