package com.bjlthy.lbss.data.weight.service.impl;

import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.data.weight.domain.LbssWeight;
import com.bjlthy.lbss.data.weight.domain.LbssWeightDTO;
import com.bjlthy.lbss.data.weight.mapper.LbssWeightMapper;
import com.bjlthy.lbss.data.weight.service.ILbssWeightService;
import com.bjlthy.lbss.dataComm.redis.RedisUtil;
import com.bjlthy.lbss.dataComm.socket.processor.belt.BeltRealtimeDataProcessor;
import com.bjlthy.lbss.tool.*;
import com.bjlthy.lbss.zero.info.domain.LbssZeroinfo;
import com.bjlthy.lbss.zero.info.mapper.LbssZeroinfoMapper;
import jakarta.annotation.Resource;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 实时重量Service业务层处理
 * @date 2020年12月1日 下午7:20:45
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
@Service
public class LbssWeightServiceImpl implements ILbssWeightService {
	@Resource
	private LbssWeightMapper lbssWeightMapper;


	@Resource
	private LbssSumWeightHourMapper lbssSumweightHourMapper;

	@Resource
	private LbssSumWeightDayMapper lbssSumweightDayMapper;

	@Resource
	private LbssZeroinfoMapper lbssZeroinfoMapper;

	@Resource
	private LbssShiftMapper lbssShiftMapper;

	@Resource
	private RedisUtil redisUtil ;

	/**
	 * 查询实时重量列表
	 *
	 * @return 实时重量
	 */
	@Override
	public List<LbssWeight> selectCoalWeightList(LbssWeight lbssWeight) {
		return lbssWeightMapper.selectLbssWeightList(lbssWeight);
	}


	/**
	 * @author 张宁
	 * @description 实时查询皮带数据进行界面显示
	 * @date 2020年12月1日 下午8:55:05
	 */
	public Map<String, Object> findRealWeightInfo(String belt_name) throws Exception {

		List<LbssWeight> weightList = new ArrayList<LbssWeight>();
		LbssWeightDTO weightDTO = new LbssWeightDTO();
		Map<String, Double> dayMap = new HashMap<String, Double>();
		Map<String, Double> hourMap = new HashMap<String, Double>();
		Map<String, Object> weekMap = new HashMap<>();
		LbssWeight coalWeight = new LbssWeight();
		String today = DateUtil.getDate();
		String currentHour = today + " " + DateUtil.getHour();
		String nextHour = DateHelper.getNextHour(currentHour, 1);
		//查询最近一分钟的瞬时信息
		weightList = lbssWeightMapper.findMinuteInfo(belt_name);
		if(weightList.size() != 0){
			//获取最新一条数据
			coalWeight = weightList.get(weightList.size() - 1);
			//对象复制
			PropertyUtils.copyProperties(weightDTO, coalWeight);
		}

		//当日煤量信息
		if (!"00:00:00".equals(ConfigBean.startTime)) {
			String lastDay = DateHelper.getLastDay(today, 1);
			dayMap = lbssWeightMapper.findDayWeight(belt_name,lastDay + " " + ConfigBean.startTime, today + " " + ConfigBean.startTime);
		} else {
			String nextDay = DateHelper.getNextDay(today, 1);
			dayMap = lbssWeightMapper.findDayWeight(belt_name,today + " " + ConfigBean.startTime, nextDay + " " + ConfigBean.startTime);

		}
		//查询当前小时煤量
		hourMap = lbssWeightMapper.findDayWeight(belt_name,currentHour + ":00:00", nextHour + ":00:00");

		weightDTO.setBelt_name(belt_name);
		//设备正常
		weightDTO.setStatus("1");
		Collection<String> values = BeltRealtimeDataProcessor.beltsMap.values();
		boolean exists = values.contains(belt_name);
		if (!exists){
			weightDTO.setSpeed(0.0);
			weightDTO.setDensity(0.0);
			weightDTO.setGangueRatio(0.0);
			//设备异常
			weightDTO.setStatus("0");
		}
		//查询当日煤量
		weightDTO.setTotalVolume((dayMap== null || dayMap.size() == 0) ? 0.00 : dayMap.get("total_volume"));
		weightDTO.setHourWeight((hourMap==null || hourMap.size() == 0) ? 0.00 : hourMap.get("total_weight"));
		weightDTO.setHourVolume((hourMap==null || hourMap.size() == 0) ? 0.00 : hourMap.get("total_volume"));
		//汇总班产量信息
		weekMap = getShiftWeight(weightDTO, belt_name);
		//查询最近一周的煤量信息
		getWeekTotalInfo(weightDTO, weekMap);

		//查询当年煤量信息
		String currentYear = DateUtil.getYear();
		String nextYear = DateHelper.getNextYear(currentYear, 1);
		Double yearWeight = lbssSumweightHourMapper.findYearWeight(belt_name,currentYear, nextYear);
		if (yearWeight == null) {
			yearWeight = 0.0;
		}
		//查询当月煤量信息
		String currentmonth = currentYear + "-" + DateUtil.getMonth();
		String nextmonth = DateHelper.getNextMonth(currentmonth, 1);
		Double monthWeight = lbssSumweightHourMapper.findMonthWeight(belt_name,currentmonth, nextmonth);
		if (monthWeight == null) {
			monthWeight = 0.0;
		}
		//查询调零信息
		LbssZeroinfo zeroInfo = lbssZeroinfoMapper.findNewestZeroInfo(belt_name);
		//数据合并
		Map<String, Object> ajaxMap = mergeMap(weightList, weightDTO, weekMap);
		//添加月统计信息
		ajaxMap.put("month_weight", String.format("%.2f", monthWeight + weightDTO.getHourWeight()));
		ajaxMap.put("year_weight", String.format("%.2f", yearWeight + weightDTO.getHourWeight()));
		ajaxMap.put("day_weight", String.format("%.2f", weightDTO.getTotalWeight()));
		ajaxMap.put("nig_weight", String.format("%.2f", weightDTO.getNig_weight()));
		ajaxMap.put("mor_weight", String.format("%.2f", weightDTO.getMor_weight()));
		ajaxMap.put("aft_weight", String.format("%.2f", weightDTO.getAft_weight()));
		ajaxMap.put("weight", String.format("%.2f", weightDTO.getWeight()));
		ajaxMap.put("hour_weight", String.format("%.2f", weightDTO.getHourWeight()));
		ajaxMap.put("month", currentmonth.substring(5, 7));
		ajaxMap.put("speed", String.format("%.2f", weightDTO.getSpeed()));
		ajaxMap.put("gangueRatio", String.format("%.2f", weightDTO.getGangueRatio()));
		ajaxMap.put("density", String.format("%.2f", weightDTO.getDensity()));
		ajaxMap.put("belt_name", belt_name);
		ajaxMap.put("status", weightDTO.getStatus());
		ajaxMap.put("zeroInfo", zeroInfo);
		double maxweight_sensor = DictionariesHelper.getDicDoubleValueByCode(belt_name + "-maxweight_sensor");
		if (weightDTO.getHourWeight() > maxweight_sensor) {
			ajaxMap.put("hourWeight_warning", "整时累计重量已超过压力传感器最大量程");
		} else {
			ajaxMap.put("hourWeight_warning", "");
		}
		redisUtil.set(belt_name,ajaxMap);
		return ajaxMap;
	}

