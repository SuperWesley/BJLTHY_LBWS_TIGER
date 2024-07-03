package com.bjlthy.lbss.log.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.log.domain.LbssLog;
import com.bjlthy.lbss.log.mapper.LbssLogMapper;
import com.bjlthy.lbss.log.service.ILbssLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 日志信息Service业务层处理
 * 
 * @author 张宁
 * @date 2021-08-09
 */
@Service
public class LbssLogServiceImpl implements ILbssLogService
{
    @Autowired
    private LbssLogMapper lbssLogMapper;

    /**
     * 查询日志信息
     * 
     * @param id 日志信息ID
     * @return 日志信息
     */
    @Override
    public LbssLog selectLbssLogById(Integer id)
    {
        return lbssLogMapper.selectLbssLogById(id);
    }

    /**
     * 查询日志信息列表
     * 
     * @param lbssLog 日志信息
     * @return 日志信息
     */
    @Override
    public List<LbssLog> selectLbssLogList(LbssLog lbssLog)
    {
        return lbssLogMapper.selectLbssLogList(lbssLog);
    }



    /**
     * 删除日志信息对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssLogByIds(String ids)
    {
        return lbssLogMapper.deleteLbssLogByIds(Convert.toStrArray(ids));
    }
}
