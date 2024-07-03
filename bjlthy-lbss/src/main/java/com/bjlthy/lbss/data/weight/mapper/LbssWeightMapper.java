package com.bjlthy.lbss.data.weight.mapper;


import com.bjlthy.lbss.data.weight.domain.LbssWeight;

import java.util.List;
import java.util.Map;


/**
 * 实时重量Mapper接口
 *
 * @author zn
 * @date 2020-11-10
 */
public interface LbssWeightMapper
{

	/**
	 * 查询实时重量列表
	 *
	 * @param coalWeight 实时重量
	 * @return 实时重量集合
	 */
	public List<LbssWeight> selectLbssWeightList(LbssWeight coalWeight);

	/**
	 * 新增实时重量
	 *
	 * @param coalWeight 实时重量
	 * @return 结果
	 */
	public int insertLbssWeight(LbssWeight coalWeight);

	/**
	 *
	 * @author 张宁
	 * @param string
	 * @description 查询一天内的重量
	 * @param startTime 开始时间
	 * @param endTime 结束时间
	 * @date 2020年12月1日 下午9:03:40
	 */
	public Map<String,Double> findDayWeight(String belt_name,String  startTime, String endTime);

	/**
	 *
	 * @author 张宁
	 * @Title: findMomentInfo
	 * @Description: 查询最近一分钟的煤量信息
	 * @param @return    设定文件
	 * @return List<LbssWeight>    返回类型
	 * @date 2021年8月19日 上午10:09:37
	 * @throws
	 */
	public List<LbssWeight> findMinuteInfo(String belt_name);

	/**
	 *
	 * @author 张宁
	 * @Title: findMomentInfo
	 * @Description: 查询最新的煤量信息
	 * @param @return    设定文件
	 * @return List<LbssWeight>    返回类型
	 * @date 2021年8月19日 上午10:09:37
	 * @throws
	 */
	public LbssWeight findNewInfo();

	/**
	 *
	 * @author 张宁
	 * @description 查询一段时间内瞬时重量和
	 * @param 参数
	 * @date 2023年3月17日 下午3:57:44
	 */
	public LbssWeight findSumWeight(String belt_name,String  startTime, String endTime);

	/**
	 *
	 * @author 张宁
	 * @description 查询一段时间内过煤重量
	 * @param 参数
	 * @date 2023年3月17日 下午3:57:44
	 */
	public LbssWeight selectSumWeight(String belt_name,String  startTime, String endTime);
	/**
	 *
	 * @author 张宁
	 * @description 查询一小时内的重量
	 * @param 参数
	 * @date 2020年12月1日 下午9:03:40
	 */
	public Double selectShiftWeight(String belt_name,String startTime,String endTime);

}
