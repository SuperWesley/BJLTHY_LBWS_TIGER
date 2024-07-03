package com.bjlthy.lbss.config.config.mapper;


import com.bjlthy.lbss.config.config.domain.LbssARMConfig;

import java.util.List;



/**
 * 其他配置Mapper接口
 * 
 * @author 张宁
 * @date 2021-03-27
 */
public interface LbssARMConfigMapper 
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
     * @param 
     * @return 其他配置集合
     */
    public List<LbssARMConfig> selectLbssARMConfigList(LbssARMConfig lbssARMConfig);

    /**
     * 新增其他配置
     * 
     * @param 
     * @return 结果
     */
    public int insertLbssARMConfig(LbssARMConfig lbssARMConfig);

    /**
     * 修改其他配置
     * 
     * @param 
     * @return 结果
     */
    public int updateLbssARMConfig(LbssARMConfig lbssARMConfig);

    /**
     * 删除其他配置
     * 
     * @param id 其他配置ID
     * @return 结果
     */
    public int deleteLbssARMConfigById(Integer id);

    /**
     * 批量删除其他配置
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteLbssARMConfigByIds(String[] ids);
    /**
     * 
     * @author 张宁
     * @description 校验编码是否存在
     * @param 参数
     * @date 2021年3月2日 下午8:29:05
     */
	public LbssARMConfig checkConfigCodeUnique(String code);
}
