package com.bjlthy.lbss.dataComm.socket.processor.belt;

import com.alibaba.fastjson.JSON;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.common.utils.spring.SpringUtils;
import com.bjlthy.lbss.config.errorcode.domain.LbssError;
import com.bjlthy.lbss.config.errorcode.service.ILbssErrorService;
import com.bjlthy.lbss.dataComm.socket.processor.relay.RelayProcessor;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyAioQuickServer;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyServerMethod;
import com.bjlthy.lbss.dataComm.socket.webSocket.BeltStateWebSocket;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import com.bjlthy.lbss.tool.ParseUtils;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 数据汇总端，下位机所有数据均连接至此，接收消息后按照IP将数据以对应话题进行消息发布
 */
@Controller
@RequestMapping("/lbss/route")
public class BeltRealtimeDataProcessor implements CurrencyServerMethod {

	public static String code = "1";

	private Map<String, AioSession> sessionMap = new ConcurrentHashMap<>();
	public static Map<String, String> beltsMap = new HashMap<>();

	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("BeltRealtimeDataProcessor", "BeltRealtimeDataProcessor");
	//主运状态（主）
	public static String zy_status = "0";

	/**
	 * 
	 * @author 张宁 @Title: openBeltServer @Description:
	 *         开启socket服务端接收皮带实时数据 @param 设定文件 @return void 返回类型 @date
	 *         2021年8月27日 下午4:40:04 @throws
	 */
	public static void openBeltServer() {
		CurrencyAioQuickServer aioQuickServer = new CurrencyAioQuickServer();
		try {
			aioQuickServer.getAioQuickServer(ConfigBean.PC_PORT, new BeltRealtimeDataProcessor());
			aioQuickServer.setReadBufferSize(2048);
			aioQuickServer.start();
		} catch (Exception e) {
			log.warn("BeltRealtimeDataProcessor::openBeltServer -  启动失败 ,{} " , e);
			aioQuickServer.shutdown();
		}
	}

	/**
	 * 状态机事件,当枚举事件发生时由框架触发该方法
	 *
	 * @param aioSession
	 *            本次触发状态机的AioSession对象
	 * @param stateMachineEnum
	 *            状态枚举
	 * @param throwable
	 *            异常对象，如果存在的话
	 */
	@Override
	public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable)  {
		// TODO Auto-generated method stub
		String belt_name = "";
		switch (stateMachineEnum) {
			case NEW_SESSION:
			sessionMap.put(aioSession.getSessionID(), aioSession);
			String ipAddr = null;
			try {
				ipAddr = aioSession.getRemoteAddress().getHostString();
				log.info("BeltRealtimeDataProcessor::stateEvent ,{} " + aioSession.getRemoteAddress().getPort()+" "+aioSession.getSessionID()+"-->"+stateMachineEnum.toString());
			} catch (IOException e) {
				log.error("BeltRealtimeDataProcessor::stateEvent ,{} " , e);
			}
			belt_name = ConfigBean.beltsIPMap.get(ipAddr);
			beltsMap.put(aioSession.getSessionID(),belt_name);
			break;
		case SESSION_CLOSED:
			belt_name = beltsMap.get(aioSession.getSessionID());
			sessionMap.remove(aioSession.getSessionID(), aioSession);
			beltsMap.remove(aioSession.getSessionID());
			aioSession.close();
			code = "1";
			sendStateToWebBrowser(code,"socket",belt_name);
			//重启arm板
			rebootARM();
			log.warn("BeltRealtimeDataProcessor::stateEvent - " + aioSession.getSessionID()+"-->"+stateMachineEnum.toString() + "(0x000005)");
			break;
		default:
			sessionMap.remove(aioSession.getSessionID(), aioSession);
			aioSession.close();
			log.warn("BeltRealtimeDataProcessor::stateEvent ,{} " + aioSession.getSessionID()+"-->"+stateMachineEnum.toString() + "(0x000005)");
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
			log.info("BeltRealtimeDataProcessor::sendHeartRequest - 心跳发送成功 ,{} "+ aioSession.getSessionID());
		} catch (IOException e) {
			log.warn("BeltRealtimeDataProcessor::sendHeartRequest - 心跳发送失败 ,{} " , e);
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
					log.warn("BeltRealtimeDataProcessor::isHeartMessage - 心跳发送失败,{}  " , e);
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * 处理接收到的消息
	 *
	 * @param aioSession
	 *            通信会话
	 * @param bytes
	 *            待处理的业务消息
	 */
	@Override
	public void process(AioSession aioSession, byte[] bytes) {
		// $BELTREAL 1.29,4224.51,0.88,2758.68,1.46,0.16,5.03,4224.51,0,0 END$
		String data = new String(bytes);
		synchronized (BeltRealtimeDataProcessor.class) {
			try {
				String ipAddr = aioSession.getRemoteAddress().getHostString();
				String msgs = ipAddr+"_"+data;
//				redisTemplate.convertAndSend(ConfigBean.FUNC_TYPE_BELT,msgs);
				BeltRealtimeDataSave.saveBeltData(data,ipAddr);
			} catch (Exception e) {
				log.warn("BeltRealtimeDataProcessor::process 数据异常 ",e);
			}
		}
	}

	/**
	 * @author 张宁
	 * @description 发送皮带机状态到web浏览器
	 * @date 2020年11月16日 下午4:12:17
	 */
	public void sendStateToWebBrowser(String code,String type,String beltName) {
		try {
			if (StringUtils.isEmpty(beltName)){
				return;
			}
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
			ajaxResult.put("belt_name",beltName);
			ajaxResult.put("msg", collect.size() > 0 ? collect.get(0).getTips() : "正常");

			// 异常消息发送至界面显示
			String working_area = ConfigBean.beltsMap.get(beltName).getWorking_area();
			//发送至PC端显示
			BeltStateWebSocket.sendMessageToClient(beltName,JSON.toJSONString(ajaxResult));
		} catch (Exception e) {
			log.warn("BeltRealtimeDataProcessor::sendStateToWebBrowser - 发送皮带机状态到web浏览器失败,{}  ", e);
		}
	}




	/**
	 *
	 * @author 张宁
	 * @Description: 通过网络继电器重启arm板
	 * @return void    返回类型
	 * @date 2022年6月29日 下午5:19:27
	 * @throws
	 */
	public void rebootARM(){
		try {
			Thread.sleep(1000*60);
			//先检查网络继电器网络是否正常
			boolean real_flag  = ParseUtils.pingIp(ConfigBean.REAL_IP);
			//Nano板ip
			boolean arm_flag  = ParseUtils.pingIp(ConfigBean.ARM_CONFIG_IP);
			if(real_flag && !arm_flag){
				RelayProcessor.openRelayClient("Nano");
			}
		} catch (InterruptedException e) {
			log.warn("rebootARM,{}", e);
		}
	}
}
