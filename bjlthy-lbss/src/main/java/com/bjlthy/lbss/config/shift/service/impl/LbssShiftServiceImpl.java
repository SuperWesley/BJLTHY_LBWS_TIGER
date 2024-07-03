package com.bjlthy.lbss.config.shift.service.impl;

import com.bjlthy.common.constant.UserConstants;
import com.bjlthy.common.core.text.Convert;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.config.shift.service.ILbssShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班次Service业务层处理
 * 
 * @author zn
 * @date 2020-11-10
 */
@Service
public class LbssShiftServiceImpl implements ILbssShiftService
{
    @Autowired
    private LbssShiftMapper coalShiftMapper;

    /**
     * 查询班次
     * 
     * @param id 班次ID
     * @return 班次
     */
    @Override
    public LbssShift selectLbssShiftById(Integer id)
    {
        return coalShiftMapper.selectLbssShiftById(id);
    }

    /**
     * 查询班次列表
     * 
     * @param coalShift 班次
     * @return 班次
     */
    @Override
    public List<LbssShift> selectLbssShiftList(LbssShift coalShift)
    {
        return coalShiftMapper.selectLbssShiftList(coalShift);
    }

    /**
     * 新增班次
     * 
     * @param coalShift 班次
     * @return 结果
     */
    @Override
    public int insertLbssShift(LbssShift coalShift)
    {
        return coalShiftMapper.insertLbssShift(coalShift);
    }

    /**
     * 修改班次
     * 
     * @param coalShift 班次
     * @return 结果
     */
    @Override
    public int updateLbssShift(LbssShift coalShift)
    {
        return coalShiftMapper.updateLbssShift(coalShift);
    }

    /**
     * 删除班次对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssShiftByIds(String ids)
    {
    	String[] idArr= Convert.toStrArray(ids);
    	int result=0;
    	for (String id : idArr) {
    		result =coalShiftMapper.deleteLbssShiftById(Integer.valueOf(id));
		}
    	return result;
    }

    /**
     * 校验岗位名称是否唯一
     * 
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public String checkShiftNameUnique(LbssShift coalShift)
    {
    	//根据名称查询信息
    	LbssShift shift = coalShiftMapper.checkShiftNameUnique(coalShift.getName());
    	//判断信息是否存在
        if (StringUtils.isNotNull(shift))
        {
            return UserConstants.SHIFT_NAME_NOT_UNIQUE;
        }
        return UserConstants.SHIFT_NAME_UNIQUE;
    }
}
