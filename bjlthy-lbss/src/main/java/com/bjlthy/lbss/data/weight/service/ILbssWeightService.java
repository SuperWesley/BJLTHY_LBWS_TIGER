package com.bjlthy.lbss.data.weight.service;


import com.bjlthy.lbss.data.weight.domain.LbssWeight;

import java.util.List;
import java.util.Map;


/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 皮带实时重量Service接口
 * @date 2021年2月6日 上午10:35:24
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public interface ILbssWeightService 
{

    /**
     * 查询实时重量列表
     * 
     * @param coalWeight 实时重量
     * @return 实时重量集合
     */
    public List<LbssWeight> selectCoalWeightList(LbssWeight coalWeight);

	/**
	 * 
	 * @author 张宁
	 * @description 查询实时数据
	 * @param 参数
	 * @date 2020年12月1日 下午8:55:05
	 */
	public Map<String, Object> findRealWeightInfo(String belt_name) throws Exception;

	/**
	 *
	 * @author 张宁
	 * @description web端查询实时数据
	 * @date 2020年12月1日 下午8:55:05
	 */
	public Map<String, Object> webFindRealWeightInfo(String belt_name) throws Exception;


}
