package com.bjlthy.lbss.dataComm.socket.processor.belt;

import com.alibaba.fastjson.JSON;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.mapper.LbssARMConfigMapper;
import com.bjlthy.lbss.config.errorcode.domain.LbssError;
import com.bjlthy.lbss.config.errorcode.service.ILbssErrorService;
import com.bjlthy.lbss.data.weight.domain.LbssWeight;
import com.bjlthy.lbss.data.weight.mapper.LbssWeightMapper;
import com.bjlthy.lbss.dataComm.redis.RedisTopic;
import com.bjlthy.lbss.dataComm.socket.processor.config.NanoConfigProcessor;
import com.bjlthy.lbss.dataComm.socket.webSocket.BeltStateWebSocket;
import com.bjlthy.lbss.tool.*;
import com.mysql.cj.protocol.MessageListener;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class BeltRealtimeDataSave  {

	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("BeltRealtimeDataProcessor", "BeltRealtimeDataSave");
	
	public static LbssWeightMapper weightMapper = SpringUtils.getBean(LbssWeightMapper.class);
	//历史速度
	public static Double hisspeed = 0.0;
	//记录异常时间
	public static String recordTime;
	//数据订阅失败
	public static String dyError = "0";
	//判斷連接是否斷開
	public static String code = "1";


	/**
	 * 保存皮带数据
	 * 
	 * @param data
	 */
	public static void saveBeltData(String data,String ip) {

		String belt_name = ConfigBean.beltsIPMap.get(ip);
		if(StringUtils.isEmpty(belt_name)){
			return;
		}
		// 保存异常消息
		String[] msgs = data.split(" ");
		String[] weights = msgs[1].split(",");
		String info = msgs[1].substring(0, msgs[1].length() - 3);
		Double speed = Double.valueOf(weights[6]);
		// 速度传感器状态
		Integer speed_status = Integer.valueOf(weights[10]);
		// 重量传感器状态
		Integer weight_status = Integer.valueOf(weights[11]);
		/** ------------------传感器状态信息更新 start-------------------- */
		if (speed_status != 0 || weight_status != 0) {

			if (speed_status > 0 && weight_status == 0) {
				// 速度传感器状态变更
				if (speed_status > 0 && speed_status < 6) {
					speed_status = 1;
				} else if (speed_status == 6) {
					speed_status = 2;
				}
				sendStateToWebBrowser(speed_status.toString(), "speed",belt_name);
			} else if (weight_status > 0 && speed_status == 0) {
				if(weight_status != 2){
					weight_status = 1;
				}
				sendStateToWebBrowser(weight_status.toString(), "weight",belt_name);
			} else if (speed_status > 0 && weight_status > 0) {
				sendStateToWebBrowser("1", "speed_weight",belt_name);
			}

			data = DateUtil.getDateTime() + " " + data;
			FileUtil.writeFile(PathUtil.errorPath +belt_name+"/"+ DateUtil.getDateHour() + "_error.txt", data);
		} else {
			code = "0";
			sendStateToWebBrowser(code, "socket",belt_name);
		}
		try {
			/** ------------------传感器状态信息更新 end-------------------- */
			// 订阅失败数据不进行保存
			if (info.contains("-1")) {
				FileUtil.writeFile(PathUtil.errorPath +belt_name+"/"+ DateUtil.getDateHour() + "_error.txt", data);
				checkData(belt_name);
				return;
			} else {
				dyError = "0";
			}
			// 过来累计重量为0的情况
			if (Double.valueOf(weights[1]) == 0.00) {
				FileUtil.writeFile(PathUtil.errorPath +belt_name+"/"+ DateUtil.getDateHour() + "_error.txt", data);
				return;
			}
			LbssWeight weight = new LbssWeight();
			weight.setTime(new Date());
			weight.setBelt_name(belt_name);
			/** 瞬时重量 */
			weight.setWeight(Double.valueOf(weights[0]));
			/** 累计重量 */
			weight.setTotalWeight(Double.valueOf(weights[1]));
			/** 瞬时体积 */
			weight.setVolume(Double.valueOf(weights[2]));
			/** 累计体积 */
			weight.setTotalVolume(Double.valueOf(weights[3]));
			/** 密度 */
			Double density = Double.valueOf(weights[4]);
			/** 含矸率 */
			Double gangueRatio = Double.valueOf(weights[5]);
			int show_control = DictionariesHelper.getDicIntegerValueByCode(belt_name+"-show_control");
			if(show_control == 1 && weight.getWeight() >0){

				Random r = new Random();
				gangueRatio =  (r.nextDouble() * 2.5 + 1)/100;
				density = (Math.random()*20 + 100)/100;
			}
			weight.setDensity(ParseUtils.formart(density.toString()));
			weight.setGangueRatio(ParseUtils.formart(gangueRatio.toString(), 4));
			/** 速度 */
			weight.setSpeed(speed < 0 ? 0 : speed);
			/** 早班产量 08-16 */
			weight.setMor_weight(Double.valueOf(weights[7]));
			/** 中班产量 16-00 */
			weight.setAft_weight(Double.valueOf(weights[8]));
			/** 晚班产量 00-08 */
			weight.setNig_weight(Double.valueOf(weights[9]));

			//按照工作面信息存储到对应的实时数据表中
			weightMapper.insertLbssWeight(weight);

		}catch (Exception e){
			log.error("saveBeltData,{}",e);
		}

		/** ------------------更新下位机速度模式 start-------------------- */
		Integer speed_flag = Integer.valueOf(weights[13]);
		Integer hisspeedstate = DictionariesHelper.getDicIntegerValueByCode(belt_name+"-SpeedSwitch");
		if (hisspeedstate != speed_flag) {
			updateArmConfig(weights[6], speed_flag.toString(),belt_name,hisspeedstate);
		}
		/** ------------------更新下位机速度模式 end---------------------- */
	}
	
	/***
	 * 
	 * @author 张宁
	 * @Description: 更新板子配置信息
	 * @param speed 当前速度
	 * @param type  切换类型
	 * @return void    返回类型
	 * @date 2023年2月9日 下午3:58:10
	 * @throws
	 */
	public static void updateArmConfig(String speed,String type,String belt_name ,Integer hisspeedstate){

		LbssARMConfigMapper armMapper = SpringUtils.getBean(LbssARMConfigMapper.class);
		//更新速度模拟开关
		LbssARMConfig arm1 = new LbssARMConfig();
		arm1.setBelt_name(belt_name);
		arm1.setCode("SpeedSwitch");
		List<LbssARMConfig> list1 = armMapper.selectLbssARMConfigList(arm1);
		if(list1.size() > 0){

			LbssARMConfig armConfig = list1.get(0);
			armConfig.setValue(type);
			armMapper.updateLbssARMConfig(armConfig);
			NanoConfigProcessor.log.info(belt_name+"下位机速度模式切换--->"+"原速度模式为："+hisspeedstate+",现在速度模式为："+type);
			//更新历史速度模拟开关
			DictionariesHelper.setDicIntegerValueByCode(belt_name+"-SpeedSwitch",Integer.valueOf(type));
			//更新速度模拟值
			Double s = Double.valueOf(speed);
			if("3".equals(type) && s>0.0){
				LbssARMConfig arm2 = new LbssARMConfig();
				arm2.setCode("speed");
				List<LbssARMConfig> list2 = armMapper.selectLbssARMConfigList(arm2);
				LbssARMConfig armConfig2 = list2.get(0);
				armConfig2.setValue(speed);
				armMapper.updateLbssARMConfig(armConfig2);
			}
		}

	}
	
	/**
     * 
     * @author 张宁
     * @Description: 检查数据是否异常
     * @return void    返回类型
     * @date 2022年5月18日 上午11:48:53
     * @throws
     */
    public static void checkData(String belt_name){
    	//订阅失败标识
    	if("0".equals(dyError)){
    		Calendar calendar = new GregorianCalendar();
    		calendar.add(calendar.MINUTE, 10);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		//记录10分钟后的时间
    		recordTime = sdf.format(calendar.getTime());
    		//标识更改为1，订阅失败
    		dyError = "1";
		}else{
			String currentTime = DateUtil.getDateTime();
			//判断订阅失败是否超过10分钟
			if(currentTime.compareTo(recordTime)>0){
				sendStateToWebBrowser("2","socket",belt_name);
			}
		}
    }
	/**
	 * @author 张宁
	 * @description 发送皮带机状态到web浏览器
	 * @date 2020年11月16日 下午4:12:17
	 */
	public static void sendStateToWebBrowser(String code,String type,String belt_name) {
		try {
			AjaxResult ajaxResult = AjaxResult.success();
			ajaxResult.put("code", code);
			LbssError bopsError = new LbssError();
			bopsError.setType("下位机设备");
			bopsError.setSolution(type);
			// 查询异常消息
			ILbssErrorService bean = SpringUtils.getBean(ILbssErrorService.class);
			List<LbssError> bopsErrors = bean.selectCoalErrorList(bopsError);
			// 查找异常码是否在bopsErrors集合中
			List<LbssError> collect = bopsErrors.stream().filter(a -> a.getCode().equals(code))
					.collect(Collectors.toList());
			ajaxResult.put("msg", collect.size() > 0 ? collect.get(0).getTips() : "正常");
			ajaxResult.put("belt_name", belt_name);
			//获取工作面信息
			String working_area = ConfigBean.beltsMap.get(belt_name).getWorking_area();
			//发送至PC端显示
			BeltStateWebSocket.sendMessageToClient(belt_name,JSON.toJSONString(ajaxResult));

		} catch (Exception e) {
			log.warn("sendStateToWebBrowser - 发送皮带机状态到web浏览器失败,{}",e);
		}
	}



}
