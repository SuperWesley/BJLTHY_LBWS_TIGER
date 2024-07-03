package com.bjlthy.lbss.dataComm.socket.processor.label;

import com.bjlthy.lbss.dataComm.socket.protocol.ElecsDataProtocol;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyAioQuickServer;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyServerMethod;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 数据汇总端，下位机所有数据均连接至此，接收消息后按照IP将数据以对应话题进行消息发布
 */
@Controller
@RequestMapping("/lbss/route")
public class LabelDataProcessor implements CurrencyServerMethod {

	private static CurrencyAioQuickServer aioQuickServer;

	private Map<String, AioSession> sessionMap = new HashMap<String, AioSession>();
	// 日志工具类
	private static Logger log = LogbackUtil.getLogger("LabelDataProcessor", "LabelDataProcessor");

	/**
	 * 
	 * @author 张宁
	 * @description 连接建立成功调用的方法
	 * @date 2020年11月16日 下午4:10:45
	 */
	public static void openElectronicScaleServer() {

		try {
			aioQuickServer = new CurrencyAioQuickServer();
			// 获取aioserver
			aioQuickServer.getAioQuickServer(ConfigBean.ELECS_PORT, 3, 30, TimeUnit.SECONDS, new LabelDataProcessor(), new ElecsDataProtocol());

			aioQuickServer.setReadBufferSize(2048);
			aioQuickServer.start();
		} catch (Exception e) {
			log.error("LabelDataProcessor:openElectronicScaleServer - Address already in use,{}",e);
			aioQuickServer.shutdown();
		}
	}

	/**
	 * 状态机事件,当枚举事件发生时由框架触发该方法
	 *
	 *
	 * @param aioSession
	 *            本次触发状态机的AioSession对象
	 * @param stateMachineEnum
	 *            状态枚举
	 * @param throwable
	 *            异常对象，如果存在的话
	 */
	@Override
	public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
		// TODO Auto-generated method stub
		switch (stateMachineEnum) {
		case NEW_SESSION:
			sessionMap.put(aioSession.getSessionID(), aioSession);
			try {
				log.info("LabelDataProcessor::stateEvent - " + aioSession.getRemoteAddress().getPort()+" "+aioSession.getSessionID()+"-->"+stateMachineEnum.toString());
			} catch (IOException e) {
				log.error("LabelDataProcessor::stateEvent - " , e);
			}
			break;
		case SESSION_CLOSED:
			sessionMap.remove(aioSession.getSessionID(), aioSession);
			log.warn("LabelDataProcessor::stateEvent - " + aioSession.getSessionID()+"-->"+stateMachineEnum.toString() + "(0x000007)");
			break;
		default:
			sessionMap.remove(aioSession.getSessionID(), aioSession);
			log.warn("LabelDataProcessor::stateEvent - " + aioSession.getSessionID()+"-->"+stateMachineEnum.toString() + "(0x000007)");
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
			log.info("LabelDataProcessor::sendHeartRequest - 心跳发送成功  " + aioSession.getSessionID());

		} catch (IOException e) {
			log.error("LabelDataProcessor::sendHeartRequest - 心跳发送失败 ,{} " ,e);
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
		if (message.indexOf("$HEART") > -1) {
			// 对请求心跳进行响应
			if (message.indexOf("REQUEST") > -1) {

				// 控制台打印心跳响应时刻信息
				String msg = "$HEART RESPONSE END$";
				try {
					AioSession session = sessionMap.get(aioSession.getSessionID());
					session.writeBuffer().write(msg.getBytes());
					session.writeBuffer().flush();

				} catch (IOException e) {
					log.warn("LabelDataProcessor::isHeartMessage - 心跳发送失败,{}  " , e);
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
		// $BELTREAL 1.29,4224.51,0.88,2758.68,1.46,0.16,5.03,4224.51,0,0 99
		String msg = new String(bytes);
		synchronized (LabelDataProcessor.class) {
			if (msg.indexOf("$ELECS") > -1) {
				try {
					String ipAddr = aioSession.getRemoteAddress().getHostString();
					String msgs = ipAddr+"_"+msg;
//					redisTemplate.convertAndSend(ConfigBean.FUNC_TYPE_LABEL,msgs);
					LabelDataSave.saveLabelData(msg,ipAddr);
				} catch (Exception e) {
					log.warn("process,{}",e);
				}
			}
		}
	}

}
