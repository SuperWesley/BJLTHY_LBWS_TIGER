package com.bjlthy.lbss.data.sumweight.mapper;


import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;

import java.util.List;



/**
 * 日累计重量Mapper接口
 * 
 * @author zn
 * @date 2020-11-10
 */
public interface LbssSumWeightDayMapper
{
    /**
     * 查询日累计重量
     * 
     * @param id 日累计重量ID
     * @return 日累计重量
     */
    public LbssSumWeightDay selectLbssSumWeightDayById(Integer id);

	/**
	 * 查询日累计重量列表
	 *
	 * @param lbssSumweightDay 日累计重量
	 * @return 日累计重量集合
	 */
	public List<LbssSumWeightDay> selectLbssSumWeightDayList(LbssSumWeightDay lbssSumweightDay);

    /**
     * 新增日累计重量
     * 
     * @param LbssSumWeightDay 日累计重量
     * @return 结果
     */
    public int insertLbssSumWeightDay(LbssSumWeightDay LbssSumWeightDay);

	/**
	 * 更新过煤量信息
	 *
	 * @param LbssSumWeightDay 过煤量信息
	 * @return 结果
	 */
	public int updateLbssSumWeightDay(LbssSumWeightDay LbssSumWeightDay);

	/**
	 * 
	 * @author 张宁
	 * @description 查询详细统计煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年2月22日 下午5:48:45
	 */
	public List<LbssSumWeightDay> queryDetailData(String beginTime, String endTime);

    /**
	 * 
	 * @author 张宁
	 * @description 统计每天的煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年3月2日 下午3:09:40
	 */
	public LbssSumWeightDay queryDayWeightInfo(String belt_name,String beginTime);
    /**
	 *
	 * @author 张宁
	 * @description 统计每y月的煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年3月2日 下午3:09:40
	 */
	public LbssSumWeightDay queryMonthWeightInfo(String belt_name,String beginTime, String endTime);

  /**
	 *
	 * @author 张宁
	 * @description 统计每天的煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年3月2日 下午3:09:40
	 */
	public LbssSumWeightDay queryDayWeightInfoByTime(LbssSumWeightDay LbssSumWeightDay);


	/**
	 * 
	 * @author 张宁
	 * @Description: 根据时间段查询每日的煤量信息
	 * @return List<LbssSumWeightDay>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightDay> findDayWeightInfoByTime(LbssSumWeightDay lbssSumWeightDay);
	
	/**
	 * 
	 * @author 张宁
	 * @Description: 根据时间段查询每月的煤量信息
	 * @return List<LbssSumWeightDay>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightDay> findMonthWeightInfoByTime(LbssSumWeightDay lbssSumWeightDay);
	
	/**
	 * 
	 * @author 张宁
	 * @Description: 根据时间段查询每年的煤量信息
	 * @return List<LbssSumWeightDay>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightDay> findYearWeightInfoByTime(LbssSumWeightDay lbssSumWeightDay);

	/**
	 *
	 * @author 张宁
	 * @Description: 根据时间段查询采高和进尺
	 * @return LbssSumWeightDay    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public LbssSumWeightDay findHeightORFootageByTime(String belt_name,String beginTime);

	/**
	 * 查询月过煤量信息
	 * @param month
	 * @param currentmonth 当前月
	 * @param nextmonth 下个月
	 * @return
	 */
	public Double findMonthWeight(String belt_name,String currentmonth, String nextmonth);

	/**
	 * 查询年过煤量信息
	 * @param month
	 * @param currentyear 当前年
	 * @param nextyear 下个年
	 * @return
	 */
	public Double findYearWeight(String belt_name,String currentyear, String nextyear);
}
