package com.bjlthy.lbss.config.config.service.impl;

import com.bjlthy.common.constant.UserConstants;
import com.bjlthy.common.core.text.Convert;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.mapper.LbssARMConfigMapper;
import com.bjlthy.lbss.config.config.service.ILbssARMConfigService;
import com.bjlthy.lbss.dataComm.socket.processor.config.NanoConfigProcessor;
import com.bjlthy.lbss.tool.DictionariesHelper;
import com.bjlthy.lbss.tool.ParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 其他配置Service业务层处理
 * 
 * @author 张宁
 * @date 2021-01-03
 */
@Service
public class LbssARMConfigServiceImpl implements ILbssARMConfigService
{
	  @Autowired
	    private LbssARMConfigMapper armConfigMapper;

	    /**
	     * 查询其他配置
	     * 
	     * @param id 其他配置ID
	     * @return 其他配置
	     */
	    @Override
	    public LbssARMConfig selectLbssARMConfigById(Integer id)
	    {
	        return armConfigMapper.selectLbssARMConfigById(id);
	    }

	    /**
	     * 查询其他配置列表
	     * 
	     * @param lbssArmonfig 其他配置
	     * @return 其他配置
	     */
	    @Override
	    public List<LbssARMConfig> selectLbssARMConfigList(LbssARMConfig lbssArmonfig)
	    {
	        return armConfigMapper.selectLbssARMConfigList(lbssArmonfig);
	    }

	    /**
	     * 新增其他配置
	     * 
	     * @param lbssArmonfig 其他配置
	     * @return 结果
	     */
	    @Override
	    public int insertLbssARMConfig(LbssARMConfig lbssArmonfig)
	    {
	        return armConfigMapper.insertLbssARMConfig(lbssArmonfig);
	    }

	    /**
	     * 修改其他配置
	     * 
	     * @param armConfig 其他配置
	     * @return 结果
	     */
	    @Override
	    public int updateLbssARMConfig(LbssARMConfig armConfig)
	    {
	    	int num = armConfigMapper.updateLbssARMConfig(armConfig);
	    	if(num>0){
	    		if("0".equals(armConfig.getType())){
	    			DictionariesHelper.setDicIntegerValueByCode(armConfig.getBelt_name()+"-"+armConfig.getCode(), Integer.valueOf(armConfig.getValue()));
	    		}else if("1".equals(armConfig.getType())){
	    			DictionariesHelper.setDicStringValueByCode(armConfig.getBelt_name()+"-"+armConfig.getCode(), armConfig.getValue());
	    		}else if("2".equals(armConfig.getType())){
	    			DictionariesHelper.setDicDoubleValueByCode(armConfig.getBelt_name()+"-"+armConfig.getCode(), Double.valueOf(armConfig.getValue()));
	    		}
	    	}
	        return num;
	    }

	    /**
	     * 删除其他配置对象
	     * 
	     * @param ids 需要删除的数据ID
	     * @return 结果
	     */
	    @Override
	    public int deleteLbssARMConfigByIds(String ids)
	    {
	        return armConfigMapper.deleteLbssARMConfigByIds(Convert.toStrArray(ids));
	    }

	    /**
	     * 删除其他配置信息
	     * 
	     * @param id 其他配置ID
	     * @return 结果
	     */
	    @Override
	    public int deleteLbssARMConfigById(Integer id)
	    {
	        return armConfigMapper.deleteLbssARMConfigById(id);
	    }
    
    /**
     * 更新下位机配置信息
     * @author 张宁
     * @param LbssARMConfig 智能调速配置ID
     * @return 结果
     */
	@Override
	public void updateConfig(LbssARMConfig LbssARMConfig) {
		String code =LbssARMConfig.getCode();
		String value = LbssARMConfig.getValue();

		//拼接配置信息
		String msg ="$CONFIG "+code+":"+value;
		//计算校验和
		String checkSum = ParseUtils.checkData(msg);
		msg = msg+" " +checkSum;
		//发送信息到下位机
		try {
            if(NanoConfigProcessor.session != null){

                NanoConfigProcessor.session.writeBuffer().write(msg.getBytes());
                NanoConfigProcessor.session.writeBuffer().flush();
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 校验编码是否存在
	 */
	@Override
	public String checkConfigCodeUnique(LbssARMConfig lbssARMConfig) {

        Long id = StringUtils.isNull(lbssARMConfig.getId()) ? -1L : lbssARMConfig.getId();
        //根据编码查询数据
        LbssARMConfig ARMConfig = armConfigMapper.checkConfigCodeUnique(lbssARMConfig.getCode());
        //判断数据信息是否存在
        if (StringUtils.isNotNull(ARMConfig) && ARMConfig.getId().longValue() != id.longValue())
        {
            return UserConstants.CONFIG_CODE_NOT_UNIQUE;
        }
        return UserConstants.CONFIG_CODE_UNIQUE;
	}
}
