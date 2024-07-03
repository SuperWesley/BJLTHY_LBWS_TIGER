package com.bjlthy.lbss.dataComm.socket.processor.zero;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyAioQuickServer;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyServerMethod;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZeroInfoProcessor implements CurrencyServerMethod {
	private static Logger log = LogbackUtil.getLogger("ZeroInfoProcessor", "ZeroInfoProcessor");
	private static CurrencyAioQuickServer quickServer;
	public static Map<String, AioSession> sessionMap = new ConcurrentHashMap<>();
	private static RedisTemplate redisTemplate;
	public ZeroInfoProcessor(RedisTemplate redisTemplate){
		this.redisTemplate = redisTemplate;
	}


	// 开启接收自动化调零服务
	public static void openZeroInfoServer() {
		try {
			quickServer = new CurrencyAioQuickServer();
			quickServer.getAioQuickServer(ConfigBean.ZERO_PORT, new ZeroInfoProcessor(redisTemplate));
			quickServer.setReadBufferSize(2048);
			quickServer.start();
		} catch (Exception e) {
			log.error("ZeroInfoProcessor::openServer - Address already in use (0x00000E),{}",e);
			quickServer.shutdown();
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
	public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
		switch (stateMachineEnum) {
		case NEW_SESSION:
			sessionMap.put(aioSession.getSessionID(), aioSession);
			log.info("ZeroInfoProcessor::stateEvent ,{} " + stateMachineEnum.toString() + "");
			break;
		case SESSION_CLOSED:
			sessionMap.put(aioSession.getSessionID(), aioSession);
			log.warn("ZeroInfoProcessor::stateEvent ,{} " + stateMachineEnum.toString() + "(0x000016)");
			break;
		default:
			sessionMap.put(aioSession.getSessionID(), aioSession);
			log.warn("ZeroInfoProcessor::stateEvent ,{} " + stateMachineEnum.toString() + "(0x000016)");
			break;
		}
	}

	/**
	 * 发送心跳消息
	 */

	@Override
	public void sendHeartRequest(AioSession aioSession) {
		String msg = "$HEART REQUEST END$";
		try {
			AioSession session = sessionMap.get(aioSession.getSessionID());
			session.writeBuffer().write(msg.getBytes());
			session.writeBuffer().flush();
			log.info("ZeroInfoProcessor::sendHeartRequest - 心跳发送成功 ,{} " , aioSession.getSessionID());

		} catch (IOException e) {
			log.error("ZeroInfoProcessor::sendHeartRequest - 心跳发送失败 ,{} " , e);
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
					log.warn("ZeroInfoProcessor::isHeartMessage - 心跳发送失败 ,{} " , e);
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
		// TODO Auto-generated method stub
		try {
			String data = new String(bytes);
			String ipAddr = aioSession.getRemoteAddress().getHostString();
			String msgs = ipAddr+"_"+data;
			if (data.contains("$ZERO")) {
				// 保存调零信息
//				redisTemplate.convertAndSend(ConfigBean.FUNC_TYPE_ZERO,msgs);
				ZeroDataSave.saveZeroInfo(data,ipAddr);
			}else if(data.contains("$INFO")){
				// 保存调秤信息
//				redisTemplate.convertAndSend(ConfigBean.FUNC_TYPE_ZERO,msgs);
				ZeroDataSave.saveZeroFile(data,ipAddr);
			}
		}catch (Exception e){
			log.warn("process,{}",e);
		}
	}



}
