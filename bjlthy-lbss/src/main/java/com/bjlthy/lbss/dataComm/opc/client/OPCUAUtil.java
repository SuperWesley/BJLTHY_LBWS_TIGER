package com.bjlthy.lbss.dataComm.opc.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bjlthy.lbss.config.config.domain.LbssWorking;
import com.bjlthy.lbss.config.opc.domain.LbssOpc;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class OPCUAUtil {

	public static String endpointUrl = ConfigBean.endpointUrl;
	public static String applicationUrl = ConfigBean.applicationUrl;

	//创建OPC服务
	public static OPCUAClient opcuaClient = new OPCUAClient();

	public static List<NodeId> nodeIds = new ArrayList<NodeId>();
	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("OPCUAUtil", "OPCUAUtil");

	/**
	 *
	 * @author 张宁
	 * @Description: 往OPC点表更新信息
	 * @return void    返回类型
	 * @date 2021年12月10日 下午9:43:39
	 * @throws
	 */
	public static void writeNodesToOPC(Map<String,Object> map,String beltName){
		Map<NodeId, String> nodeMap = new HashMap<>();
		LbssWorking working = ConfigBean.beltsMap.get(beltName);
		List<LbssOpc> list = ConfigBean.opcList;
		//添加节点信息
		for (int i = 0; i <list.size() ; i++) {
			String identifier = list.get(i).getNodeId();
			String value = "";
			if(identifier.contains("List")){
				List<String> strList = (List<String>) map.get(identifier);
				value = strList.toString().replace("[", "").replace("]", "").replace(" ", "");
			}else{
				value = (String) map.get(identifier);
			}
			String working_area = working.getWorking_area();
			NodeId nodeid = new NodeId(2,"lthy.opc."+working_area+"."+beltName+"."+identifier);
			nodeMap.put(nodeid,value);
		}

		//连接成功更新数据
		if(opcuaClient.isRunningState()){
			//更新节点信息
			boolean updateStatus= opcuaClient.writeNodes(nodeMap);
			//断开OPC链接
//        	opcuaClient.disconnect();

		}else{
			boolean connect = opcuaClient.createConnection();//如果opcua控制开关打开，系统启动时连接opcua服务
			//连接成功更新节点信息
			if(connect){
				System.out.println("opcua 连接成功");
				opcuaClient.writeNodes(nodeMap);
			}
		}
	}

	/**
	 * 订阅信息
	 *
	 * @param
	 */
	public static void addNodeIds() {
		List<NodeId> nodeIdList = new ArrayList<>();
		//工作面信息
		List<LbssWorking> workingList = ConfigBean.workingList;
		for (LbssWorking working : workingList) {
			//添加节点
			List<LbssOpc> opcList = ConfigBean.opcList;
			for (LbssOpc opc : opcList) {
				String identifier = "lthy.opc."+working.getWorking_area()+"."+working.getBelt_name()+"."+opc.getNodeId();
				NodeId nodeId = new NodeId(2, identifier);
				nodeIdList.add(nodeId);
			}
		}
		nodeIds = nodeIdList;
//		opcuaClient.subscription(nodeIds, 1000);

	}

	/**
	 *
	 * @author 张宁
	 * @Description: 从OPC点表读取信息
	 * @return void    返回类型
	 * @date 2021年12月10日 下午9:43:39
	 * @throws
	 */
	public static JSONObject readNodesToOPC(){
		StringBuffer buff = new StringBuffer();
		JSONObject head = new JSONObject();

		//连接成功更新数据
		if(opcuaClient.isRunningState()){
			//添加节点信息
			if(nodeIds.size() == 0){
				addNodeIds();
			}
			//读取节点信息
			String flag = "";
			JSONObject body = new JSONObject();
			JSONArray ar_body = new JSONArray();

			CompletableFuture<List<DataValue>> listCompletableFuture = opcuaClient.getClient().readValues(0.0, TimestampsToReturn.Both, nodeIds);
			try {
				List<DataValue> dataValues = listCompletableFuture.get();
				for (int i = 0; i < dataValues.size(); i++) {
					DataValue dataValue = dataValues.get(i);
					if (nodeIds.get(i)==null){
						continue;
					}
					String name=((String) nodeIds.get(i).getIdentifier()).replace("lthy.opc.","");
					String area = name.split("\\.")[1];
					if(!area.equals(flag)){
						if(buff.length()>0){
							buff.deleteCharAt(buff.length() - 1);
							String str = "{"+buff.toString()+"}";
							JSONObject jsonObj = JSON.parseObject(str);
							JSONObject data1 = new JSONObject();
							data1.put(flag,jsonObj);
							ar_body.add(data1);
							body.put("rows",ar_body);
							buff = new StringBuffer();
						}
					}

					String value = (String)dataValue.getValue().getValue();
					if(value== null || value.contains("String")){
						value = "0";
					}
					if(name.contains("List")){
						value = "'"+value+"'";
					}
					buff.append(name.split("\\.")[2]+":"+value.trim()).append(",");
					flag = name.split("\\.")[1];

				}
				buff.deleteCharAt(buff.length() - 1);
				String str = "{"+buff.toString()+"}";
				JSONObject jsonObj = JSON.parseObject(str);
				JSONObject data1 = new JSONObject();
				data1.put(flag,jsonObj);
				ar_body.add(data1);
				body.put("rows",ar_body);

				head.put("data",body);

			} catch (InterruptedException | ExecutionException e) {
				 log.error("readNodesToOPC",e);
			}

		}else{
			opcuaClient.createConnection();//如果opcua控制开关打开，系统启动时连接opcua服务
		}
		return head;
	}

}
