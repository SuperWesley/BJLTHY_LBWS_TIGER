package com.bjlthy.lbss.config.config.service;


import com.bjlthy.lbss.config.config.domain.LbssARMConfig;

import java.util.List;



/**
 * 其他配置Service接口
 * 
 * @author 张宁
 * @date 2021-01-03
 */
public interface ILbssARMConfigService 
{
    /**
     * 查询其他配置
     * 
     * @param id 其他配置ID
     * @return 其他配置
     */
    public LbssARMConfig selectLbssARMConfigById(Integer id);

    /**
     * 查询其他配置列表
     * 
     * @param LbssOtherConfig 其他配置
     * @return 其他配置集合
     */
    public List<LbssARMConfig> selectLbssARMConfigList(LbssARMConfig lbssARMConfig);

    /**
     * 新增其他配置
     * 
     * @param LbssOtherConfig 其他配置
     * @return 结果
     */
    public int insertLbssARMConfig(LbssARMConfig lbssARMConfig);

    /**
     * 修改其他配置
     * 
     * @param LbssOtherConfig 其他配置
     * @return 结果
     */
    public int updateLbssARMConfig(LbssARMConfig lbssARMConfig);

    /**
     * 批量删除其他配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssARMConfigByIds(String ids);

    /**
     * 删除其他配置信息
     * 
     * @param id 其他配置ID
     * @return 结果
     */
    public int deleteLbssARMConfigById(Integer id);

    /**
     * 
     * @author 张宁
     * @description 更新下位机配置信息
     * @param 参数
     * @date 2021年2月27日 下午7:41:51
     */
	public void updateConfig(LbssARMConfig lbssARMConfig);
	
	/**
	 * 
	 * @author 张宁
	 * @description 校验编码是否存在
	 * @param 参数
	 * @date 2021年3月2日 下午8:27:53
	 */
	public String checkConfigCodeUnique(LbssARMConfig lbssARMConfig);
}
