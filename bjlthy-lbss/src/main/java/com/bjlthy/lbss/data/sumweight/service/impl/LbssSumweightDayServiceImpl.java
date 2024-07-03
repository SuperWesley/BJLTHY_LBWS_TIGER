package com.bjlthy.lbss.data.sumweight.service.impl;

import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightDayService;
import com.bjlthy.lbss.tool.DataHelper;
import com.bjlthy.lbss.tool.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 日累计重量Service业务层处理
 * @date 2021年2月6日 下午12:00:38 @copyright(c) 北京龙田华远科技有限公司
 *
 */

@Service
public class LbssSumweightDayServiceImpl implements ILbssSumWeightDayService {

	@Autowired
	private LbssSumWeightHourMapper lbssSumWeightHourMapper;

	@Autowired
	private LbssSumWeightDayMapper lbssSumWeightDayMapper;

	@Autowired
	private LbssShiftMapper lbssShiftMapper;

	/**
	 * 查询日累计重量
	 * 
	 * @param id
	 *            日累计重量ID
	 * @return 日累计重量
	 */
	@Override
	public LbssSumWeightDay selectCoalSumWeightById(Integer id) {
		return lbssSumWeightDayMapper.selectLbssSumWeightDayById(id);
	}

	/**
	 * 查询日累计重量列表
	 *
	 * @param lbssSumweightDay 日累计重量
	 * @return 日累计重量
	 */
	@Override
	public List<LbssSumWeightDay> selectLbssSumweightDayList(LbssSumWeightDay lbssSumweightDay)
	{
		return lbssSumWeightDayMapper.selectLbssSumWeightDayList(lbssSumweightDay);
	}

	/**
	 * 新增日累计重量
	 * 
	 * @param lbssSumWeight
	 *            日累计重量
	 * @return 结果
	 */
	@Override
	public int insertCoalSumWeight(LbssSumWeightDay lbssSumWeight) {
		return lbssSumWeightDayMapper.insertLbssSumWeightDay(lbssSumWeight);
	}


	/**
	 * 查询每日的煤量信息
	 */
	@Override
	public Map<String,Object> queryDayWeightInfo(String startTime, String endTime,String belt_name) {
		//获取两个日期之间所有的日期
		List<String> days = DateHelper.getDays(startTime, endTime);
		//查询每月的煤量信息
		List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
		for (int i = 0; i < days.size(); i++) {
			String time = days.get(i);
			//获取上一天日期
			String nextTime = DateHelper.getNextDay(time, 1);
			LbssSumWeightDay weight = lbssSumWeightDayMapper.queryDayWeightInfo(belt_name,time);
			if(weight == null){
				weight = new LbssSumWeightDay();
				weight.setTotalWeight(0.0);
				weight.setTotalVolume(0.0);
			}
			weight.setDay(time);
			beltList.add(weight);
		}

		// 汇总每日的过皮带信息
		Map<String,Object> resultMap = getDayWeight(beltList,days,"W");
		return resultMap;
	}

	/**
	 * 查询每月的煤量信息
	 */
	@Override
	public Map<String,Object> queryMonthWeightInfo(String startTime, String endTime,String belt_name) {
		//获取两个日期之间所有的日期
		List<String> days = DateHelper.getDays(startTime, endTime);
		//查询每月的煤量信息
		List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
		for (int i = 0; i < days.size(); i++) {
			String time = days.get(i);
			//获取上一天日期
			String nextTime = DateHelper.getNextDay(time, 1);
			LbssSumWeightDay weight = lbssSumWeightDayMapper.queryDayWeightInfo(belt_name,time);
			if(weight == null){
				weight = new LbssSumWeightDay();
				weight.setTotalWeight(0.0);
				weight.setTotalVolume(0.0);
			}
			weight.setDay(time);
			beltList.add(weight);
		}

		// 汇总每日的过皮带信息
		Map<String,Object> resultMap = getDayWeight(beltList,days,"M");
		return resultMap;
	}

	/**
	 * 查询每年的煤量信息
	 */
	@Override
	public Map<String,Object> queryYearWeightInfo(String startTime, String endTime,String belt_name) {
		//查询每月的煤量信息
		List<LbssSumWeightDay> beltList = selectYearWeight(startTime, endTime,belt_name);
		//获取两个日期之间所有的日期
		List<String> days = DateHelper.getMonthDays(startTime, endTime);
		// 汇总每月的过皮带信息
		Map<String,Object> resultMap = getDayWeight(beltList,days,"Y");
		return resultMap;
	}

