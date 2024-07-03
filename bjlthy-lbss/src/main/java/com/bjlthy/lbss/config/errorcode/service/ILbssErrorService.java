package com.bjlthy.lbss.config.errorcode.service;


import com.bjlthy.lbss.config.errorcode.domain.LbssError;

import java.util.List;


/**
 * 上位机界面异常表Service接口
 * 
 * @author 张宁
 * @date 2021-03-01
 */
public interface ILbssErrorService 
{
    /**
     * 查询上位机界面异常表
     * 
     * @param id 上位机界面异常表ID
     * @return 上位机界面异常表
     */
    public LbssError selectCoalErrorById(Long id);

    /**
     * 查询上位机界面异常表列表
     * 
     * @param coalError 上位机界面异常表
     * @return 上位机界面异常表集合
     */
    public List<LbssError> selectCoalErrorList(LbssError coalError);

    /**
     * 新增上位机界面异常表
     * 
     * @param coalError 上位机界面异常表
     * @return 结果
     */
    public int insertCoalError(LbssError coalError);

    /**
     * 修改上位机界面异常表
     * 
     * @param coalError 上位机界面异常表
     * @return 结果
     */
    public int updateCoalError(LbssError coalError);

    /**
     * 批量删除上位机界面异常表
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteCoalErrorByIds(String ids);

    /**
     * 删除上位机界面异常表信息
     * 
     * @param id 上位机界面异常表ID
     * @return 结果
     */
    public int deleteCoalErrorById(Long id);
}
