package com.bjlthy.lbss.tool;


import com.bjlthy.lbss.config.config.domain.LbssWorking;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 上位机启动配置
 * @date 2021年5月26日 下午11:05:27
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ConfigBean {

	 /** ============================IP和Post 配置=======================*/
	
	  /** 皮带实时数据端口号 */
	  public static Integer PC_PORT = DictionariesHelper.getDicIntegerValueByCode("PC_PORT");
	  
	  /** 电子秤端口号 */
	  public static Integer ELECS_PORT = DictionariesHelper.getDicIntegerValueByCode("ELECS_PORT");
	  
	  /** OTA通信端口 */
	  public static Integer OTA_PORT = DictionariesHelper.getDicIntegerValueByCode("OTA_PORT");
	  
	  /** 下位机配置服务端IP*/
	  public static String ARM_CONFIG_IP = DictionariesHelper.getDicStringValueByCode("ARM_CONFIG_IP");
	  
	  /** 下位机配置服务端端口号*/
	  public static Integer ARM_CONFIG_PORT = DictionariesHelper.getDicIntegerValueByCode("ARM_CONFIG_PORT");
	  
	  /** 外设激光雷达IP*/
	  public static String LIDAR_IP = DictionariesHelper.getDicStringValueByCode("LIDAR_IP");
	  
	  /** 外设激光雷达端口号*/
	  public static Integer LIDAR_PORT = DictionariesHelper.getDicIntegerValueByCode("LIDAR_PORT");
	  
	  /** 激光雷达过滤端口号*/
	  public static Integer LIDAR_FILTER_PORT = DictionariesHelper.getDicIntegerValueByCode("LIDAR_FILTER_PORT");
	  
	  /** 自动化调零端口号*/
	  public static Integer ZERO_PORT = DictionariesHelper.getDicIntegerValueByCode("ZERO_PORT");
	  
	  /** 网络继电器IP*/
	  public static String REAL_IP = DictionariesHelper.getDicStringValueByCode("REAL_IP");
	  
	  /** 网络继电器端口号*/
	  public static Integer REAL_PORT = DictionariesHelper.getDicIntegerValueByCode("REAL_PORT");
	  
	  /** 雷达网络继电器IP*/
	  public static String Lidar_REAL_IP = DictionariesHelper.getDicStringValueByCode("Lidar_REAL_IP");
	  
	  /** 雷达网络继电器端口号*/
	  public static Integer Lidar_REAL_PORT = DictionariesHelper.getDicIntegerValueByCode("Lidar_REAL_PORT");
	  
	  
	  /** OPCUA应用链接地址*/
	  public static String applicationUrl = DictionariesHelper.getDicStringValueByCode("applicationUrl");
	  
	  /** OPCUA服务链接地址*/
	  public static String endpointUrl = DictionariesHelper.getDicStringValueByCode("endpointUrl");
	  
	  
	  /** ============================开关配置=======================*/
	  
	  /** 含矸率及密度控制开关*/
	  public static Integer show_control = DictionariesHelper.getDicIntegerValueByCode("show_control");
	  /** 手机端展示开关*/
	  public static Integer show_cellphone = DictionariesHelper.getDicIntegerValueByCode("show_cellphone");
	  /** OPCUA服务通讯接口开关*/
	  public static Integer server_or_client = DictionariesHelper.getDicIntegerValueByCode("server_or_client");
	  /** opc服务启动状态*/
	  public static boolean opc_state = false;
	  /** 速度校验开关*/
	  public static Integer speed_check = DictionariesHelper.getDicIntegerValueByCode("speed_check");
	  /** 矿速度获取开关*/
	  public static Integer httpApi_swtich = DictionariesHelper.getDicIntegerValueByCode("httpApi_swtich");
	  
	  /** 速度模拟开关*/
	  public static Integer speedSwitch = DictionariesHelper.getDicIntegerValueByCode("SpeedSwitch");
	  
	  /** 皮带所在工作面*/
	  public static String belt_name =DictionariesHelper.getDicStringValueByCode("working_area");
	  
	  /** 相差比例系数*/
	  public static Double percentage = DictionariesHelper.getDicDoubleValueByCode("percentage");
	  
	  /** ============================其他配置=======================*/
	  /**皮带速度*/
	  public static Double belt_speed = 0.00;
	  /** 松散系数*/
	  public static Double songsan = DictionariesHelper.getDicDoubleValueByCode("songSan");
	  /** 模拟速度*/
	  public static Double simulation_speed = DictionariesHelper.getDicDoubleValueByCode("simulation_speed");
	  /** 雷达数据最大边界值*/
	  public static Integer lidarBegin = DictionariesHelper.getDicIntegerValueByCode("lidarBegin"); 
	  /** 雷达数据最小边界值*/
	  public static Integer lidarEnd = DictionariesHelper.getDicIntegerValueByCode("lidarEnd"); 
	  
	  /** 早班时间*/
	  public static String MOR_time;
	  
	  /** 中班时间*/
	  public static String AFT_time;

	  /** 晚班时间*/
	  public static String NIG_time;
	  
	  /** 开始时间用于拼接sql查询*/
	  public static String startTime;
	  
	  /** OPCUA服务节点*/
	  public static List<LbssOpc> opcList = new ArrayList<>();
	  /** Modbus服务节点*/
	  public static Map<String, List<LbssOpc>> modbusMap = new HashMap<String, List<LbssOpc>>();
	  /** 皮带秤信息*/
	  public static Map<String, LbssWorking> beltsMap = new HashMap<String, LbssWorking>();
	  /** 皮带秤IP信息*/
	  public static Map<String, String> beltsIPMap = new HashMap<String, String>();

	  /** 服务器ip信息*/
	  public static Map<String, String> serverIp_Map = new HashMap<String, String>();

	  /** 工作面集合信息*/
	  public static List<LbssWorking> workingList = new ArrayList<>();

	  /** 皮带秤小时统计*/
	  public static Map<String, Double> belt_hourMap = new HashMap<String, Double>();
	  
	  /** 用于记录雷达数据异常时间*/
	  public static String recordTime;
	  /** 下位机配置修改状态*/
	  public static String edit_status = "0";
	  
	  /** 重量传感器最大量程*/
	  public static Double maxweight_sensor = DictionariesHelper.getDicDoubleValueByCode("maxweight_sensor");

	  /** 定义皮带机功能码 */
	  public static final String FUNC_TYPE_BELT = "belt_func";
	  public static final String FUNC_TYPE_LABEL = "label_func";
	  public static final String FUNC_TYPE_LIDAR = "lidar_func";
	  public static final String FUNC_TYPE_ZERO = "zero_func";
	  public static final String FUNC_TYPE_RELAY = "relay_func";

}
