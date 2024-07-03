package com.bjlthy.lbss.data.weight.service.impl;

import com.bjlthy.common.utils.bean.BeanUtils;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightHour;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightHourMapper;
import com.bjlthy.lbss.data.weight.domain.LbssWeight;
import com.bjlthy.lbss.data.weight.mapper.LbssWeightMapper;
import com.bjlthy.lbss.data.weight.service.ILbssJobService;
import com.bjlthy.lbss.ftp.Ftp;
import com.bjlthy.lbss.ftp.FtpClient;
import com.bjlthy.lbss.tool.*;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class LbssJobServiceImpl implements ILbssJobService {

	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("LbssJobServiceImpl", "LbssJobServiceImpl");

	@Autowired
	private LbssWeightMapper weightMapper;

    @Autowired
    private LbssSumWeightHourMapper lbssSumWeightHourMapper;

	@Autowired
	private LbssSumWeightDayMapper lbssSumWeightDayMapper;

	/**
	 *
	 * @author 张宁
	 * @description 定时每小时查询皮带机信息
	 * @date 2021年3月1日 下午9:54:52
	 */
	public void sumHourBeltInfo() {

		Calendar calendar = new GregorianCalendar();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		//当前小时
		String currentHour= sdf.format(calendar.getTime())+ ":00:00";
		int currentYear = Integer.parseInt(currentHour.substring(0,4));
		//上一小时
		String lastHour = DateHelper.getLastHour(currentHour,1);
		int lastYear = Integer.parseInt(currentHour.substring(0,4));
		//如果不是同一年
		if(lastYear != currentYear){
			currentHour = currentHour.substring(0,13)+":59:59";
		}
		//各皮带秤信息
		Set<String> beltNames = ConfigBean.beltsMap.keySet();
		for (String beltName : beltNames) {
			LbssWeight lbssWeight = weightMapper.selectSumWeight(beltName,lastHour,currentHour);

			//查询出上一小时的过煤信息
			Double maxweight_sensor = DictionariesHelper.getDicDoubleValueByCode(beltName+"-maxweight_sensor");
			if(lbssWeight !=null && lbssWeight.getTotalWeight() >maxweight_sensor){
				lbssWeight.setTotalWeight(maxweight_sensor);
				lbssWeight.setTotalVolume(maxweight_sensor/1.35);
			}
			//保存皮带数据
			saveHourBeltInfo(lbssWeight,lastHour,beltName);
			
		}

	}

	/**
	 *
	 * @author 张宁
	 * @description 保存每小时皮带信息
	 * @param lbssWeight
	 * @date 2021年3月1日 下午10:04:03
	 */
	public void saveHourBeltInfo(LbssWeight lbssWeight,String lastHour,String beltName) {

		try {
			Double totalWeight = 0.0;
			Double totalVolume = 0.0;
			if(lbssWeight != null){
				totalWeight = lbssWeight.getTotalWeight();
				totalVolume = lbssWeight.getTotalVolume();
			}
			//计算密度和含矸率
			Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(totalWeight,totalVolume);
			//获取密度和含矸率
			Double density = map.get("density");
			Double gangueRatio = map.get("gangueRatio");

			//存入汇总数据表中
			LbssSumWeightHour sumweight = new LbssSumWeightHour();
			//时间
			sumweight.setDay(lastHour);
			//重量
			sumweight.setTotalWeight(totalWeight);
			//体积
			sumweight.setTotalVolume(totalVolume);
			//密度
			sumweight.setDensity(density);
			//含矸率
			sumweight.setGangueRatio(gangueRatio);
			//星期
			String week = DateUtil.dayForWeek(lastHour);
			Double density_min = DictionariesHelper.getDicDoubleValueByCode("density_min");
			Double density_max = DictionariesHelper.getDicDoubleValueByCode("density_max");
			//密度超阈值判断
			if(density == density_max || density == density_min){
				sumweight.setRemark("该时间段密度超限，统计的重量或者体积存在偏差");
			}else{
				sumweight.setRemark("定时任务插入");
			}
			sumweight.setWeek(week);
			sumweight.setBelt_name(beltName);
			LbssSumWeightHour lbssSumWeight = lbssSumWeightHourMapper.queryDetailDataLikeHour(beltName,lastHour);
			if (lbssSumWeight ==null){
				lbssSumWeightHourMapper.insertLbssSumweight(sumweight);
			}else{
				Integer id =  lbssSumWeight.getId();
				BeanUtils.copyProperties(lbssSumWeight, sumweight);
				sumweight.setId(id);
				lbssSumWeightHourMapper.updateLbssSumweight(sumweight);
			}
		}catch (Exception e){
			log.error("saveHourBeltInfo-->",e);
		}

	}

	/**
	 *
	 * @author 张宁
	 * @description 定时每天查询皮带机信息
	 * @date 2021年3月1日 下午9:54:52
	 */
	public void sumDayBeltInfo() {
		//当前小时
		String currentDay= DateUtil.getDate();
		String day = DateHelper.getLastDay(currentDay,1);
		currentDay = currentDay+" "+ConfigBean.startTime;
		String lastDay = day+" "+ConfigBean.startTime;
		//各皮带秤信息
		Set<String> beltNames = ConfigBean.beltsMap.keySet();
		for (String beltName : beltNames) {
			//查询出上一天的过煤信息
			LbssSumWeightHour lbssSumWeight = lbssSumWeightHourMapper.queryWeightInfo(beltName,lastDay, currentDay);
			//保存皮带数据
			installDayBeltInfo(lbssSumWeight,beltName,day);
		}

	}

	/**
	 *
	 * @author 张宁
	 * @description 保存皮带信息
	 * @param lbssSumWeight
	 * @date 2021年3月1日 下午10:04:03
	 */
	public void installDayBeltInfo(LbssSumWeightHour lbssSumWeight,String beltName, String lastDay){
		try {
			Double totalWeight = 0.0;
			Double totalVolume = 0.0;
			if(lbssSumWeight != null){
				totalWeight = lbssSumWeight.getTotalWeight();
				totalVolume = lbssSumWeight.getTotalVolume();
			}
			//计算密度和含矸率
			Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(totalWeight,totalVolume);
			//获取密度和含矸率
			Double density = map.get("density");
			Double gangueRatio = map.get("gangueRatio");

			//存入汇总数据表中
			LbssSumWeightDay sumweight = new LbssSumWeightDay();
			//时间
			sumweight.setDay(lastDay);
			//重量
			sumweight.setTotalWeight(totalWeight);
			//体积
			sumweight.setTotalVolume(totalVolume);
			//密度
			sumweight.setDensity(density);
			//含矸率
			sumweight.setGangueRatio(gangueRatio);
			//星期
			String week = DateUtil.dayForWeek(lastDay);
			Double density_min = DictionariesHelper.getDicDoubleValueByCode("density_min");
			Double density_max = DictionariesHelper.getDicDoubleValueByCode("density_max");
			//密度超阈值判断
			if(density == density_max || density == density_min){
				sumweight.setRemark("该时间段密度超限，统计的重量或者体积存在偏差");
			}else{
				sumweight.setRemark("定时任务插入");
			}
			sumweight.setWeek(week);
			sumweight.setBelt_name(beltName);

			LbssSumWeightDay lbssSumWeightDay = lbssSumWeightDayMapper.queryDayWeightInfo(beltName,lastDay);
			if(lbssSumWeightDay != null){
				Integer id = lbssSumWeightDay.getId();
				BeanUtils.copyProperties(sumweight, lbssSumWeightDay);
				lbssSumWeightDay.setId(id);
				lbssSumWeightDayMapper.updateLbssSumWeightDay(lbssSumWeightDay);
			}else{
				lbssSumWeightDayMapper.insertLbssSumWeightDay(sumweight);
			}
		}catch (Exception e){
			log.error("installDayBeltInfo-->",e);
		}

	}

	/**
	 * 通过FTP服务下载文件
	 */
	public void downloadFileToFTP() {
		//各皮带秤信息
		Set<String> beltNames = ConfigBean.beltsMap.keySet();
		try {
			for (String beltName : beltNames) {
				String ip = DictionariesHelper.getDicStringValueByCode(beltName + "-Nano_IP");
				Ftp ftp=new Ftp();
				ftp.setIpAddr(ip);
				ftp.setUserName("ftp");
				ftp.setPwd("bjlthy123");
				if (!FtpClient.connectFtp(ftp)){
					continue;
				}
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH");
				String beltFilePath = PathUtil.beltHis+beltName+"/"+sdf.format(new Date())+"/";
				String EWSFilePath = PathUtil.path+"/Belt/"+beltName+PathUtil.elecsPath;
				String LidarFilePath = PathUtil.LidarHis+beltName+"/"+sdf.format(new Date())+"/";
				String zeroFilePath = PathUtil.path+"/ZERO/"+beltName;
				//下载皮带秤ftp文件
				FtpClient.startDown(ftp, beltFilePath,  "/belt");
				//下载电子秤ftp文件
				FtpClient.startDownEWS(ftp, EWSFilePath,  "/EWS");
				//下载雷达ftp文件
				FtpClient.startDown(ftp, LidarFilePath,  "/Lidar");
				//下载调零文件ftp文件
				FtpClient.startDown(ftp, zeroFilePath+"/ZeroFile/",  "/Zero");
				//下载调零值ftp文件
				FtpClient.startDown(ftp, zeroFilePath+"/ZeroValue/",  "/ZeroInfo");
				//读取文件更新数据库
				readFileUpdateDB(beltFilePath,beltName);
			}
		}catch (Exception e){
			log.error("downloadFileToFTP-->",e);
		}

	}

	/**
	 * 读取文件更新数据库
	 */
	@Override
	public  void readFileUpdateDB(String filePath,String beltName){

		File file = new File(filePath);
		String[] files = file.list();
		try {
			if(files != null) {

				for (int i = 0; i < files.length; i++) {
					String path = files[i];
					if(path.contains("error")){
						continue;
					}
					File beltFile = new File(path);// Text文件
					String s = null;
					String day = "";
					/** ---------------------将FTP历史数据更新到数据表 ---------------------*/
					BufferedReader br = new BufferedReader(new FileReader(filePath+"\\"+beltFile));// 构造一个BufferedReader类来读取文件
					while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
						// 保存异常消息
						String[] msgs = s.trim().split(" ");
						String[] weights = msgs[3].split(",");
						LbssWeight weight = new LbssWeight();
						String time = msgs[0]+" "+msgs[1];
						day = time;
						DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date date = df.parse(time);
						weight.setTime(date);
						weight.setBelt_name(beltName);
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
						Double speed = Double.valueOf(weights[6]);
						weight.setDensity(ParseUtils.formart(density.toString()));
						weight.setGangueRatio(ParseUtils.formart(gangueRatio.toString(),4));
						/** 速度 */
						weight.setSpeed(speed<0?0:speed);
						/** 早班产量 08-16 */
						weight.setMor_weight(Double.valueOf(weights[7]));
						/** 中班产量 16-00 */
						weight.setAft_weight(Double.valueOf(weights[8]));
						/** 晚班产量 00-08 */
						weight.setNig_weight(Double.valueOf(weights[9]));
						weight.setRemark("新增FTP历史数据");

						weightMapper.insertLbssWeight(weight);
					}
					br.close();

					/** ---------------------重新更新皮带秤小时产量数据 ---------------------*/
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
					//当前小时
					String lastHour= day.substring(0,13)+":00:00";
					int last_year = Integer.valueOf(lastHour.substring(0,4));
					//下一小时
					String currentHour = DateHelper.getNextHour(lastHour,1);
					int next_year = Integer.valueOf(currentHour.substring(0,4));
					//如果不是同一年
					if(last_year != next_year){
						currentHour = lastHour.substring(0,13)+":59:59";
					}
					LbssWeight lbssWeight = new LbssWeight();
					lbssWeight = weightMapper.selectSumWeight(beltName,lastHour,currentHour);
					//查询出上一小时的过煤信息
					Double maxweight_sensor = DictionariesHelper.getDicDoubleValueByCode(beltName+"-maxweight_sensor");
					if(lbssWeight.getTotalWeight() >maxweight_sensor){
						lbssWeight.setTotalWeight(maxweight_sensor);
						lbssWeight.setTotalVolume(maxweight_sensor/1.35);
					}
					//保存皮带数据
					updateBeltInfoFromFile(lbssWeight,lastHour,beltName);
				}

			}
		} catch (Exception e) {
			log.error("readFileUpdateDB {}",e);
		}
	}

	/**
	 * 从下载的文件中
     * 更新皮带秤数据信息
	 * @param lbssWeight
	 * @param lastHour
	 * @param beltName
	 */
	public void updateBeltInfoFromFile(LbssWeight lbssWeight,String lastHour,String beltName) {
		try {
			LbssSumWeightHour lbssSumWeight = lbssSumWeightHourMapper.queryDetailDataLikeHour(beltName,lastHour);
			if(lbssSumWeight != null){
				//更新累计过煤重量
				lbssSumWeight.setTotalWeight(lbssWeight.getTotalWeight());
				//更新累计过煤体积
				lbssSumWeight.setTotalVolume(lbssWeight.getTotalVolume());
				//计算密度和含矸率
				Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(lbssSumWeight.getTotalWeight(),lbssSumWeight.getTotalVolume());
				//获取密度和含矸率
				Double density = map.get("density");
				Double gangueRatio = map.get("gangueRatio");
				lbssSumWeight.setDensity(density);
				lbssSumWeight.setGangueRatio(gangueRatio);
				//星期
				String week = DateUtil.dayForWeek(lastHour);
				lbssSumWeight.setWeek(week);
				lbssSumWeight.setRemark("更新FTP历史数据");
				//更新数据库
				lbssSumWeightHourMapper.updateLbssSumweight(lbssSumWeight);

				//将数据更新到工作面汇总表
				//查询出上一天的过煤信息
				String lastDay= lastHour.substring(0,10);
				//下一天
				String nextDay = DateHelper.getNextDay(lastDay,1);
				LbssSumWeightHour lbssSumWeightHour = lbssSumWeightHourMapper.queryWeightInfo(beltName,lastDay, nextDay);
				LbssSumWeightDay lbssSumWeightDay = lbssSumWeightDayMapper.queryDayWeightInfo(beltName,lastDay);
				if (lbssSumWeightDay!= null){
					lbssSumWeightDay.setTotalWeight(lbssSumWeightHour.getTotalWeight());
					lbssSumWeightDay.setTotalVolume(lbssSumWeightHour.getTotalVolume());
					lbssSumWeightDay.setRemark("更新FTP历史数据");
					lbssSumWeightDayMapper.updateLbssSumWeightDay(lbssSumWeightDay);
				}else {
					if (DateUtil.getDate().compareTo(lastDay)<=0){
						log.warn("FTP历史数据时间大于当前时间不进行日统计数据插入");
						return;
					}
					LbssSumWeightDay lbssSumWeightDay2 = new LbssSumWeightDay();
					BeanUtils.copyProperties(lbssSumWeightHour, lbssSumWeightDay2);
					lbssSumWeightDay2.setDay(lastDay);
					lbssSumWeightDay2.setBelt_name(beltName);
					//计算密度和含矸率
					Map<String,Double> dayMap = DataHelper.checkDensityAndGangueRatio(lbssSumWeightDay2.getTotalWeight(),lbssSumWeightDay2.getTotalVolume());
					//获取密度和含矸率
					Double density_day = dayMap.get("density");
					Double gangueRatio_day = dayMap.get("gangueRatio");
					lbssSumWeightDay2.setDensity(density_day);
					lbssSumWeightDay2.setGangueRatio(gangueRatio_day);
					//星期
					lbssSumWeightDay2.setWeek(week);
					lbssSumWeightDay2.setRemark("插入FTP历史数据");
					lbssSumWeightDayMapper.insertLbssSumWeightDay(lbssSumWeightDay2);
				}
			}else{
				String currTime = DateUtil.getDateTime().substring(0,13)+":00:00";
				if (currTime.compareTo(lastHour)<=0){
					log.warn("FTP历史数据时间大于当前时间不进行小时统计数据插入");
					return;
				}
				//不存在新增数据
				LbssSumWeightHour lbssSumWeight2 = new LbssSumWeightHour();
				lbssSumWeight2.setBelt_name(beltName);
				lbssSumWeight2.setDay(lastHour);
				Double maxweight_sensor = DictionariesHelper.getDicDoubleValueByCode(beltName+"-maxweight_sensor");
				lbssSumWeight2.setTotalWeight(lbssWeight.getTotalWeight()>maxweight_sensor?maxweight_sensor:lbssWeight.getTotalWeight());
				lbssSumWeight2.setTotalVolume(lbssWeight.getTotalVolume());
				//计算密度和含矸率
				Map<String,Double> map = DataHelper.checkDensityAndGangueRatio(lbssWeight.getTotalWeight(),lbssWeight.getTotalVolume());
				//获取密度和含矸率
				Double density = map.get("density");
				Double gangueRatio = map.get("gangueRatio");
				lbssSumWeight2.setDensity(density);
				lbssSumWeight2.setGangueRatio(gangueRatio);
				//星期
				String week = DateUtil.dayForWeek(lastHour);
				lbssSumWeight2.setWeek(week);
				lbssSumWeight2.setRemark("已新增FTP历史数据");
				//新增数据
				lbssSumWeightHourMapper.insertLbssSumweight(lbssSumWeight2);

				//将数据更新到工作面汇总表
				//查询出上一天的过煤信息
				String lastDay= lastHour.substring(0,10);
				//下一天
				String nextDay = DateHelper.getNextDay(lastDay,1);
				LbssSumWeightHour lbssSumWeightHour = lbssSumWeightHourMapper.queryWeightInfo(beltName,lastDay, nextDay);
				LbssSumWeightDay lbssSumWeightDay = lbssSumWeightDayMapper.queryDayWeightInfo(beltName,lastDay);
				if (lbssSumWeightDay!= null){
					lbssSumWeightDay.setTotalWeight(lbssSumWeightHour.getTotalWeight());
					lbssSumWeightDay.setTotalVolume(lbssSumWeightHour.getTotalVolume());
					lbssSumWeightDayMapper.updateLbssSumWeightDay(lbssSumWeightDay);
				}else {
					if (DateUtil.getDate().compareTo(lastDay)<=0){
						log.warn("FTP历史数据时间大于当前时间不进行日统计数据插入");
						return;
					}
					LbssSumWeightDay lbssSumWeightDay2 = new LbssSumWeightDay();
					BeanUtils.copyProperties(lbssSumWeightHour, lbssSumWeightDay2);
					lbssSumWeightDay2.setDay(lastDay);
					lbssSumWeightDay2.setBelt_name(beltName);
					//计算密度和含矸率
					Map<String,Double> map2 = DataHelper.checkDensityAndGangueRatio(lbssSumWeightDay2.getTotalWeight(),lbssSumWeightDay2.getTotalVolume());
					lbssSumWeightDay2.setDensity(map2.get("density"));
					lbssSumWeightDay2.setGangueRatio(map2.get("gangueRatio"));
					//星期
					lbssSumWeightDay2.setWeek(week);
					lbssSumWeightDay2.setRemark("已新增FTP历史数据");
					lbssSumWeightDayMapper.insertLbssSumWeightDay(lbssSumWeightDay2);
				}
			}
		}catch (Exception e){
			log.error("updateBeltInfoFromFile-->,{}",e);
		}

	}
}
