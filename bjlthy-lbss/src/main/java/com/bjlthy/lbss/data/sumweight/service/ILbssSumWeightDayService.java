package com.bjlthy.lbss.data.sumweight.service;


import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;

import java.util.List;
import java.util.Map;




/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 日累计重量Service接口
 * @date 2021年2月6日 下午12:00:25
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */

public interface ILbssSumWeightDayService
{
    /**
     * 查询日累计重量
     * 
     * @param id 日累计重量ID
     * @return 日累计重量
     */
    public LbssSumWeightDay selectCoalSumWeightById(Integer id);
	/**
	 * 查询日累计重量列表
	 *
	 * @param lbssSumweightDay 日累计重量
	 * @return 日累计重量集合
	 */
	public List<LbssSumWeightDay> selectLbssSumweightDayList(LbssSumWeightDay lbssSumweightDay);

    /**
     * 新增日累计重量
     * 
     * @param CoalSumWeight 日累计重量
     * @return 结果
     */
    public int insertCoalSumWeight(LbssSumWeightDay lbssSumWeight);
    
	/**
	 * 
	 * @author 张宁
	 * @description 查询每天的煤量信息
	 * @param 参数
	 * @date 2021年3月2日 下午3:07:53
	 */
	public Map<String, Object> queryDayWeightInfo(String belt_name,String beginTime, String endTime);
	
	/**
	 * 
	 * @author 张宁
	 * @description 查询每月的煤量信息
	 * @param 参数
	 * @date 2021年2月22日 下午3:42:03
	 */
	public Map<String,Object> queryMonthWeightInfo(String startTime, String endTime,String belt_name);
	
	/**
	 * 
	 * @author 张宁
	 * @description 查询每年的煤量信息
	 * @param 参数
	 * @date 2021年2月22日 下午3:42:03
	 */
	public Map<String, Object> queryYearWeightInfo(String beginTime, String endTime,String belt_name);
	
	/**
	 * 
	 * @author 张宁
	 * @description 查询详细统计信息
	 * @param 参数
	 * @date 2021年2月22日 下午5:38:03
	 */
	public List<LbssSumWeightDay> queryDetailData(LbssSumWeightDay lbssSumWeight);

	/**
	 *
	 * @author 张宁
	 * @Description: 对List进行合计计算
	 * @param @param list
	 * @return List<LbssSumWeight>    返回类型
	 * @date 2021年9月11日 下午8:59:15
	 * @throws
	 */
	public List<LbssSumWeightDay> totalListInfo(List<LbssSumWeightDay> list);


}
