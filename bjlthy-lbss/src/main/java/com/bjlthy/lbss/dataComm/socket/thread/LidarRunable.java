package com.bjlthy.lbss.dataComm.socket.thread;

import com.bjlthy.common.utils.IpUtils;
import com.bjlthy.lbss.dataComm.socket.processor.lidar.BeltLidar1Processor;
import com.bjlthy.lbss.dataComm.socket.processor.lidar.BeltLidar2Processor;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DictionariesHelper;

import java.util.Set;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 删除文件
 * @date 2020年11月16日 下午4:56:16
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class LidarRunable implements Runnable{

	public LidarRunable(){
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//工作面信息
			Set<String> beltNames = ConfigBean.beltsMap.keySet();
			for (String beltName : beltNames) {
				String lidarIp = DictionariesHelper.getDicStringValueByCode(beltName+"-LIDAR_IP");
				/** 启动雷达socket通信 */
				if(beltName.equals(BeltLidar2Processor.belt_name) && IpUtils.ipDetection(lidarIp,3000)){
					BeltLidar2Processor.openBeltLidarClient(lidarIp);
				}
				if(beltName.equals(BeltLidar1Processor.belt_name) && IpUtils.ipDetection(lidarIp,3000)){
					BeltLidar1Processor.openBeltLidarClient(lidarIp);
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
