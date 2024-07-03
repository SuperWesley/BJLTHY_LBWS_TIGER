package com.bjlthy.lbss.config.errorcode.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.config.errorcode.domain.LbssError;
import com.bjlthy.lbss.config.errorcode.mapper.LbssErrorMapper;
import com.bjlthy.lbss.config.errorcode.service.ILbssErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 上位机界面异常表Service业务层处理
 * 
 * @author 张宁
 * @date 2021-03-01
 */
@Service
public class LbssErrorServiceImpl implements ILbssErrorService
{
    @Autowired
    private LbssErrorMapper coalErrorMapper;

    /**
     * 查询上位机界面异常表
     * 
     * @param id 上位机界面异常表ID
     * @return 上位机界面异常表
     */
    @Override
    public LbssError selectCoalErrorById(Long id)
    {
        return coalErrorMapper.selectCoalErrorById(id);
    }

    /**
     * 查询上位机界面异常表列表
     * 
     * @param coalError 上位机界面异常表
     * @return 上位机界面异常表
     */
    @Override
    public List<LbssError> selectCoalErrorList(LbssError coalError)
    {
        return coalErrorMapper.selectCoalErrorList(coalError);
    }

    /**
     * 新增上位机界面异常表
     * 
     * @param coalError 上位机界面异常表
     * @return 结果
     */
    @Override
    public int insertCoalError(LbssError coalError)
    {
        return coalErrorMapper.insertCoalError(coalError);
    }

    /**
     * 修改上位机界面异常表
     * 
     * @param coalError 上位机界面异常表
     * @return 结果
     */
    @Override
    public int updateCoalError(LbssError coalError)
    {
        return coalErrorMapper.updateCoalError(coalError);
    }

    /**
     * 删除上位机界面异常表对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteCoalErrorByIds(String ids)
    {
        return coalErrorMapper.deleteCoalErrorByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除上位机界面异常表信息
     * 
     * @param id 上位机界面异常表ID
     * @return 结果
     */
    @Override
    public int deleteCoalErrorById(Long id)
    {
        return coalErrorMapper.deleteCoalErrorById(id);
    }
}
