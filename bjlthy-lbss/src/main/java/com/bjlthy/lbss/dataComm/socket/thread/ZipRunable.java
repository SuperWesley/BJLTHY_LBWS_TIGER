package com.bjlthy.lbss.dataComm.socket.thread;

import com.alibaba.fastjson.JSONObject;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.data.sumweight.domain.LbssSumWeightDay;
import com.bjlthy.lbss.data.sumweight.mapper.LbssSumWeightDayMapper;
import com.bjlthy.lbss.tool.*;
import com.bjlthy.lbss.zip.ZipUtilParallelScatter;
import org.slf4j.Logger;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Set;

/**
 *
 * @version V1.0
 * @author 张宁
 * @description 压缩文件
 * @date 2020年11月16日 下午4:56:16
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ZipRunable implements Runnable{

	public ZipRunable(){

	}
	public LbssSumWeightDayMapper sumWeightDayMapper = SpringUtils.getBean(LbssSumWeightDayMapper.class);

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("SysJob","ZipRunable");

	@Override
	public void run() {
		Set<String> beltNames = ConfigBean.beltsMap.keySet();
		for (String beltName : beltNames) {
			String elecsPath = PathUtil.path+"/Belt/"+beltName+PathUtil.elecsPath;
			String currentDay = DateUtil.getDate();
			String yesterDay = DateHelper.getBeforDay(currentDay, 1);
			//数据对比
			dataCompare(elecsPath+yesterDay,beltName);
			String lastDay = DateHelper.getBeforDay(currentDay, 2);

			//压缩电子秤文件夹
			String beltPath = elecsPath+lastDay;
			File source_belt = new File(beltPath);
			//文件夹不存在不压缩文件
			if(source_belt.exists()){
				File target_belt = new File(beltPath+".zip");
				ZipUtilParallelScatter zipUtil_belt = new ZipUtilParallelScatter();
				zipUtil_belt.doZipFile(source_belt, target_belt);
				//删除原始文件夹
				FileUtil.deleteDir(beltPath);
			}
			List<File> beltList = FileUtil.getFileSort(elecsPath);
			// 暂时保留180天数据的文件
			if (beltList.size() > 180) {
				String path1 = beltList.get(0).toString();
				// 删除文件数
				DeleteFileRunable de = new DeleteFileRunable(path1);
				Thread td = new Thread(de);
				td.start();
			}

			//压缩雷达文件夹
			String bLidarPath = PathUtil.path+"/Belt/"+beltName+PathUtil.bLidarPath;
			String laserPath = bLidarPath+lastDay;
			File source_lidar = new File(laserPath);
			//文件夹不存在不压缩文件
			if(source_lidar.exists()){
				File target_lidar = new File(laserPath+".zip");
				ZipUtilParallelScatter zipUtil_lidar = new ZipUtilParallelScatter();
				zipUtil_lidar.doZipFile(source_lidar, target_lidar);
				//删除原始文件夹
				FileUtil.deleteDir(laserPath);
			}
			List<File> laserList = FileUtil.getFileSort(bLidarPath);
			// 暂时保留180天数据的文件
			if (laserList.size() > 180) {
				String path1 = laserList.get(0).toString();
				// 删除文件数
				DeleteFileRunable de = new DeleteFileRunable(path1);
				Thread td = new Thread(de);
				td.start();
			}
		}

	}

	/**
	 * 数据比较--比较电子秤数据和统计数据
	 */
	public void dataCompare(String path,String beltName){
		File file = new File(path);
		String[] files = file.list();
		if(files != null){
			try {
				//获取文件第一行数据
				String firstLine = getFileFirstLine(file + "\\" + files[0]);
				//获取文件最后一行数据
				String lastLine = getFileLastLine(file + "\\" + files[files.length-1]);
				//截取第一行数据中的累计重量和累计体积
				Double firstSumWeight = Double.valueOf(firstLine.split(",")[2]);
				String day = firstLine.substring(0,10);
				//截取最后一行数据中的累计重量和累计体积
				Double lastSumWeight = Double.valueOf(lastLine.split(",")[2]);
				//计算该时间段的重量
				Double wft = DictionariesHelper.getDicDoubleValueByCode(beltName+"-WTF");
				if (wft == null){
					wft = 0.0;
				}
				Double fileWeight = (lastSumWeight - firstSumWeight)*wft;

				//查询日统计产量
				LbssSumWeightDay lbssSumWeightDay = sumWeightDayMapper.queryDayWeightInfo(beltName, day);
				Double totalWeight = lbssSumWeightDay==null?0.0:lbssSumWeightDay.getTotalWeight();

				//偏差结果
				Double dif = fileWeight - totalWeight;
				JSONObject json = new JSONObject();
				json.put("result",dif);
				json.put("fileWeight",fileWeight);
				json.put("totalWeight",totalWeight);
				json.put("day",day);
				String month = DateUtil.getYear()+"-"+DateUtil.getMonth();
				FileUtil.writeFileAndTime(PathUtil.dataComparePath+beltName+"/"+month+".txt",json.toJSONString());
			} catch (Exception e) {
				log.error("ZipRunable:dataCompare:134行",e);
			}
		}
	}

	/**
	 *
	 * @description 获取文件第一行记录
	 * @version 1.0.0
	 * @param filePath
	 * @return String
	 */
	public static String getFileFirstLine(String filePath){
		RandomAccessFile raf;
		String firstLine = "";
		try {
			raf = new RandomAccessFile(filePath, "r");
			firstLine=raf.readLine();
			raf.close();
		} catch (Exception e) {
			log.error("ZipRunable:getFileFirstLine:154行",e);
		}
		return firstLine;
	}

	/**
	 *
	 * @description 获取文件最后一行记录
	 * @version 1.0.0
	 * @return String
	 */
	public static String getFileLastLine(String filePath){
		RandomAccessFile raf;
		String lastLine = "";
		try {
			raf = new RandomAccessFile(filePath, "r");
			long len = raf.length();
			if (len != 0L) {
				long pos = len - 1;
				while (pos > 0) {
					pos--;
					raf.seek(pos);
					if (raf.readByte() == '\n') {
						lastLine = raf.readLine();
						break;
					}
				}
			}
			raf.close();
		} catch (Exception e) {
			log.error("ZipRunable:getFileLastLine:184行",e);
		}
		return lastLine;
	}
	public static void main(String[] args) {
		ZipRunable zip = new ZipRunable();
		Thread rd = new Thread(zip);
		rd.start();
	}

}
