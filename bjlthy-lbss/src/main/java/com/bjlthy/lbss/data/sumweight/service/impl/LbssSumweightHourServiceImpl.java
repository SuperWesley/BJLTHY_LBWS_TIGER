package com.bjlthy.lbss.data.sumweight.service.impl;

import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.data.sumweight.service.ILbssSumWeightHourService;
import com.bjlthy.lbss.tool.DataHelper;
import com.bjlthy.lbss.tool.DateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 日累计重量Service业务层处理
 * @date 2021年2月6日 下午12:00:38
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */

@Service
public class LbssSumweightHourServiceImpl implements ILbssSumWeightHourService
{
    @Autowired
    private LbssSumWeightHourMapper lbssSumWeightHourMapper;

	@Autowired
	private LbssSumWeightDayMapper lbssSumWeightDayMapper;

    @Autowired
    private LbssShiftMapper lbssShiftMapper;
    /**
     * 查询日累计重量
     * 
     * @param id 日累计重量ID
     * @return 日累计重量
     */
    @Override
    public LbssSumWeightHour selectCoalSumWeightById(Integer id)
    {
        return lbssSumWeightHourMapper.selectLbssSumWeightById(id);
    }


    /**
     * 新增日累计重量
     * 
     * @param lbssSumWeight 日累计重量
     * @return 结果
     */
    @Override
    public int insertCoalSumWeight(LbssSumWeightHour lbssSumWeight)
    {
        return lbssSumWeightHourMapper.insertLbssSumweight(lbssSumWeight);
    }

