package com.bjlthy.lbss.dataComm.socket.processor.config;


import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.config.domain.LbssARMConfig;
import com.bjlthy.lbss.config.config.mapper.LbssARMConfigMapper;
import com.bjlthy.lbss.config.shift.domain.LbssShift;
import com.bjlthy.lbss.config.shift.mapper.LbssShiftMapper;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyAioQuickClient;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyClientMethod;
import com.bjlthy.lbss.dataComm.socket.protocol.ARMConfigProtocol;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.DateUtil;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 *
 * @version V1.0
 * @author 张宁
 * @description 配置通信服务端
 * @date 2020年12月18日 下午9:32:51
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class NanoConfigProcessor implements CurrencyClientMethod {

	public static  AioQuickClient aioQuickClient = null;

	public static LbssARMConfigMapper armConfigMapper = SpringUtils.getBean(LbssARMConfigMapper.class);
	public static  AioSession session ;
	private Map<String, AioSession> sessionMap = new ConcurrentHashMap<>();
	private Map<String, String> beltsMap = new HashMap<>();
	//日志工具类
	public static Logger log = LogbackUtil.getLogger("NanoConfigProcessor", "NanoConfigProcessor");

	/**
	 *
	 * @author 张宁
	 * @description 连接建立成功调用的方法
	 * @date 2020年11月16日 下午4:10:45
	 */
	public static void openARMConfigClient(String ip) {
		try {
			CurrencyAioQuickClient currencyAioQuickClient = new CurrencyAioQuickClient();
			//雷达连接
			aioQuickClient = currencyAioQuickClient.getAioQuickClient(ip, ConfigBean.ARM_CONFIG_PORT, 3, 30, TimeUnit.SECONDS, new NanoConfigProcessor(),new ARMConfigProtocol());
			aioQuickClient.setReadBufferSize(2048);
			aioQuickClient.start();

		}catch(Exception e){
			log.error("SendOtherConfigProcessor::openSendOtherConfigClient  "+ip+",{}",e);
		}
	}

	/**
	 * 状态机事件,当枚举事件发生时由框架触发该方法
	 *
	 *
	 * @param aioSession          本次触发状态机的AioSession对象
	 * @param stateMachineEnum 状态枚举
	 * @param throwable        异常对象，如果存在的话
	 */
	@Override
	public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
		// TODO Auto-generated method stub
		String belt_name = "";
		switch (stateMachineEnum) {
			case NEW_SESSION:
				session = aioSession;
				sessionMap.put(aioSession.getSessionID(), aioSession);
				String ipAddr = null;
				try {
					ipAddr = aioSession.getRemoteAddress().getHostString();
					log.info("NanoConfigProcessor::stateEvent - "+belt_name+"皮带秤,{}"+aioSession.getRemoteAddress().getPort()+" "+stateMachineEnum.toString());
				} catch (IOException e) {
					log.error("NanoConfigProcessor::stateEvent - "+belt_name+"皮带秤,{}", e);
				}
				belt_name = ConfigBean.beltsIPMap.get(ipAddr);
				beltsMap.put(aioSession.getSessionID(),belt_name);
				break;
			case SESSION_CLOSED:
				sessionMap.remove(aioSession.getSessionID(), aioSession);
				belt_name = beltsMap.get(aioSession.getSessionID());
				beltsMap.remove(aioSession.getSessionID());
				log.warn("NanoConfigProcessor::stateEvent - "+belt_name+"皮带秤,{}"+aioSession.getSessionID()+"-->"+stateMachineEnum.toString()+"(0x000009)");
				break;
			default:
				sessionMap.remove(aioSession.getSessionID(), aioSession);
				belt_name = beltsMap.get(aioSession.getSessionID());
				beltsMap.remove(aioSession.getSessionID());
				log.warn("NanoConfigProcessor::stateEvent -  "+belt_name+"皮带秤,{}"+aioSession.getSessionID()+"-->"+stateMachineEnum.toString()+"(0x000009)");
				break;
		}
	}
	/**
	 * 发送心跳消息
	 */
	@Override
	public void sendHeartRequest(AioSession aioSession) {
		// TODO Auto-generated method stub
		String msg = "$HEART REQUEST END$";
		try {
			AioSession session = sessionMap.get(aioSession.getSessionID());
			session.writeBuffer().write(msg.getBytes());
			session.writeBuffer().flush();
			log.info("BeltRealtimeDataProcessor::sendHeartRequest - 心跳发送成功  "+ aioSession.getSessionID());
		} catch (IOException e) {
			log.warn("BeltRealtimeDataProcessor::sendHeartRequest - 心跳发送失败,{}  " , e);
		}
	}

	/**
	 * 判断心跳还是数据信息
	 */
	@Override
	public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
		// TODO Auto-generated method stub
		String message = new String(bytes);
		// 判断是否是服务端响应的心跳数据
		if (message.indexOf("$HEART")>-1) {
			//对请求心跳进行响应
			if(message.indexOf("REQUEST")>-1){

				// 控制台打印心跳响应时刻信息
				String msg = "$HEART RESPONSE END$";
				try {
					AioSession session = sessionMap.get(aioSession.getSessionID());
					session.writeBuffer().write(msg.getBytes());
					session.writeBuffer().flush();

				} catch (IOException e) {
					log.warn("BeltRealtimeDataProcessor::isHeartMessage - 心跳发送失败 ,{} " , e);
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void process(AioSession session,byte[] bytes) {
		String data = new String(bytes);
		try {
			String ipAddr = session.getRemoteAddress().getHostString();
			//发送配置异常，重新发送
			if(data.indexOf("$WRONG")>-1){
				String belt_name = ConfigBean.beltsIPMap.get(ipAddr);
				log.warn(" NanoConfigProcessor:process "+belt_name+"皮带秤更新配置失败" + data);
				sendConfigInfo(belt_name);
			}else{
				//发送配置成功断开客户端
				aioQuickClient.shutdown();
				aioQuickClient = null;
			}
		} catch (IOException e) {
			log.error("process,{}",e);
		}

	}




	/**
	 * 
	 * @author 张宁
	 * @Title: sendConfigInfo
	 * @Description: 发送配置信息给下位机
	 * @return void    返回类型
	 * @date 2021年6月22日 下午5:20:42
	 * @throws
	 */
	public static void sendConfigInfo(String beltName){
		try {
			LbssShiftMapper shiftMapper = SpringUtils.getBean(LbssShiftMapper.class);

			//查询所有配置信息
			LbssARMConfig armConfig1 = new LbssARMConfig();
			armConfig1.setBelt_name("ALL");
			List<LbssARMConfig> configList1 = armConfigMapper.selectLbssARMConfigList(armConfig1);
			LbssARMConfig armConfig2 = new LbssARMConfig();
			armConfig2.setBelt_name(beltName);
			List<LbssARMConfig> configList2 = armConfigMapper.selectLbssARMConfigList(armConfig2);
			//查询班次配置信息
			List<LbssShift> shiftList = shiftMapper.selectLbssShiftList(null);
			StringBuilder buff = new StringBuilder();
			buff.append("$CONFIG ");
			//拼接班次时间
			for (LbssShift shift : shiftList) {
				long sec = DateUtil.getSec(shift.getBegin());
				if ("0".equals(shift.getStatus())) {
					buff.append(shift.getCode()+":"+sec+",");
				}
			}
			//循环拼接配置信息 格式为code:value
			for (LbssARMConfig config1 : configList1) {
				if("reset".equals(config1.getCode())){
					long sec = DateUtil.getSec(shiftList.get(Integer.valueOf(config1.getValue())).getBegin());
					buff.append("reset"+":"+sec).append(",");
				}else{
					buff.append(config1.getCode()+":"+config1.getValue()).append(",");
				}
			}
			//循环拼接配置信息 格式为code:value
			for (LbssARMConfig config2 : configList2) {
				buff.append(config2.getCode()+":"+config2.getValue()).append(",");
			}
			//截取数据
			String msg =buff.toString().substring(0,buff.toString().length()-1);
			msg = msg+" END$";
			//发送配置信息
			session.writeBuffer().write(msg.getBytes());
			session.writeBuffer().flush();
			//发送配置信息，上位机需要记录发送日志
			log.info("NanoConfigProcessor:nanoConfigInfo "+beltName+"皮带秤数据发送成功,{}"+msg);
		} catch (IOException e) {
			log.warn("NanoConfigProcessor:nanoConfigInfo "+beltName+"皮带秤数据发送失败,{}",e);
		}
	}


}
