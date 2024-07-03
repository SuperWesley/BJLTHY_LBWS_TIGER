package com.bjlthy.lbss.data.sumweight.service;


import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;

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

public interface ILbssSumWeightHourService
{
    /**
     * 查询日累计重量
     * 
     * @param id 日累计重量ID
     * @return 日累计重量
     */
    public LbssSumWeightHour selectCoalSumWeightById(Integer id);


    /**
     * 新增日累计重量
     * 
     * @param CoalSumWeight 日累计重量
     * @return 结果
     */
    public int insertCoalSumWeight(LbssSumWeightHour CoalSumWeight);
    
	/**
	 * 
	 * @author 张宁
	 * @description 查询班次煤量信息
	 * @param 参数
	 * @date 2021年3月1日 下午10:57:07
	 */
	public Map<String, Object> queryShiftWeightInfo(String beginTime, String endTime,String belt_name);
	


}