	 /**
     * 查询每班次煤量信息
     */
	@Override
		public Map<String,Object> queryShiftWeightInfo(String beginTime, String endTime,String belt_name) {
		
		List<LbssSumWeightHour> coalWeightList = new ArrayList<LbssSumWeightHour>();
		//获取班次时间
		List<LbssShift> coalShiftList=lbssShiftMapper.selectLbssShiftList(null);
		LbssShift shift =coalShiftList.get(0);
		if("00:00:00".equals(shift.getBegin())){
			for (LbssShift coalShift : coalShiftList) {
				String start=coalShift.getBegin();
				String end=coalShift.getEnd();
				Integer id=coalShift.getId();
				//查询皮带班次信息
				if(id ==1){
					//计算晚班产量信息
					LbssSumWeightHour ngtWeight=lbssSumWeightHourMapper.queryShiftWeightInfo(belt_name,beginTime+" "+start,beginTime+" "+end);
					if(ngtWeight == null){
						ngtWeight = new LbssSumWeightHour();
						ngtWeight.setTotalWeight(0.0);
						ngtWeight.setTotalVolume(0.0);
					}
					ngtWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(ngtWeight);
				}else if(id ==2){
					//计算早班产量信息
					LbssSumWeightHour morWeight = lbssSumWeightHourMapper.queryShiftWeightInfo(belt_name,beginTime+" "+start,beginTime+" "+end);
					if(morWeight == null){
						morWeight = new LbssSumWeightHour();
						morWeight.setTotalWeight(0.0);
						morWeight.setTotalVolume(0.0);
					}
					morWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(morWeight);
				}else{
					//计算中班产量信息
					LbssSumWeightHour aftWeight = lbssSumWeightHourMapper.queryShiftWeightInfo(belt_name,beginTime+" "+start,endTime+" "+end);
					if(aftWeight == null){
						aftWeight = new LbssSumWeightHour();
						aftWeight.setTotalWeight(0.0);
						aftWeight.setTotalVolume(0.0);
					}
					aftWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(aftWeight);
				}
			}	
		}else{
			 //获取上一天日期 
	        String lastTime = DateHelper.getLastDay(beginTime, 1);
			for (LbssShift coalShift : coalShiftList) {
				String start=coalShift.getBegin();
				String end=coalShift.getEnd();
				Integer id=coalShift.getId();
				//查询皮带班次信息
				if(id ==1){
					//计算晚班产量信息
					LbssSumWeightHour ngtWeight=lbssSumWeightHourMapper.queryShiftWeightInfo(lastTime+" "+start,beginTime+" "+end, belt_name);
					if(ngtWeight == null){
						ngtWeight = new LbssSumWeightHour();
						ngtWeight.setTotalWeight(0.0);
						ngtWeight.setTotalVolume(0.0);
					}
					ngtWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(ngtWeight);
				}else if(id ==2){
					//计算早班产量信息
					LbssSumWeightHour morWeight = lbssSumWeightHourMapper.queryShiftWeightInfo(beginTime+" "+start,beginTime+" "+end, belt_name);
					if(morWeight == null){
						morWeight = new LbssSumWeightHour();
						morWeight.setTotalWeight(0.0);
						morWeight.setTotalVolume(0.0);
					}
					morWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(morWeight);
				}else{
					//计算中班产量信息
					LbssSumWeightHour aftWeight = lbssSumWeightHourMapper.queryShiftWeightInfo(beginTime+" "+start,beginTime+" "+end, belt_name);
					if(aftWeight == null){
						aftWeight = new LbssSumWeightHour();
						aftWeight.setTotalWeight(0.0);
						aftWeight.setTotalVolume(0.0);
					}
					aftWeight.setRemark(coalShift.getRemark());
					coalWeightList.add(aftWeight);
				}
			}	
		}
		
		
		//班次信息汇总
		Map<String,Object> resultMap = getShiftWeightInfo(coalWeightList);
    	
		return resultMap;
	}

	
	/**
     * 统计班次的过煤量信息
     * @param list
     */
    public Map<String, Object> getShiftWeightInfo(List<LbssSumWeightHour> list){
    	
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	Map<String, String> weightMap = new HashMap<String, String>();
    	//过煤重量list
    	List<String> weightList = new ArrayList<String>();
    	//过煤体积list
    	List<String> volumeList = new ArrayList<String>();
    	//松散密度list
    	List<String> densityList = new ArrayList<String>();
    	//含矸率list
    	List<String> gangueRatioList = new ArrayList<String>();
    	//时间list
    	List<String> timeList = new ArrayList<String>();
    	//重量
    	Double totalWeight = 0.00;
    	//体积
    	Double totalVolume = 0.00;
		String belt_name = "";
		String day = "";
    	for (int i = 0; i < list.size(); i++) {
    		LbssSumWeightHour coalSumWeight = list.get(i);
			belt_name = coalSumWeight.getBelt_name();
			day = coalSumWeight.getDay();
    		//班重量
    		Double weight = coalSumWeight.getTotalWeight().doubleValue();
    		//格式化，保留2位小数
    		weightList.add(String.format("%.2f", weight));
    		weightMap.put(coalSumWeight.getRemark(), String.format("%.2f", weight));
    		//班体积
    		Double volume = coalSumWeight.getTotalVolume().doubleValue();
    		//班松散密度
    		Double b_density = volume==0.00?0.00:weight/volume;
    		densityList.add(String.format("%.2f", b_density));
    		//班含矸率
    		Double b_gangueRatio = 0.00;
    		//计算含矸率
    		if(weight.intValue()>0 && volume.intValue()>0){
    			b_gangueRatio = DataHelper.getGangueRatio(weight,volume)*100;
    		}
    		volumeList.add(String.format("%.2f", volume));
    		gangueRatioList.add(String.format("%.2f", b_gangueRatio));
    		//时间
    		String time = coalSumWeight.getRemark();
    		timeList.add(time);

			totalWeight += weight;
			totalVolume += volume;
		}
    	//进行松散密度和含矸率校验
		Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(totalWeight,totalVolume);
		//存入map中
    	/**-----------------------列表显示项---------------------------*/
    	//累计重量
    	resultMap.put("totalWeight", String.format("%.2f", totalWeight));
    	//累计体积
    	resultMap.put("totalVolume", String.format("%.2f", totalVolume));
    	//松散密度
    	resultMap.put("density", map.get("density"));
    	//含矸率
    	resultMap.put("gangueRatio", map.get("gangueRatio"));
    	//早班过煤量
    	resultMap.put("mor_weight", weightMap.get("早班"));
    	//中班过煤量
    	resultMap.put("aft_weight", weightMap.get("中班"));
    	//晚班过煤量
    	resultMap.put("nig_weight", weightMap.get("晚班"));
		/**-----------------------echarts图表显示项---------------------------*/
    	//过煤量集合
    	resultMap.put("weightList", weightList);
    	//过煤体积集合
    	resultMap.put("volumeList", volumeList);
    	//过煤含矸率集合
    	resultMap.put("gangueRatioList", gangueRatioList);
    	resultMap.put("densityList", densityList);
    	resultMap.put("timeList", timeList);
    	
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
	
}
