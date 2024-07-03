package com.bjlthy.lbss.tool;

/**
 *
 * @version V1.0
 * @author 张宁
 * @description 这是一个地址保存配置工具类
 * @date 2021年2月17日 上午10:49:12
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class PathUtil {

	public final static String path="D:/bjlthy/LBSS";
	/**皮带雷达数据路径**/
	public final static String bLidarPath="/Lidar/";

	/**皮带电子秤数据路径**/
	public final static String elecsPath="/EWS/";

	/**调零数据路径**/
	public final static String zeroPath=path+"/ZERO/";

	/** 接收下位机异常消息*/
	public final static String errorPath=path+"/ErrorInfo/";

	/** 更新下位机配置**/
	public final static String configPath=path+"/Belt/CONFIG/";



	/** OTA业务报活**/
	public final static String otaPath=path+"/OTA/";

	/** 皮带秤历史数据**/
	public final static String beltHis=path+"/History/belt/";

	/** 电子秤历史数据**/
	public final static String EWSHis=path+"/History/EWS/";
	/** 雷达历史数据**/
	public final static String LidarHis=path+"/History/Lidar/";

	public final static String logPath = path+"/SocketLog/";

	/** 矿皮带实时数据**/
	public final static String KuangSpeed=path+"/Belt/KuangSpeed/";

	/** 数据库备份路径**/
	public final static String backupPath = path+"/BackupDB/";

	/** 皮带运动与静止规则判断路径**/
	public final static String beltBumpPath = path+"/belt_bump/";

	/** 电子秤与日统计数据比较**/
	public final static String dataComparePath = path+"/dataCompare/";

	/** 打标签配置路径**/
	public final static String labelConfigPath = path+"/File/config/";

	/** 打标签路径**/
	public final static String labelPath = "D:/bjlthy/Label/";
}
