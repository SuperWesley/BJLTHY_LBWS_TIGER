package com.bjlthy.lbss.data.sumweight.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.lbss.config.config.domain.LbssWorking;
import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.data.sumweight.service.ILbssALLBeltService;
import com.bjlthy.lbss.data.weight.domain.LbssWeightDTO;
import com.bjlthy.lbss.dataComm.opc.client.OPCUAUtil;
import com.bjlthy.lbss.dataComm.redis.RedisUtil;
import com.bjlthy.lbss.tool.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
public class LbssALLBeltServiceImpl implements ILbssALLBeltService {

	@Autowired
	private LbssSumWeightHourMapper lbssSumWeightHourMapper;
	@Autowired
	private LbssSumWeightDayMapper lbssSumWeightDayMapper;
	@Autowired
	private LbssShiftMapper lbssShiftMapper;

	@Resource
	private RedisUtil redisUtil ;
	/***
	 * 根据时间进行数据汇总
	 * @param list
	 * @param days
	 * @return
	 */
	public double[] getDayWeight(List<LbssSumWeightDay> list, List<String> days){
		double[] weightArr = new double[days.size()];
		//数据list转map
		Map<String,Object> map = list.stream().collect(Collectors.toMap(LbssSumWeightDay::getDay, weight->weight));
		for (int i = 0; i < days.size(); i++) {
			String day = days.get(i);
			String today = DateUtil.getDate();
			LbssSumWeightDay weight = (LbssSumWeightDay) map.get(day);
			if(weight != null){
				double totalWeight = weight.getTotalWeight();
				String Day = weight.getBelt_name();
	    		double working_hour = 0.0;
	    		if(day.equals(today)){
	    			if(StringUtils.isNotEmpty(ConfigBean.belt_hourMap)){
	    				working_hour = ConfigBean.belt_hourMap.get(Day);
	    			}
	    		}
	    		weightArr[i] = totalWeight+working_hour;
			}else{
				weightArr[i] = 0.0;
			}
		}
		return weightArr;
		
	}


