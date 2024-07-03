package com.bjlthy.lbss.tool;

import com.bjlthy.common.utils.IpUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.*;
import com.bjlthy.lbss.config.config.mapper.*;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;
import com.bjlthy.lbss.config.opc.mapper.LbssOpcMapper;
import org.slf4j.Logger;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 数据字典
 * @date 2022年2月14日 上午11:38:19
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class DictionariesHelper {

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("DictionariesHelper","DictionariesHelper");
	//按层级存储字典信息
	private static Map<Object,Object> dataDictionaries = new LinkedHashMap<Object,Object>();
	//存储全部字典信息
	private static Map<String, Integer> intMap = new LinkedHashMap<String, Integer>();
	private static Map<String, Double> doubleMap = new LinkedHashMap<String, Double>();
	private static Map<String, String> strMap = new LinkedHashMap<String, String>();
	private static DictionariesHelper dictionariesHelper;
	private LbssSysconfigMapper configMapper = SpringUtils.getBean(LbssSysconfigMapper.class);
	private LbssIpconfigMapper ipMapper = SpringUtils.getBean(LbssIpconfigMapper.class);
	private LbssARMConfigMapper armMapper = SpringUtils.getBean(LbssARMConfigMapper.class);
	private LbssWorkingMapper workingMapper = SpringUtils.getBean(LbssWorkingMapper.class);
	private LbssServerIpconfigMapper serverIpconfigMapper = SpringUtils.getBean(LbssServerIpconfigMapper.class);
	//私有化构造函数
	private DictionariesHelper(){}
	
	public static DictionariesHelper getInstance(){
		if(DictionariesHelper.dictionariesHelper==null){
			DictionariesHelper.dictionariesHelper = new DictionariesHelper();
		}
		return DictionariesHelper.dictionariesHelper;
	}
	/**
	 * 初始化数据字典
	 */
	public void init(){
		if(DictionariesHelper.dataDictionaries.size()==0){
			initDataDictionaries();
		}
	}
	/**
	 * 重载数据字典
	 */
	public void reLoad() {
		// TODO Auto-generated method stub
		DictionariesHelper.dataDictionaries.clear();
		DictionariesHelper.intMap.clear();
		DictionariesHelper.strMap.clear();
		DictionariesHelper.doubleMap.clear();
		init();
	}
	/**
	 * 初始化数据字典
	 */
	private void initDataDictionaries(){
		log.info("--------------数据字典初始化开始---------------");
		//查询系统参数信息
		LbssSysconfig config_0 = new LbssSysconfig();
		config_0.setType("0");
		List<LbssSysconfig> dataList0 = configMapper.selectLbssSysconfigList(config_0);
		for(LbssSysconfig sysConfig:dataList0){
			//加载全部数据至intMap
			String code = "";
			if("ALL".equals(sysConfig.getBelt_name())){
				code = sysConfig.getCode();
			}else{
				code = sysConfig.getBelt_name()+"-"+sysConfig.getCode();
			}
			DictionariesHelper.intMap.put(code,Integer.valueOf(sysConfig.getValue()));
		}
		LbssSysconfig config_1 = new LbssSysconfig();
		config_1.setType("1");
		List<LbssSysconfig> dataList1 = configMapper.selectLbssSysconfigList(config_1);
		for(LbssSysconfig sysConfig:dataList1){
			//加载全部数据至strMap
			String code = "";
			if("ALL".equals(sysConfig.getBelt_name())){
				code = sysConfig.getCode();
			}else{
				code = sysConfig.getBelt_name()+"-"+sysConfig.getCode();
			}
			DictionariesHelper.strMap.put(code,sysConfig.getValue());
		}
		
		LbssSysconfig config_2 = new LbssSysconfig();
		config_2.setType("2");
		List<LbssSysconfig> dataList2 = configMapper.selectLbssSysconfigList(config_2);
		for(LbssSysconfig sysConfig:dataList2){
			//加载全部数据至doubleMap
			String code = "";
			if("ALL".equals(sysConfig.getBelt_name())){
				code = sysConfig.getCode();
			}else{
				code = sysConfig.getBelt_name()+"-"+sysConfig.getCode();
			}
			DictionariesHelper.doubleMap.put(code,Double.valueOf(sysConfig.getValue()));
		}
		//查询系统IP端口信息
		LbssIpconfig ipconfig_0 = new LbssIpconfig();
		ipconfig_0.setType("0");
		List<LbssIpconfig> ipList0 = ipMapper.selectLbssIpconfigList(ipconfig_0);
		for (LbssIpconfig ipconfig : ipList0) {
			String code = "";
			if("ALL".equals(ipconfig.getBelt_name())){
				code = ipconfig.getCode();
			}else{
				code = ipconfig.getBelt_name()+"-"+ipconfig.getCode();
			}
			DictionariesHelper.intMap.put(code,Integer.valueOf(ipconfig.getValue()));
		}
		LbssIpconfig ipconfig_1 = new LbssIpconfig();
		ipconfig_1.setType("1");
		List<LbssIpconfig> ipList1 = ipMapper.selectLbssIpconfigList(ipconfig_1);
		for (LbssIpconfig ipconfig : ipList1) {
			String code = "";
			if("ALL".equals(ipconfig.getBelt_name())){
				code = ipconfig.getCode();
			}else{
				code = ipconfig.getBelt_name()+"-"+ipconfig.getCode();
			}
			DictionariesHelper.strMap.put(code,ipconfig.getValue());
		}
		
		//查询下位机配置信息
		LbssARMConfig armConfig_0 = new LbssARMConfig();
		armConfig_0.setType("0");
		List<LbssARMConfig> armList0 = armMapper.selectLbssARMConfigList(armConfig_0);
		for (LbssARMConfig armConfig : armList0) {
			String code = "";
			if("ALL".equals(armConfig.getBelt_name())){
				code = armConfig.getCode();
			}else{
				code = armConfig.getBelt_name()+"-"+armConfig.getCode();
			}
			DictionariesHelper.intMap.put(code,Integer.valueOf(armConfig.getValue()));
		}
		LbssARMConfig armConfig_2 = new LbssARMConfig();
		armConfig_2.setType("2");
		List<LbssARMConfig> armList2 = armMapper.selectLbssARMConfigList(armConfig_2);
		for (LbssARMConfig armConfig : armList2) {
			String code = "";
			if("ALL".equals(armConfig.getBelt_name())){
				code = armConfig.getCode();
			}else{
				code = armConfig.getBelt_name()+"-"+armConfig.getCode();
			}
			DictionariesHelper.doubleMap.put(code,Double.valueOf(armConfig.getValue()));
		}
		//查询工作面信息
		List<LbssWorking> workingList = workingMapper.selectLbssWorkingNotALLList(null);
		ConfigBean.workingList = workingList;
		for (LbssWorking working : workingList) {
//			DictionariesHelper.strMap.put(working.getBelt_name().trim(),working.getBelt_ip());
			ConfigBean.beltsIPMap.put(working.getBelt_ip(), working.getBelt_name());
			ConfigBean.beltsMap.put(working.getBelt_name(),working);
		}
		//OPCUA 服务节点初始化
		LbssOpcMapper opcMapper = SpringUtils.getBean(LbssOpcMapper.class);
		List<LbssOpc> opcList = opcMapper.selectLbssOpcList(null);
		//根据工作面进行分组
		ConfigBean.opcList =opcList;

		//查询服务器ip地址
		List<LbssServerIpconfig> serverIpconfigList = serverIpconfigMapper.selectLbssServerIpconfigList(null);
		String iPV4 = IpUtils.getHostIp();
		for (LbssServerIpconfig serverIpConfig:serverIpconfigList) {
			DictionariesHelper.strMap.put(serverIpConfig.getServerName(),serverIpConfig.getServerIp());
			//如果获取的本地ip地址和配置的地址一致进行保存
			if (iPV4.equals(serverIpConfig.getServerIp())){
				ConfigBean.serverIp_Map.put(serverIpConfig.getServerName(),serverIpConfig.getServerName());
			}
		}

		log.info("--------------数据字典初始化结束---------------");
	}
	
	/**
	 * 获取全部字典数据
	 */
	public static Map<Object,Object> getDataDictionaries(){
		return DictionariesHelper.dataDictionaries;
	}
	
    /**
	 * 根据类型获取下设的字典数据
	 * @param type 类型编码(T_COMMON_TYPE表中的OBJTYPE字段)
	 * @return map:key为code,value为name
	 */
	public static List getDicListByType(String type){
		return  (List)DictionariesHelper.dataDictionaries.get(type);
	}
	
    /**
	 * 根据key获取value
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static Integer getDicIntegerValueByCode(String code){
		return DictionariesHelper.intMap.get(code);
	}
	
	 /**
	 * 根据key获取value
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static Double getDicDoubleValueByCode(String code){
		return DictionariesHelper.doubleMap.get(code);
	}
		
	
	 /**
	 * 根据key获取value
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static String getDicStringValueByCode(String code){
		return DictionariesHelper.strMap.get(code);
	}
	
    /**
	 * 更新点表配置
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static void setDicIntegerValueByCode(String code,Integer value){
		//加载全部数据至dataAllMap
		DictionariesHelper.intMap.put(code,value);
	}
    /**
	 * 更新点表配置
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static void setDicStringValueByCode(String code,String value){
		//加载全部数据至msgMap
		DictionariesHelper.strMap.put(code,value);
	}
	
	 /**
	 * 更新点表配置
	 * @param code 编码(T_COMMON_INFO中的CONCODE)
	 * @return 值(T_COMMON_INFO中的CONNAME)
	 */
	public static void setDicDoubleValueByCode(String code,Double value){
		//加载全部数据至msgMap
		DictionariesHelper.doubleMap.put(code,value);
	}
}