	/**
	 * 获取每天的过煤量信息
	 * @param list
	 */
	public Map<String, Object> getDayWeight(List<LbssSumWeightDay> list, List<String> days, String type){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//时间list
		List<String> timeList  = new ArrayList<String>();
		//过煤重量list
		List<String> weightList  = new ArrayList<String>();
		//过煤体积list
		List<String> volumeList  = new ArrayList<String>();
		//过煤松散密度list
		List<String> densityList  = new ArrayList<String>();
		//含矸率list
		List<String> gangueRatioList  = new ArrayList<String>();
		// 重量
		Double totalWeight = 0.00;
		// 体积
		Double totalVolume = 0.00;
		//平均采高
		Double avgheight = 0.00;
		//进尺
		Double sumfootage = 0.00;
		int no = 0;
		Map<String, String> msgMap = new HashMap<String, String>();
		for (int i = 0; i < list.size(); i++) {
			LbssSumWeightDay coalSumweight = list.get(i);
			// 日期
			String day = coalSumweight.getDay();
			// 重量
			Double weight = coalSumweight.getTotalWeight();
			totalWeight += weight;
			// 体积
			Double volume = coalSumweight.getTotalVolume();
			totalVolume += volume;
			//进行松散密度和含矸率校验
			Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(weight,volume);

			//松散密度
			Double day_density = Double.valueOf(map.get("density"));
			//含矸率
			Double day_gangueRatio = Double.valueOf(map.get("gangueRatio"));

			String msg = String.format("%.2f", weight)+","+String.format("%.2f", volume)+","+String.format("%.2f", (day_density))+","+String.format("%.2f", (day_gangueRatio));
			msgMap.put(day, msg);

		}

		if("W".equals(type)){//日期格式yyyy-MM-dd
			for(String day:days){
				timeList.add(day);
				String msg = msgMap.get(day);
				if(StringUtils.isNotEmpty(msg)){
					String[] data = msg.split(",");
					weightList.add(data[0]);
					volumeList.add(data[1]);
					densityList.add(data[2]);
					gangueRatioList.add(data[3]);
				}else{
					weightList.add("0.00");
					volumeList.add("0.00");
					densityList.add("0.00");
					gangueRatioList.add("0.00");
				}
			}
		}else if("M".equals(type)){//日期格式MM-dd
			for(String day:days){
				timeList.add(day);
				String msg = msgMap.get(day);
				if(StringUtils.isNotEmpty(msg)){
					String[] data = msg.split(",");
					weightList.add(data[0]);
					volumeList.add(data[1]);
					densityList.add(data[2]);
					gangueRatioList.add(data[3]);
				}else{
					weightList.add("0.00");
					volumeList.add("0.00");
					densityList.add("0.00");
					gangueRatioList.add("0.00");
				}
			}
		}else if("Y".equals(type)){//日期格式yyyy-MM
			for(String day:days){
				timeList.add(day);
				String msg = msgMap.get(day);
				if(StringUtils.isNotEmpty(msg)){
					String[] data = msg.split(",");
					weightList.add(data[0]);
					volumeList.add(data[1]);
					densityList.add(data[2]);
					gangueRatioList.add(data[3]);
				}else{
					weightList.add("0.00");
					volumeList.add("0.00");
					densityList.add("0.00");
					gangueRatioList.add("0.00");
				}
			}
		}

		//进行松散密度和含矸率校验
		Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(totalWeight,totalVolume);
		//放入map中
		//总过煤重量
		resultMap.put("totalWeight", String.format("%.2f", totalWeight));
		//总过煤体积
		resultMap.put("totalVolume", String.format("%.2f", totalVolume));
		//平均松散密度
		resultMap.put("density", map.get("density"));
		//平均含矸率
		resultMap.put("gangueRatio", map.get("gangueRatio"));
		//平均采高
		resultMap.put("height", avgheight);
		//掘进进尺
		resultMap.put("footage", sumfootage);
		resultMap.put("timeList", timeList);
		//重量信息集合
		resultMap.put("weightList", weightList);
		//体积信息集合
		resultMap.put("volumeList", volumeList);
		//松散密度信息集合
		resultMap.put("densityList", densityList);
		//含矸率集合
		resultMap.put("gangueRatioList", gangueRatioList);

		//获取煤量最大值
		List<Double> wList = weightList.stream().map(Double::valueOf).collect(Collectors.toList());
		Long max_weight = Collections.max(wList).longValue();
		if(max_weight==0){
			resultMap.put("max", "1000");
			resultMap.put("interval", "200");
		}else{
			//转整补0
			String number = 1+String.format("%0" + (max_weight.toString().length()-1) + "d", 0);
			Long max = (long) (Integer.valueOf(max_weight.toString().substring(0, 1))*2) * Long.valueOf(number);
			int interval = (int) (max/5);
			resultMap.put("max", max);
			resultMap.put("interval", interval);
		}

		return resultMap;
	}

