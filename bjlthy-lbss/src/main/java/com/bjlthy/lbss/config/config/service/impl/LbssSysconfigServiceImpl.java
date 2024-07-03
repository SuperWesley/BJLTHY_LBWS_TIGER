package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.common.core.text.Convert;
import com.bjlthy.lbss.config.config.domain.LbssSysconfig;
import com.bjlthy.lbss.config.config.mapper.LbssSysconfigMapper;
import com.bjlthy.lbss.config.config.service.ILbssSysconfigService;
import com.bjlthy.lbss.tool.DictionariesHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 开关控制Service业务层处理
 * 
 * @author zhangning
 * @date 2022-08-10
 */
@Service
public class LbssSysconfigServiceImpl implements ILbssSysconfigService
{
    @Autowired
    private LbssSysconfigMapper lbssSysconfigMapper;

    /**
     * 查询开关控制
     * 
     * @param id 开关控制ID
     * @return 开关控制
     */
    @Override
    public LbssSysconfig selectLbssSysconfigById(Integer id)
    {
        return lbssSysconfigMapper.selectLbssSysconfigById(id);
    }

    /**
     * 查询开关控制列表
     * 
     * @param lbssSysconfig 开关控制
     * @return 开关控制
     */
    @Override
    public List<LbssSysconfig> selectLbssSysconfigList(LbssSysconfig lbssSysconfig)
    {
        return lbssSysconfigMapper.selectLbssSysconfigList(lbssSysconfig);
    }

    /**
     * 新增开关控制
     * 
     * @param lbssSysconfig 开关控制
     * @return 结果
     */
    @Override
    public int insertLbssSysconfig(LbssSysconfig lbssSysconfig)
    {
        return lbssSysconfigMapper.insertLbssSysconfig(lbssSysconfig);
    }

    /**
     * 修改开关控制
     * 
     * @param lbssSysconfig 开关控制
     * @return 结果
     */
    @Override
    public int updateLbssSysconfig(LbssSysconfig lbssSysconfig)
    {
    	int num  = lbssSysconfigMapper.updateLbssSysconfig(lbssSysconfig);
    	if(num > 0){
    		if("0".equals(lbssSysconfig.getType())){
    			DictionariesHelper.setDicIntegerValueByCode(lbssSysconfig.getBelt_name()+"-"+lbssSysconfig.getCode(), Integer.valueOf(lbssSysconfig.getValue()));
    		}else if("1".equals(lbssSysconfig.getType())){
    			DictionariesHelper.setDicStringValueByCode(lbssSysconfig.getBelt_name()+"-"+lbssSysconfig.getCode(), lbssSysconfig.getValue());
    		}else if("2".equals(lbssSysconfig.getType())){
    			DictionariesHelper.setDicDoubleValueByCode(lbssSysconfig.getBelt_name()+"-"+lbssSysconfig.getCode(), Double.valueOf(lbssSysconfig.getValue()));
    		}
    	}
        return num;
    }

    /**
     * 删除开关控制对象
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteLbssSysconfigByIds(String ids)
    {
        return lbssSysconfigMapper.deleteLbssSysconfigByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除开关控制信息
     * 
     * @param id 开关控制ID
     * @return 结果
     */
    @Override
    public int deleteLbssSysconfigById(Integer id)
    {
        return lbssSysconfigMapper.deleteLbssSysconfigById(id);
    }
}
