package com.bjlthy.lbss.dataComm.socket.processor.relay;

import com.bjlthy.lbss.dataComm.socket.client.CurrencyAioQuickClient;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyClientMethod;
import com.bjlthy.lbss.dataComm.socket.protocol.RelayProtocol;
import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class RelayProcessor implements CurrencyClientMethod {

	private static AioSession session;
	
	public static  AioQuickClient aioQuickClient = null;
	
	/** 发送状态标识，1关闭，0 开启  -1失效*/
	public static int flag = -1;
	//日志工具类
	private static Logger log = LogbackUtil.getLogger("RelayProcessor", "RelayProcessor");

	public static AioSession getSession() {
		return session;
	}

	public static void setSession(AioSession session) {
		RelayProcessor.session = session;
	}
	
	/**
	 * 重新启动继电器
	 */
	public static void openRelayClient(String ip) {
	    try {
	    	CurrencyAioQuickClient currencyAioQuickClient = new CurrencyAioQuickClient();
	    	//继电器
	    	aioQuickClient = currencyAioQuickClient.getAioQuickClient(ip, ConfigBean.REAL_PORT, 3, 30, TimeUnit.SECONDS, new RelayProcessor(),new RelayProtocol());
		    aioQuickClient.setReadBufferSize(2048);
		    aioQuickClient.start();
		  
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
            	//修改继电器Y3端口开关重启arm板
				//changeRelayStatus();
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
		System.out.println("接收到继电器反馈的信息:"+Arrays.toString(data));

		return false;
	}
	
	/**
	 * 接收继电器返回的信息
	 */
	@Override
	public void process(AioSession aioSession, byte[] bytes) {
		try {
			if(bytes[0] == 00 && flag ==0){
				log.info("============继电器关闭完成==============");
				aioQuickClient.shutdown();
				setSession(null);
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
	public static void changeRelayNanoStatus() {
		try {
			//关闭板子
			byte[] buff_06 = hexStringToByteArray("06");
			getSession().writeBuffer().write(buff_06);
			getSession().writeBuffer().flush();
			flag = 1;
			Thread.sleep(1000*30);
			//启动板子
			byte[] buff_05 = hexStringToByteArray("05");
			getSession().writeBuffer().write(buff_05);
			getSession().writeBuffer().flush();
			flag = 0;
		} catch (IOException | InterruptedException e) {
			log.error("changeRelayStatus,{}",e);
		}
		
	}
	
	
	 /**
     * 16进制表示的字符串转换为字节数组
     *
     * @param hexString 16进制表示的字符串
     * @return byte[] 字节数组
     */
    public static byte[] hexStringToByteArray(String hexString) {
        hexString = hexString.replaceAll(" ", "");
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character
                    .digit(hexString.charAt(i + 1), 16));
        }
        return bytes;
    }
}