	/**
	 * 查询详细统计信息
	 */
	@Override
	public List<LbssSumWeightDay> queryDetailData(LbssSumWeightDay sumWeightDay) {
		// 查询一段时间内的皮带信息
		List<LbssSumWeightDay> list = new ArrayList<LbssSumWeightDay>();
		String status = sumWeightDay.getStatus();
		String startTime = sumWeightDay.getStartTime();
		String endTime = sumWeightDay.getEndTime();
		switch (status) {

		case "2":
			// 根据小时查皮带信息
			list = lbssSumWeightHourMapper.findHourWeightInfoByTime(sumWeightDay);
			break;
		case "3":
			// 获取两个日期之间所有的日期
			if(!"00:00:00".equals(ConfigBean.startTime)){
				List<String> days = DateHelper.getDays(startTime, endTime);
				for (int i = 0; i < days.size(); i++) {
					String time = days.get(i);
					//获取上一天日期
					String lastTime = DateHelper.getLastDay(time, 1);
					String nextTime = DateHelper.getNextDay(lastTime, 1);
					if(endTime.compareTo(nextTime)>0){
						sumWeightDay.setStartTime(lastTime + " " + ConfigBean.startTime);
						sumWeightDay.setEndTime(nextTime + " " + ConfigBean.startTime);
						LbssSumWeightDay sumWeightDay2 = lbssSumWeightDayMapper.queryDayWeightInfoByTime(sumWeightDay);
						if (sumWeightDay2 != null){

							sumWeightDay2.setDay(nextTime);
							sumWeightDay2.setBelt_name(sumWeightDay.getBelt_name());
							list.add(sumWeightDay2);
						}
					}
				}
			}else{
				// 根据日查皮带信息
				list = lbssSumWeightDayMapper.findDayWeightInfoByTime(sumWeightDay);
			}

			break;
		case "4":
			// 根据月查皮带信息
			try {
				if(!"00:00:00".equals(ConfigBean.startTime)){
					List<String> days = DateHelper.getMonthDays(startTime, endTime);
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					for (int i = 0; i < days.size(); i++) {
						String time = days.get(i)+"-01";
						//获取上一天日期
						String lastTime = DateHelper.getLastDay(time, 1);
						String nextTime = DateHelper.getLastDayOfMonth2(sdf.parse(time));
						sumWeightDay.setStartTime(lastTime + " " + ConfigBean.startTime);
						sumWeightDay.setEndTime(nextTime + " " + ConfigBean.startTime);
						LbssSumWeightDay sumWeightDay2 = lbssSumWeightDayMapper.queryDayWeightInfoByTime(sumWeightDay);
						if (sumWeightDay2 != null){

							sumWeightDay2.setDay(days.get(i));
							sumWeightDay2.setBelt_name(sumWeightDay.getBelt_name());
							list.add(sumWeightDay2);
						}
					}
				}else{
					startTime = startTime + "-01 00:00:00";
					endTime = endTime + "-01 00:00:00";
					sumWeightDay.setStartTime(startTime);
					sumWeightDay.setEndTime(endTime);
					list = lbssSumWeightDayMapper.findMonthWeightInfoByTime(sumWeightDay);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case "5":
			// 根据年查皮带信息

			try {
				if(!"00:00:00".equals(ConfigBean.startTime)){
					List<String> days = DateHelper.getYearDays(startTime, endTime);
					DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					for (int i = 0; i < days.size(); i++) {
						String time = days.get(i)+"-01-01";
						//获取上一天日期
						String lastTime = DateHelper.getLastDay(time, 1);
						String nextTime = days.get(i)+"-12-31";
						sumWeightDay.setStartTime(lastTime + " " + ConfigBean.startTime);
						sumWeightDay.setEndTime(nextTime + " " + ConfigBean.startTime);
						LbssSumWeightDay sumWeightDay2 = lbssSumWeightDayMapper.queryDayWeightInfoByTime(sumWeightDay);
						if (sumWeightDay2 != null){

							sumWeightDay2.setDay(days.get(i));
							sumWeightDay2.setBelt_name(sumWeightDay.getBelt_name());
							list.add(sumWeightDay2);
						}
					}
				}else{
					startTime =  startTime + "-01-01 00:00:00";
					endTime = endTime + "-01-01 00:00:00";
					sumWeightDay.setStartTime(startTime);
					sumWeightDay.setEndTime(endTime);
					list = lbssSumWeightDayMapper.findYearWeightInfoByTime(sumWeightDay);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;

		default:
			break;
		}
		if("2,3".indexOf(status)<0){
			// 数据统计
			for (int i = 0; i < list.size(); i++) {
				LbssSumWeightDay lbssSumWeight = list.get(i);
				Double totalWeight = lbssSumWeight.getTotalWeight();
				Double totalVolume = lbssSumWeight.getTotalVolume();
				Double density = 0.00;
				Double gangueRatio = lbssSumWeight.getGangueRatio();
				if (totalWeight != 0 && totalVolume != 0) {
					// 松散密度
					Map<String, Double> map = DataHelper.checkDensityAndGangueRatio(totalWeight, totalVolume);
					density = map.get("density");
					gangueRatio = map.get("gangueRatio");

				}
				lbssSumWeight.setDensity(density);
				lbssSumWeight.setGangueRatio(gangueRatio);

				String Belt_name = ConfigBean.beltsMap.get(lbssSumWeight.getBelt_name()).getBelt_name();
				lbssSumWeight.setBelt_name(Belt_name);

			}
		}
		return list;
	}

	/**
	 *
	 * @author 张宁
	 * @description Map 合并
	 * @param 参数
	 * @date 2020年11月26日 下午5:04:03
	 */
	public Map<String, Object> mergeMap(Map<String, List<String>> resMap, Map<String, String> sumMap) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 总过煤重量
		map.put("totalWeight", sumMap.get("totalWeight"));
		// 总过煤体积
		map.put("totalVolume", sumMap.get("totalVolume"));
		// 平均松散密度
		map.put("avgDensity", sumMap.get("avgDensity"));
		// 平均含矸率
		map.put("avgGangueRatio", sumMap.get("avgGangueRatio"));

		map.put("day", resMap.get("day"));
		map.put("weight", resMap.get("weight"));
		map.put("volume", resMap.get("volume"));
		map.put("gangueRatio", resMap.get("gangueRatio"));
		return map;
	}

	/**
	 * 统计班次的过煤量信息
	 *
	 * @param list
	 */
	public Map<String, Object> getShiftWeightInfo(List<LbssSumWeightHour> nigList, List<LbssSumWeightHour> morList, List<LbssSumWeightHour> aftList) {
		Map<String, Object> map = new HashMap<String, Object>();
		//工作面信息
		List<LbssWorking> beltNames = ConfigBean.workingList;
		double[][] totalWeightArr = new double[beltNames.size()][];
		int index = 0;
		for (LbssWorking working : beltNames) {

			List<Double> sumWeight = new ArrayList<Double>();
			//晚班信息
			for (int i = 0; i < nigList.size(); i++) {
				LbssSumWeightHour weight = nigList.get(i);
				Double totalWeight = weight.getTotalWeight();
	    		//按照工作面信息添加到集合中
	    		if(working.getBelt_name().equals(weight.getBelt_name())){
	    			sumWeight.add(totalWeight);
	    		}
			}
			//早班信息
			for (int i = 0; i < morList.size(); i++) {
				LbssSumWeightHour weight = morList.get(i);
				Double totalWeight = weight.getTotalWeight();
	    		//按照工作面信息添加到集合中
	    		if(working.getBelt_name().equals(weight.getBelt_name())){
	    			sumWeight.add(totalWeight);
	    		}
			}
			//中班信息
			for (int i = 0; i < aftList.size(); i++) {
				LbssSumWeightHour weight = aftList.get(i);
				Double totalWeight = weight.getTotalWeight();
	    		//按照工作面信息添加到集合中
	    		if(working.getBelt_name().equals(weight.getBelt_name())){
	    			sumWeight.add(totalWeight);
	    		}
			}
			double[] weights = sumWeight.stream().mapToDouble(Double::doubleValue).toArray();
			totalWeightArr[index] = weights;
			index++;
			// 将所有数据存放在map集合中
			//map.put("totalWeight"+Day, sumWeight);
		}
		// 将所有数据存放在map集合中
		map.put("totalWeightArr", totalWeightArr);
		return map;

	}

	/**
	 *
	 * @author 张宁
	 * @description 汇总过煤量信息
	 * @param list
	 *            返回值
	 * @date 2020年11月14日 下午12:06:14
	 */
	public Map<String, String> sumWeight(List<LbssSumWeightHour> list) {
		Map<String, String> map = new HashMap<String, String>();
		// 重量
		Double totalWeight = 0.00;
		// 体积
		Double totalVolume = 0.00;
		// 合计重量、体积
		for (int i = 0; i < list.size(); i++) {
			LbssSumWeightHour sumweight = list.get(i);
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
	 * 
	 * @author 张宁 @Description: 查询各工作面产量信息 @return List<LbssSumWeightDay>
	 * 返回类型 @date 2022年7月6日 下午8:31:54 @throws
	 */
	public void queryALLBeltWeight(Map<String, Object> map) {

		List<String> weeks = DateHelper.getWeekDays(DateUtil.getDate());
		String startTime = weeks.get(0);
		String endTime = weeks.get(weeks.size()-1);
		String day = DateUtil.getDate();
		// 获取两个日期之间所有的日期
		List<String> days = DateHelper.getDays(startTime, endTime);
		//工作面信息
		List<LbssWorking> workingList = ConfigBean.workingList;
		double[][] totalWeightArr = new double[workingList.size()][days.size()];
		int index = 0;
		for (LbssWorking working : workingList) {
			//按照工作面和时间进行分组查询一周每日产量
			List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
			LbssSumWeightDay sumWeightDay = new LbssSumWeightDay();

			// 查询每周的煤量信息
			for (int i = 0; i < days.size(); i++) {
				String time = days.get(i);
				//获取上一天日期
				String lastTime = DateHelper.getLastDay(time, 1);
				sumWeightDay.setStartTime(lastTime + " " + ConfigBean.startTime);
				sumWeightDay.setEndTime(time + " " + ConfigBean.startTime);
				sumWeightDay.setBelt_name(working.getBelt_name());
				LbssSumWeightDay sumWeightDay2 = lbssSumWeightDayMapper.queryDayWeightInfoByTime(sumWeightDay);
				if (sumWeightDay2 != null){

					sumWeightDay2.setBelt_name(working.getBelt_name());
					sumWeightDay2.setDay(lastTime);
					beltList.add(sumWeightDay2);
				}
				if (day.compareTo(time)==0){
					Map<String, Object> weightMap = (Map<String, Object>) redisUtil.get(working.getBelt_name());
					String day_weight = "0.0";
					if (weightMap != null ){
						day_weight = (String) weightMap.get("day_weight");
					}
					LbssSumWeightDay sumWeightDay3 = new LbssSumWeightDay();
					sumWeightDay3.setBelt_name(working.getBelt_name());
					sumWeightDay3.setDay(time);
					sumWeightDay3.setTotalWeight(Double.valueOf(day_weight));
					beltList.add(sumWeightDay3);
				}
			}
			// 汇总每周的过皮带信息
			double[] weightArr = getDayWeight(beltList, days);
//			map.put("totalWeight"+Day, weightList);
			totalWeightArr[index] = weightArr;
			index++;
		}
		
		map.put("totalWeightArr", totalWeightArr);
		map.put("days", days);

	}

	/***
	 * 
	 * @author 张宁
	 * @Description: 查询各皮带详细信息
	 * @return Map<String,Object>    返回类型
	 * @date 2022年7月8日 上午9:44:25
	 * @throws
	 */
	public Map<String, Object> queryALLBeltWeightDetail() {
		List<LbssWeightDTO> list = new ArrayList<LbssWeightDTO>();
		Map<String,String> weightMap = new HashMap<String,String>();
		List<String>  strList = new ArrayList<String>();
		String times = "";
		// 从opc获取各皮带数据信息
		JSONObject info  = OPCUAUtil.readNodesToOPC();
		if(info.get("data") != null){
			String data = info.get("data").toString();
			JSONObject json1 = JSONObject.parseObject(data);
			String rows = json1.get("rows").toString();
			JSONArray ar = JSONArray.parseArray(rows);
			Double nig_weight = 0.0;
			Double mor_weight = 0.0;
			Double aft_weight = 0.0;
			Double day_weight = 0.0;
			Double month_weight = 0.0;
			Double year_weight = 0.0;
			for(int i=0;i<ar.size();i++){

				JSONObject json2 = JSONObject.parseObject(JSONObject.toJSONString(ar.get(i)));
				//工作面信息
				Set<String> beltNames = ConfigBean.beltsMap.keySet();
			
				for (String beltName : beltNames) {
					if(json2.get(beltName) == null){
						continue;
					}
					String working_area = ConfigBean.beltsMap.get(beltName).getWorking_area();
					String msg = json2.get(beltName).toString();
					JSONObject json3 = JSONObject.parseObject(msg);
					String ip = DictionariesHelper.getDicStringValueByCode(working_area);
					//json对象转实体类
					LbssWeightDTO weight = new LbssWeightDTO(
							ip,
							working_area,
							beltName,
							Double.valueOf(json3.get("nig_weight")==null?"0.0":json3.get("nig_weight").toString()),
							Double.valueOf(json3.get("mor_weight")==null?"0.0":json3.get("mor_weight").toString()),
							Double.valueOf(json3.get("aft_weight")==null?"0.0":json3.get("aft_weight").toString()),
							Double.valueOf(json3.get("weight")==null?"0.0":json3.get("weight").toString()),
							Double.valueOf(json3.get("hour_weight")==null?"0.0":json3.get("hour_weight").toString()),
							Double.valueOf(json3.get("speed")==null?"0.0":json3.get("speed").toString()),
							Double.valueOf(json3.get("density")==null?"0.0":json3.get("density").toString()),
							Double.valueOf(json3.get("gangueRatio")==null?"0.0":json3.get("gangueRatio").toString()),
							Double.valueOf(json3.get("day_weight")==null?"0.0":json3.get("day_weight").toString()),
							Double.valueOf(json3.get("month_weight")==null?"0.0":json3.get("month_weight").toString()),
							Double.valueOf(json3.get("year_weight")==null?"0.0":json3.get("year_weight").toString()),
							json3.get("status")==null?"0":json3.get("status").toString()

					);
					//瞬时重量
					weightMap.put(beltName,json3.get("weightList")==null?"0.0":json3.get("weightList").toString());
					if ("西上仓".equals(beltName)){
						times = json3.get("timeList")==null?"":json3.get("timeList").toString();
					}
					if (StringUtils.isEmpty(times)){
						if ("一采主皮".equals(beltName)){
							times = json3.get("timeList")==null?"":json3.get("timeList").toString();
						}
					}
					ConfigBean.belt_hourMap.put(beltName, weight.getHourWeight());
					list.add(weight);
					//汇总
					nig_weight += weight.getNig_weight();
					mor_weight += weight.getMor_weight();
					aft_weight += weight.getAft_weight();
					day_weight += weight.getDay_weight();
					month_weight += weight.getMonth_weight();
					year_weight += weight.getYear_weight();
				}
			}

		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", list);

		List<String> timeList = new ArrayList<String>();
    	if(StringUtils.isNotEmpty(times)){
    		String[] time= times.split(",");
			timeList = Arrays.asList(time);
			map.put("timeList", timeList);
    	}
		Set<String> DaySet = weightMap.keySet();
		String[][] realWeightArr = new String[DaySet.size()][timeList.size()];
		int index = 0;
		for (String Day : DaySet) {
			String msg = weightMap.get(Day);
			String[] weights= msg.split(",");
			realWeightArr[index] = weights;
			index++;
		}
		map.put("realWeightArr", realWeightArr);
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


	/**
	 * 查询每班次煤量信息
	 */
	@Override
	public Map<String, Object> queryShiftWeightInfo(String beginTime, String endTime) {

		// 获取班次时间
		List<LbssShift> coalShiftList = lbssShiftMapper.selectLbssShiftList(null);
		List<LbssSumWeightHour> nigList = new ArrayList<LbssSumWeightHour>();
		List<LbssSumWeightHour> morList = new ArrayList<LbssSumWeightHour>();
		List<LbssSumWeightHour> aftList = new ArrayList<LbssSumWeightHour>();
		List<String> days = new ArrayList<String>();
		LbssShift shift =coalShiftList.get(0);
		if("00:00:00".equals(shift.getBegin())){
			for (LbssShift coalShift : coalShiftList) {
				String start = coalShift.getBegin();
				String end = coalShift.getEnd();
				Integer id = coalShift.getId();
				// 查询皮带班次信息
				if (id == 1) {
					nigList = lbssSumWeightHourMapper.queryShiftWeightInfo2(beginTime + " " + start,beginTime + " " + end);
				} else if (id == 2) {
					// 计算早班产量信息
					morList =	lbssSumWeightHourMapper.queryShiftWeightInfo2(beginTime + " " + start,beginTime + " " + end);
				} else {
					// 计算中班产量信息
					aftList = lbssSumWeightHourMapper.queryShiftWeightInfo2(beginTime + " " + start,endTime + " " + end);
				}
				days.add(coalShift.getName());
			}
		}else{
			//获取上一天日期
			String lastTime = DateHelper.getLastDay(beginTime, 1);
			for (LbssShift coalShift : coalShiftList) {
				String start = coalShift.getBegin();
				String end = coalShift.getEnd();
				Integer id = coalShift.getId();
				// 查询皮带班次信息
				if (id == 1) {
					nigList = lbssSumWeightHourMapper.queryShiftWeightInfo2(lastTime + " " + start,beginTime + " " + end);
				} else if (id == 2) {
					// 计算早班产量信息
					morList =	lbssSumWeightHourMapper.queryShiftWeightInfo2(beginTime + " " + start,beginTime + " " + end);
				} else {
					// 计算中班产量信息
					aftList = lbssSumWeightHourMapper.queryShiftWeightInfo2(beginTime + " " + start,beginTime + " " + end);
				}
				days.add(coalShift.getName());
			}
		}


		// 班次信息汇总
		Map<String, Object> resultMap = getShiftWeightInfo(nigList,morList,aftList);
		resultMap.put("days", days);
		return resultMap;
	}

	/**
	 * 查询每周的煤量信息
	 */
	@Override
	public Map<String, Object> queryDayWeightInfo(String startTime, String endTime) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取两个日期之间所有的日期
		List<String> days = DateHelper.getDays(startTime, endTime);
		//工作面信息
		List<LbssWorking> workings = ConfigBean.workingList;
		double[][] totalWeightArr = new double[workings.size()][];
		int index = 0;
		//按照工作面和时间进行分组查询一月每日产量
		for (LbssWorking working : workings) {
			List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
			LbssSumWeightDay lbssSumWeightDay = new LbssSumWeightDay();
			lbssSumWeightDay.setStartTime(startTime + " " + ConfigBean.startTime);
			lbssSumWeightDay.setEndTime(endTime + " " + ConfigBean.startTime);
			lbssSumWeightDay.setBelt_name(working.getBelt_name());
			// 查询每周的煤量信息
			beltList = lbssSumWeightDayMapper.findDayWeightInfoByTime(lbssSumWeightDay);

			// 汇总每月的过皮带信息
			double[] weightArr = getDayWeight(beltList, days);
			totalWeightArr[index] = weightArr;
			index++;
//			resultMap.put("totalWeight"+area, weightList);
		}
		resultMap.put("totalWeightArr", totalWeightArr);
		resultMap.put("days", days);
		return resultMap;
	}

	/**
	 * 查询每月的煤量信息
	 */
	@Override
	public Map<String, Object> queryMonthWeightInfo(String startTime, String endTime) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取两个日期之间所有的日期
		List<String> days = DateHelper.getDays(startTime, endTime);
		//工作面信息
		List<LbssWorking> workings = ConfigBean.workingList;
		double[][] totalWeightArr = new double[workings.size()][];
		int index = 0;
		//按照工作面和时间进行分组查询一月每日产量
		for (LbssWorking working : workings) {
			List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
			LbssSumWeightDay sumWeightArea = new LbssSumWeightDay();
			sumWeightArea.setStartTime(startTime + " " + ConfigBean.startTime);
			sumWeightArea.setEndTime(endTime + " " + ConfigBean.startTime);
			sumWeightArea.setBelt_name(working.getBelt_name());
			// 查询每周的煤量信息
			beltList = lbssSumWeightDayMapper.findDayWeightInfoByTime(sumWeightArea);
			// 汇总每月的过皮带信息
			double[] weightArr = getDayWeight(beltList, days);
			totalWeightArr[index] = weightArr;
			index++;
//			resultMap.put("totalWeight"+area, weightList);
		}
		resultMap.put("totalWeightArr", totalWeightArr);
		//月时间格式YYYY-MM
		List<String> timeList = new ArrayList<String>();
		for (int i = 0; i < days.size(); i++) {
			String time = days.get(i);
			time = time.substring(5, time.length());
			timeList.add(time);
		}
		resultMap.put("days", timeList);
		return resultMap;
	}

	/**
	 * 查询每年的煤量信息
	 */
	@Override
	public Map<String, Object> queryYearWeightInfo(String startTime, String endTime) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		// 获取两个日期之间所有的日期
		List<String> days = DateHelper.getMonthDays(startTime, endTime);
		//工作面信息
		List<LbssWorking> workings = ConfigBean.workingList;
		double[][] totalWeightArr = new double[workings.size()][];
		int index = 0;
		//按照工作面和时间进行分组查询一年每月产量
		for (LbssWorking working : workings) {
			List<LbssSumWeightDay> beltList = new ArrayList<LbssSumWeightDay>();
			LbssSumWeightDay sumWeightArea = new LbssSumWeightDay();
			sumWeightArea.setStartTime(startTime + " " + ConfigBean.startTime);
			sumWeightArea.setEndTime(endTime + " " + ConfigBean.startTime);
			sumWeightArea.setBelt_name(working.getBelt_name());
			// 查询每周的煤量信息
			beltList = lbssSumWeightDayMapper.findMonthWeightInfoByTime(sumWeightArea);
			// 汇总每月的过皮带信息
			double[] weightArr = getDayWeight(beltList, days);
			totalWeightArr[index] = weightArr;
			index++;
		}
		resultMap.put("totalWeightArr", totalWeightArr);
		resultMap.put("days", days);
		return resultMap;
	}
}