	/**
	 *
	 * @author 张宁
	 * @description web端查询实时数据
	 * @date 2020年12月1日 下午8:55:05
	 */
	
	@Override
	public Map<String, Object> webFindRealWeightInfo(String belt_name) throws Exception{
		Map<String, Object> map  = (Map<String, Object>) redisUtil.get(belt_name);
		if (map != null){
			return map;
		}else{
			Map<String, Object> reslutMap = findRealWeightInfo(belt_name);
			return reslutMap;
		}
	}


	/**
	 * @param @param  weightList
	 * @param @param  weightDTO
	 * @param @param  weekMap
	 * @param @return 设定文件
	 * @return Map<String, Object>    返回类型
	 * @throws
	 * @author 张宁
	 * @Title: mergeMap
	 * @Description: 数据信息
	 * @date 2021年8月19日 上午11:21:35
	 */
	private Map<String, Object> mergeMap(List<LbssWeight> weightList, LbssWeightDTO weightDTO,
										 Map<String, Object> weekMap) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//时间list
		List<String> timeList = new ArrayList<String>();
		//重量list
		List<Double> wList = new ArrayList<Double>();
		//体积list
		List<Double> vList = new ArrayList<Double>();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		//汇总瞬时体积和重量
		for (LbssWeight weights : weightList) {
			//时间
			String time = sdf.format(weights.getTime());
			timeList.add(time);
			//瞬时重量
			Double weight = weights.getWeight() * 3600;

			wList.add(ParseUtils.formart(weight + ""));
			//瞬时体积
			Double volume = weights.getVolume() * 3600;

			vList.add(ParseUtils.formart(volume + ""));
		}
		//时间集合
		resultMap.put("timeList", timeList);
		//瞬时重量集合
		resultMap.put("weightList", wList);
		//瞬时体积集合
		resultMap.put("volumeList", vList);
		//实时数据
		resultMap.put("realBelt", weightDTO);
		//周产量集合
		resultMap.put("weekWeights", weekMap.get("weekWeights"));
		//周日期
		resultMap.put("weekTime", weekMap.get("weekTime"));
		//合并班次信息
		resultMap.put("shiftNames", weekMap.get("shiftNames"));
		resultMap.put("shiftList", weekMap.get("shiftList"));
		return resultMap;
	}


	/**
	 * @author 张宁
	 * @description 皮带班次信息汇总
	 * @date 2021年2月24日 下午8:56:48
	 */
	public Map<String, Object> getShiftWeight(LbssWeightDTO lbssWeight, String belt_name) {

		List<String> shiftList = new ArrayList<String>();
		List<String> shiftNames = new ArrayList<String>();

		Double aftDou = 0.0;
		Double morDou = 0.0;
		Double nigDou = 0.0;
		List<LbssShift> list = lbssShiftMapper.selectLbssShiftList(null);
		for (int i = list.size() - 1; i >= 0; i--) {
			LbssShift shift = list.get(i);
			String startTime = shift.getBegin();
			String endTime = shift.getEnd();
			shiftNames.add(shift.getName());
			//按照工作面信息存储到对应的实时数据表中
			Double zy2z_maxweight = DictionariesHelper.getDicDoubleValueByCode(belt_name+"-maxweight_sensor");
			if ("aft".equals(shift.getCode())) {
				if ("00:00:00".compareTo(ConfigBean.startTime) <= 0 && "18:00:00".compareTo(ConfigBean.startTime) > 0) {//未超过18点算今天
					aftDou = lbssWeightMapper.selectShiftWeight(belt_name,DateUtil.getDate() + " " + startTime, DateHelper.getNextDay(DateUtil.getDate(), 1) + " " + endTime);
					if (aftDou >zy2z_maxweight*8){
						aftDou = getSumWeight(belt_name,DateUtil.getDate() + " " + startTime, DateHelper.getNextDay(DateUtil.getDate(), 1) + " " + endTime);
					}
				} else {
					aftDou = lbssWeightMapper.selectShiftWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
					if (aftDou >zy2z_maxweight*8){
						aftDou = getSumWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
					}
				}
				//中班
				shiftList.add(String.format("%.2f", aftDou));
				lbssWeight.setAft_weight(aftDou);
			} else if ("mor".equals(shift.getCode())) {
				morDou = lbssWeightMapper.selectShiftWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
				if (morDou >zy2z_maxweight*8){
					morDou = getSumWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
				}
				//早班
				shiftList.add(String.format("%.2f", morDou));
				lbssWeight.setMor_weight(morDou);
			} else if ("nig".equals(shift.getCode())) {
				if ("00:00:00".compareTo(ConfigBean.startTime) <= 0 && "18:00:00".compareTo(ConfigBean.startTime) > 0) {//未超过18点算今天
					nigDou = lbssWeightMapper.selectShiftWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
					if (nigDou >zy2z_maxweight*8){
						nigDou = getSumWeight(belt_name,DateUtil.getDate() + " " + startTime, DateUtil.getDate() + " " + endTime);
					}
				} else {
					String today = DateUtil.getDate();
					nigDou = lbssWeightMapper.selectShiftWeight(belt_name,DateHelper.getLastDay(today, 1) + " " + startTime, DateUtil.getDate() + " " + endTime);
					if (nigDou >zy2z_maxweight*8){
						nigDou = getSumWeight(belt_name,DateHelper.getLastDay(today, 1) + " " + startTime, DateUtil.getDate() + " " + endTime);
					}
				}
				//晚班
				shiftList.add(String.format("%.2f", nigDou));
				lbssWeight.setNig_weight(nigDou);
			}


		}
		lbssWeight.setTotalWeight(aftDou + morDou + nigDou);
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("shiftNames", shiftNames);
		resultMap.put("shiftList", shiftList);
//		resultMap.put("LbssWeightDTO", lbssWeight);

		return resultMap;

	}

	public Double getSumWeight(String belt_name,String start,String end){
		LbssWeight lbssWeight= null;

		lbssWeight = lbssWeightMapper.findSumWeight(belt_name,start, end);
		Double sumWeight = 0.0;
		if (lbssWeight != null){
			sumWeight = lbssWeight.getTotalWeight();
		}
		return sumWeight;
	}
	/**
	 * @param @return 设定文件
	 * @return Map<String, Object>    返回类型
	 * @throws
	 * @author 张宁
	 * @Title: getWeekTotalInfo
	 * @Description: 查询最近一周的煤量信息
	 * @date 2021年8月18日 下午10:30:47
	 */
	public void getWeekTotalInfo(LbssWeightDTO weightDTO, Map<String, Object> map) {

		Double dayWeight = weightDTO.getTotalWeight();
		String currentTime = DateUtil.getDate("HH:mm:ss");
		String startTime = ConfigBean.startTime;
		String belt_name = weightDTO.getBelt_name();
		//查询最近6天的煤量信息
		List<LbssSumWeightDay> sumWeights = new ArrayList<LbssSumWeightDay>();
		//判断当前时间是否大于清零时间
		if (startTime.compareTo("18:00:00") >= 0) {
			if (currentTime.compareTo(startTime) >= 0) {
				//获取两个日期之间所有的日期
				String today = DateUtil.getDate();
				String TomDoday = DateHelper.getNextDay(today, 1);
				//前6天
				String sixDay = DateHelper.getLastDay(TomDoday, 6);
				//获取两个日期之间所有的日期
				List<String> days = DateHelper.getDays(sixDay, TomDoday);
				//查询最近6天的煤量信息
				for (int i = 0; i < days.size(); i++) {
					String time = days.get(i);
					//获取上一天日期
					String lastTime = DateHelper.getLastDay(time, 1);
					String nextTime = DateHelper.getNextDay(lastTime, 1);
					LbssSumWeightDay weight = lbssSumweightDayMapper.queryDayWeightInfo(belt_name,lastTime);
					if(weight == null){
						weight = new LbssSumWeightDay();
						weight.setTotalWeight(0.0);
					}
					weight.setDay(time);
					sumWeights.add(weight);
				}
				LbssSumWeightDay weight = new LbssSumWeightDay();
				weight.setDay(TomDoday);
				weight.setTotalWeight(dayWeight);
				sumWeights.add(weight);
			} else {
				String endDay = DateUtil.getDate();
				//前6天
				String startDay = DateHelper.getLastDay(endDay, 6);
				//获取两个日期之间所有的日期
				List<String> days = DateHelper.getDays(startDay, endDay);
				for (int i = 0; i < days.size(); i++) {
					String time = days.get(i);
					//获取上一天日期
					String lastTime = DateHelper.getLastDay(time, 1);
					LbssSumWeightDay weight = lbssSumweightDayMapper.queryDayWeightInfo(belt_name,lastTime);
					if (weight==null){
						weight = new LbssSumWeightDay();
						weight.setTotalWeight(0.0);
					}
					weight.setDay(time);
					sumWeights.add(weight);
				}
				LbssSumWeightDay weight = new LbssSumWeightDay();
				weight.setDay(endDay);
				weight.setTotalWeight(dayWeight);
				sumWeights.add(weight);
			}


		} else {
			String today = DateUtil.getDate();
			String TomDoday = DateHelper.getNextDay(today, 1);
			//前6天
			String sixDay = DateHelper.getLastDay(TomDoday, 6);
			//获取两个日期之间所有的日期
			List<String> days = DateHelper.getDays(sixDay, TomDoday);
			//查询最近6天的煤量信息
			for (int i = 0; i < days.size(); i++) {
				String time = days.get(i);
				//获取上一天日期
				String lastTime = DateHelper.getLastDay(time, 1);
				String nextTime = DateHelper.getNextDay(lastTime, 1);
				LbssSumWeightDay weight = lbssSumweightDayMapper.queryDayWeightInfo(belt_name,lastTime);
				if(weight == null){
					weight = new LbssSumWeightDay();
					weight.setTotalWeight(0.0);
				}
				weight.setDay(lastTime);
				sumWeights.add(weight);
			}
			LbssSumWeightDay weight = new LbssSumWeightDay();
			weight.setDay(today);
			weight.setTotalWeight(dayWeight);
			sumWeights.add(weight);

		}

		List<String> weekTime = new ArrayList<String>();
		List<Double> weekWeights = new ArrayList<Double>();
		//数据合并
		int length = sumWeights.size() - 1;
		for (int i = length; i >= 0; i--) {
			LbssSumWeightDay sumWeight = sumWeights.get(i);
			//日期
			weekTime.add(sumWeight.getDay().substring(5, 10));
			weekWeights.add(DataHelper.round(sumWeight.getTotalWeight(), 2));

		}

		map.put("weekTime", weekTime);
		map.put("weekWeights", weekWeights);


	}

}
