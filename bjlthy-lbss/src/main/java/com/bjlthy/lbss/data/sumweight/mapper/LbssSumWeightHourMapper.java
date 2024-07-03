package com.bjlthy.lbss.data.sumweight.mapper;


import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;

import java.util.List;

/**
 * 日累计重量Mapper接口
 * 
 * @author zn
 * @date 2020-11-10
 */
public interface LbssSumWeightHourMapper
{
    /**
     * 查询日累计重量
     * 
     * @param id 日累计重量ID
     * @return 日累计重量lbssSumWeightMapper
     */
    public LbssSumWeightHour selectLbssSumWeightById(Integer id);


    /**
     * 新增日累计重量
     * 
     * @param lbssSumWeight 日累计重量
     * @return 结果
     */
    public int insertLbssSumweight(LbssSumWeightHour lbssSumWeight);

	/**
	 * 更新过煤量信息
	 *
	 * @param lbssSumWeight 过煤量信息
	 * @return 结果
	 */
	public int updateLbssSumweight(LbssSumWeightHour lbssSumWeight);

	/**
	 * 查询煤量详细信息
	 * @param beginTime
	 * @param endTime
	 * @param beltName
	 * @return 集合
	 */
	public LbssSumWeightHour queryWeightInfo(String beltName,String beginTime, String endTime);

    /**
     * 统计每班次的煤量信息
     * @param beginTime
     * @param endTime
     * @param belt_name
	 * @return 集合
     */
    public LbssSumWeightHour queryShiftWeightInfo(String belt_name,String beginTime, String endTime);
    /**
     * 统计每班次的煤量信息
     * @param beginTime
     * @param endTime
     * @param belt_name
	 * @return 集合
     */
    public List<LbssSumWeightHour> queryShiftWeightInfo2(String beginTime, String endTime);

    /**
	 * 
	 * @author 张宁
	 * @description 统计每天的煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年3月2日 下午3:09:40
	 */
	public List<LbssSumWeightHour> queryDayWeightInfo(String beginTime, String endTime);
	
    /**
	 * 
	 * @author 张宁
	 * @description 查询每天的煤量信息--兴隆庄矿增加
	 * @param beginTime
	 * @param endTime
	 * @date 2021年3月2日 下午3:09:40
	 */
	public LbssSumWeightHour selectDayWeightInfo(String beginTime, String endTime);
	
	/**
     * 统计每班次的煤量信息
     * @param month
     * @return
     */
	public List<LbssSumWeightHour> queryMonthWeightInfo(String month);
	
	 /**
     * 统计每年的煤量信息
     * @param year
     * @return 集合
     */
    public List<LbssSumWeightHour> queryYearWeightInfo(String year);
    

	/**
	 * 
	 * @author 张宁
	 * @description 查询详细统计煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年2月22日 下午5:48:45
	 */
	public List<LbssSumWeightHour> queryDetailData(String beginTime, String endTime);


	/**
	 *
	 * @author 张宁
	 * @description 查询详细统计煤量信息--根据时间模糊查询
	 * @param beginTime
	 * @date 2021年2月22日 下午5:48:45
	 */
	public List<LbssSumWeightHour> queryDetailDataLikeTime(String beginTime);

	/**
	 * 
	 * @author 张宁
	 * @description 详细查询每小时的煤量信息
	 * @param beginTime
	 * @param endTime
	 * @date 2021年5月7日 上午10:00:29
	 */
	public List<LbssSumWeightHour> queryDetailDataByTime(String beginTime, String endTime);

	/**
	 *
	 * @author 张宁
	 * @description 模糊查询小时的煤量信息
	 * @param hour
	 * @date 2021年10月30日 上午10:00:29
	 */
	public LbssSumWeightHour queryDetailDataLikeHour(String beltName,String hour);

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
	/**
	 *
	 * @author 张宁
	 * @Description: 根据时间段查询每月的煤量信息
	 * @return List<LbssSumWeight>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightDay> findHourWeightInfoByTime(LbssSumWeightDay sumWeightDay);
	/**
	 * 
	 * @author 张宁
	 * @Description: 根据时间段查询每月的煤量信息
	 * @return List<LbssSumWeight>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightHour> queryMonthWeightInfoByTime(String beginTime, String endTime);
	
	/**
	 * 
	 * @author 张宁
	 * @Description: 根据时间段查询每年的煤量信息
	 * @return List<LbssSumWeight>    返回类型
	 * @date 2022年5月14日 上午10:12:13
	 * @throws
	 */
	public List<LbssSumWeightHour> queryYearWeightInfoByTime(String beginTime, String endTime);
}