	/**
	 * 统计每年各月份产量信息
	 * @author 张宁
	 * @description 方法说明
	 * @param 参数
	 * @date 2023年7月10日 下午4:30:06
	 */
	public List<LbssSumWeightDay> selectYearWeight(String startTime, String endTime, String belt_name){
		List<String> months = DateHelper.getMonthDays(startTime, endTime);
		//查询每年的煤量信息
		List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
		for (int i = 0; i < months.size(); i++) {
			String time = months.get(i);
			//获取下个月日期
			String nextTime = DateHelper.getNextMonth(time, 1);
			LbssSumWeightDay weight = lbssSumWeightDayMapper.queryMonthWeightInfo(belt_name,time+"-01 ",nextTime+"-01 ");
			if(weight == null){
				weight = new LbssSumWeightDay();
				weight.setTotalWeight(0.0);
				weight.setTotalVolume(0.0);
			}
			weight.setDay(time);
			beltList.add(weight);
		}
		return beltList;

	}
	
	/**
	 * 查询详细统计信息
	 */
	@Override
	public List<LbssSumWeightDay> queryDetailData(LbssSumWeightDay sumWeightDay) {
		// 查询一段时间内的皮带信息
		List<LbssSumWeightDay> list = new ArrayList<LbssSumWeightDay>();
		String status = sumWeightDay.getStatus();
		switch (status) {

		case "2":
			// 根据小时查皮带信息
			list = lbssSumWeightHourMapper.findHourWeightInfoByTime(sumWeightDay);
			break;
		case "3":
			// 获取两个日期之间所有的日期
			list = lbssSumWeightDayMapper.findDayWeightInfoByTime(sumWeightDay);
			break;
		case "4":
			// 根据月查皮带信息
			list = lbssSumWeightDayMapper.findMonthWeightInfoByTime(sumWeightDay);
			break;
		case "5":
			// 根据年查皮带信息
			list = lbssSumWeightDayMapper.findYearWeightInfoByTime(sumWeightDay);
			break;

		default:
			break;
		}
		return list;
	}


	/**
	 *
	 * @author 张宁
	 * @description 汇总过煤量信息
	 * @param list
	 *            返回值
	 * @date 2020年11月14日 下午12:06:14
	 */
	public Map<String, String> sumWeight(List<LbssSumWeightDay> list) {
		Map<String, String> map = new HashMap<String, String>();
		// 重量
		Double totalWeight = 0.00;
		// 体积
		Double totalVolume = 0.00;
		// 合计重量、体积
		for (int i = 0; i < list.size(); i++) {
			LbssSumWeightDay sumweight = list.get(i);
			// 重量
			totalWeight = DataHelper.add(totalWeight, sumweight.getTotalWeight());
			// 体积
			totalVolume = DataHelper.add(totalVolume,sumweight.getTotalVolume());
		}
		// 进行松散密度和含矸率校验
		Map<String, Double> resultMap = DataHelper.checkDensityAndGangueRatio(totalWeight.doubleValue(), totalVolume.doubleValue());

		// 页面赋值
		map.put("totalWeight", String.format("%.2f", totalWeight));
		map.put("totalVolume", String.format("%.2f", totalVolume));
		map.put("avgDensity", resultMap.get("density").toString());
		map.put("avgGangueRatio", resultMap.get("gangueRatio").toString());

		return map;
	}

	/**
	 *  进行List数据合计计算
	 * @param list
	 * @return
	 */
	public List<LbssSumWeightDay> totalListInfo (List<LbssSumWeightDay> list){
		Double totalWeight = 0.00;
		Double totalVolume = 0.00;
		//汇总总重量和总体积
		for(LbssSumWeightDay sumWeight : list){
			totalWeight +=  sumWeight.getTotalWeight();
			totalVolume +=  sumWeight.getTotalVolume();
		}

		//进行合计计算
		LbssSumWeightDay data = new LbssSumWeightDay();
		data.setDay(" 合计 ：");
		//重量保留2位小数
		BigDecimal weight = new BigDecimal(totalWeight);
		totalWeight = weight.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		data.setTotalWeight(totalWeight);
		//体积保留2位小数
		BigDecimal volume = new BigDecimal(totalVolume);
		totalVolume = volume.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		data.setTotalVolume(totalVolume);

		//进行松散密度和含矸率计算
		Map<String,Double> resultMap = DataHelper.checkDensityAndGangueRatio(totalWeight,totalVolume);
		//松散密度
		data.setDensity(resultMap.get("density"));
		//含矸率
		data.setGangueRatio(resultMap.get("gangueRatio"));
		//添加到List
		list.add(data);
		//返回List
		return  list;
	}

}
