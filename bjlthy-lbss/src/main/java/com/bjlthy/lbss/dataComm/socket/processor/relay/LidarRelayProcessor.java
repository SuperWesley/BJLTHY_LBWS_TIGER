package com.bjlthy.lbss.dataComm.socket.processor.relay;

import com.bjlthy.lbss.dataComm.socket.client.CurrencyAioQuickClient;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyClientMethod;
import com.bjlthy.lbss.dataComm.socket.protocol.RelayProtocol;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LidarUtil;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class LidarRelayProcessor implements CurrencyClientMethod {

	private static AioSession session;
	
	public static  AioQuickClient aioQuickClient = null;
	
	/** 发送状态标识，1关闭，0 开启  -1失效*/
	public static int flag = -1;
	//日志工具类
	private static Logger log = LogbackUtil.getLogger("RelayProcessor", "LidarRelayProcessor");

	public static AioSession getSession() {
		return session;
	}

	public static void setSession(AioSession session) {
		LidarRelayProcessor.session = session;
	}
	
	/**
	 * 重新启动继电器
	 */
	public static void openRelayClient(String ip,String type) {
	    try {
	    	CurrencyAioQuickClient currencyAioQuickClient = new CurrencyAioQuickClient();
	    	//继电器
	    	aioQuickClient = currencyAioQuickClient.getAioQuickClient(ip, ConfigBean.Lidar_REAL_PORT, 3, 30, TimeUnit.SECONDS, new LidarRelayProcessor(),new RelayProtocol());
		    aioQuickClient.setReadBufferSize(2048);
		    aioQuickClient.start();
		  
		    if("Lidar".equals(type)){
		    	changeRelayStatus();
		    }
	    } catch (Exception e) {
			log.error("openRelayClient,{}",e);
	    }
	}
	
    /**
     * 状态机事件,当枚举事件发生时由框架触发该方法
     *
     * @param aioSession          本次触发状态机的AioSession对象
     * @param stateMachineEnum 状态枚举
     * @param throwable        异常对象，如果存在的话
     */
    @Override
    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        switch (stateMachineEnum) {
            case NEW_SESSION:
            	setSession(aioSession);
                log.info("BeltLidarProcessor::stateEvent ,{} "+stateMachineEnum.toString());
                break;
            case SESSION_CLOSED:
            	setSession(null);
                log.warn("BeltLidarProcessor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x000006)");
                break;
            default:
            	setSession(null);
                log.warn("BeltLidarProcessor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x000006)");
                break;
        }
    }
    
	/**
	 * 发送心跳消息
	 */
	@Override
	public void sendHeartRequest(AioSession aioSession) {
		// TODO Auto-generated method stub
//		String msg = "timer";
//		try {
//			aioSession.writeBuffer().write(msg.getBytes());
//			aioSession.writeBuffer().flush();
//			log.info("OverTheAirProcessor::sendHeartRequest - 心跳发送成功  "+ aioSession.getSessionID());
//		} catch (IOException e) {
//			log.warn("OverTheAirProcessor::sendHeartRequest - 心跳发送失败  " + e.getMessage());
//			e.printStackTrace();
//		}
	}

	/**
	 * 判断心跳还是数据信息
	 */
	@Override
	public boolean isHeartMessage(AioSession aioSession, byte[] data) {
		if(data[3] != 113){
			return true;
		}

		return false;
	}
	
	/**
	 * 接收继电器返回的信息
	 */
	@Override
	public void process(AioSession aioSession, byte[] bytes) {
		try {
			// 继电器关闭返回的数据信息
			if(flag == 1){
				if(bytes[5] == 01){
					log.info("============继电器关闭完成==============");
					Thread.sleep(3000);
					getSession().writeBuffer().write(LidarUtil.opentARMBuff);
					getSession().writeBuffer().flush();
					flag = 0;
				}else{
					changeRelayStatus();
				}
			}
					
			// 继电器启动返回的数据信息
			if(flag ==0){
				if(bytes[5]==00){
					log.info("============继电器重启完成==============");
					//发送配置成功断开客户端
					aioQuickClient.shutdown();
					aioQuickClient = null;
					flag = -1;
				}else{
					getSession().writeBuffer().write(LidarUtil.opentARMBuff);
					getSession().writeBuffer().flush();
					flag = 0;
				}
				
			}
		} catch (Exception e) {
			log.error("process,{}",e);
		}
		
	}
	public static void sendData(byte[] data) {
		try {
			getSession().writeBuffer().write(data);
			getSession().writeBuffer().flush();
			System.out.println("发送到继电器的信息:"+ Arrays.toString(data));

		} catch (IOException e) {
			log.error("changeRelayStatus,{}",e);
		}

	}
	
	/***
	 * 
	 * @author 张宁
	 * @Description: 通过继电器控制ARM重启:
	 * @return void    返回类型
	 * @date 2022年6月29日 下午5:14:47
	 * @throws
	 */
	public static void changeRelayStatus() {
		try {
			getSession().writeBuffer().write(LidarUtil.closeARMBuff);
			getSession().writeBuffer().flush();
			flag = 1;
		} catch (IOException e) {
			log.error("changeRelayStatus,{}",e);
		}
	}
	
}
