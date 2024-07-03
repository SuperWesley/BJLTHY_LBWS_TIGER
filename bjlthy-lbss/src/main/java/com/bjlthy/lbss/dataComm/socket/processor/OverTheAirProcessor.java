package com.bjlthy.lbss.dataComm.socket.processor;

import com.bjlthy.lbss.dataComm.socket.protocol.OverTheAirProtocol;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyAioQuickServer;
import com.bjlthy.lbss.dataComm.socket.server.CurrencyServerMethod;
import com.bjlthy.lbss.tool.*;
import jakarta.websocket.*;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 皮带OTA网络通信
 * @date 2021年2月16日 上午11:28:14
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class OverTheAirProcessor implements CurrencyServerMethod {

	private  AioSession session;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private  CopyOnWriteArraySet<OverTheAirProcessor> webSocketSet = new CopyOnWriteArraySet<OverTheAirProcessor>();
	//与某个客户端的连接会话，需要通过它来给客户端发送数据 websocket 
	private Session socketSession;
	
	private Map<AioSession, Long> sessionMap = new HashMap<>();
	
	public AioSession getSession() {
		return session;
	}
	
	public void setSession(AioSession session) {
		this.session = session;
	}

	//日志工具类
	private static Logger log = LogbackUtil.getLogger("OverTheAirProcessor", "OverTheAirProcessor");

	 /**
     * 处理接收到的消息
     *
     * @param session 通信会话
     * @param data     待处理的业务消息
     */
	@Override
	public void process(AioSession session, byte[] data) {
		//指令(1)+报文长度(2)+报文序号(2)+设备ID(6)+数据类型(2)+数长(2)+具体数据
		try {
			//01注册（保活）
			if (data[0] == 01) {
				byte[] buff = new byte[2];
				System.arraycopy(data, 13, buff, 0, buff.length);
				int sj = byteArrayToShort(buff, 0);// 数据长度
				byte[] buff1 = new byte[data.length - 15];// 数据
				System.arraycopy(data, 15, buff1, 0, buff1.length);
				if (sj == buff1.length) {
					// 返回数据帧信息
					registInfo(session,data);
				}
			}
			//03时间校准
			else if(data[0]==03){
				//返回数据帧信息
				checkDate(session,data);
			}
		
		} catch (IOException e) {
			log.error("OverTheAirProcessor:process 数据处理失败,{}  ",e);
		}
		
	}
	
	@Override
	public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
		
		switch (stateMachineEnum) {
		
		case NEW_SESSION:
			sessionMap.put(aioSession, System.currentTimeMillis());
			log.info("OverTheAirProcessor::stateEvent ,{} "+stateMachineEnum.toString());
			break;
		case SESSION_CLOSED:
			sessionMap.remove(aioSession);
			log.warn("OverTheAirProcessor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x00000F)");
			break;
		default:
			sessionMap.remove(aioSession);
			log.warn("OverTheAirProcessor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x00000F)");
			break;
		}
	}
	
	/**
	 * 开启OTA通讯
	 */
	public static void openOTAserver(){
		
		CurrencyAioQuickServer aioQuickServer = new CurrencyAioQuickServer();
		try {
			aioQuickServer.getAioQuickServer(ConfigBean.OTA_PORT,1,30,TimeUnit.SECONDS, new OverTheAirProcessor(),new OverTheAirProtocol());
			aioQuickServer.setReadBufferSize(2048);
			aioQuickServer.start();
		} catch (Exception e) {
			log.error("OverTheAirProcessor::openOTAserver - Address already in use,{} " ,e);
		}
		
	}
	
	/**
	 * 发送心跳消息
	 */
	@Override
	public void sendHeartRequest(AioSession aioSession) {
		// TODO Auto-generated method stub
		String msg = "timer";
		try {
			aioSession.writeBuffer().write(msg.getBytes());
			aioSession.writeBuffer().flush();
			log.info("OverTheAirProcessor::sendHeartRequest - 心跳发送成功 ,{} ", aioSession.getSessionID());
		} catch (IOException e) {
			log.warn("OverTheAirProcessor::sendHeartRequest - 心跳发送失败,{}  " , e);
		}
	}

	/**
	 * 判断心跳还是数据信息
	 */
	@Override
	public boolean isHeartMessage(AioSession aioSession, byte[] data) {
		// TODO Auto-generated method stub
		// 判断是否是服务端响应的心跳数据
		try {
			if (data[0] == 01) {
				byte[] buff = new byte[2];
				System.arraycopy(data, 13, buff, 0, buff.length);
				int sj = byteArrayToShort(buff, 0);// 数据长度
				byte[] buff1 = new byte[data.length - 15];// 数据
				System.arraycopy(data, 15, buff1, 0, buff1.length);
				if (sj == buff1.length) {
					// 返回数据帧信息
					registInfo(aioSession,data);
				}
				return true;
			}
		} catch (IOException e) {
			log.info("OverTheAirProcessor::isHeartMessage - 心跳发送成功 ,{} ", aioSession.getSessionID());
		}
		
		return false;
	}

	
	/**
	 * 当网络连接建立时触发该事件
	 * @param session
	 * @throws IOException
	 * @throws ExecutionException
	 * @throws InterruptedException
	 */
	@OnOpen
	public void onOpen(Session session) throws IOException, ExecutionException, InterruptedException{
		this.socketSession = session;
	}
	/**
	 * 连接关闭调用的方法
	 * @throws InterruptedException 
	 * @throws ExecutionException 
	 * @throws IOException 
	 */
	@OnClose
	public void onClose(){
		webSocketSet.remove(this);  //从set中删除
	}
	
	/**
	 * 发生错误时调用
	 * @param session
	 * @param error
	 */
	@OnError
	public void onError(Session session, Throwable error){
		error.printStackTrace();
	}
	
	/**
	 * 收到客户端消息后调用的方法
	 * @param message 客户端发送过来的消息
	 * @param session 可选的参数
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@OnMessage
	public void onMessage(String message, Session session) throws InterruptedException, IOException {
		if(webSocketSet ==null){
			return ;
		}
		
	}


	/**
	 * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
	 * @param message
	 * @throws IOException
	 */
	public void sendMessage(String message) throws IOException{
		this.socketSession.getBasicRemote().sendText(message);
	}
	
	
	/**
	 * short类型转byte[]数组，地在前高在后
	 * @param s
	 * @return
	 */
	public static byte[] unsignedShortToByte2(short s) {
		byte[] targets = new byte[2];
		targets[0] = (byte) (0xFF & s );
		targets[1] = (byte) (0x00FF & (s >> 8));
		return targets;
	}
	/**
	 * byte[]数组转short类型，得到字节数组长度
	 * @param bytes
	 * @param startIndex
	 * @return
	 */
	public static short byteArrayToShort(byte[] bytes, int startIndex) {  
		byte high = bytes[startIndex+1];
        byte low = bytes[startIndex];
        short z = (short)(((high & 0xFF) << 8) | (0xFF & low));
        return z;
	}
	/**
	 * 注册保活信息返回的数据帧
	 * @param data
	 * @throws IOException 
	 */
	public void registInfo(AioSession aioSession,byte[] data) throws IOException{
		byte[] buff = new byte[11];
		//功能码
		buff[0] = (byte) 0x81;
		//报文长度
		buff[1] = 0x08;
		buff[2] = 0x00;
		//报文序号
		buff[3] = 0x00;
		buff[4] = 0x00;
		//设备ID
		buff[5] = data[5];
		buff[6] = data[6];
		buff[7] = data[7];
		buff[8] = data[8];
		buff[9] = data[9];
		buff[10] = data[10];
		aioSession.writeBuffer().write(buff);
		aioSession.writeBuffer().flush();

	}
	/**
	 * 时间校准返回的数据帧
	 * @param data
	 * @throws IOException 
	 */
	public static  void checkDate(AioSession aioSession,byte[] data) {
		byte[] buff = new byte[21];
		//功能码
		buff[0] = (byte) 0x83;
		//报文长度
		buff[1] = 0x12;
		buff[2] = 0x00;
		//报文序号
		buff[3] = 0x00;
		buff[4] = 0x00;
		//设备ID
		buff[5] = data[5];
		buff[6] = data[6];
		buff[7] = data[7];
		buff[8] = data[8];
		buff[9] = data[9];
		buff[10] = data[10];
		//数据类型
		buff[11] = 0x01;
		buff[12] = 0x00;
		//数据长度
		buff[13] = 0x06;
		buff[14] = 0x00;
		//时间
		Calendar calendar = Calendar.getInstance();
		int now_y = calendar.get(Calendar.YEAR)-2000;//得到年份
		int now_m = calendar.get(Calendar.MONTH)+1;//得到月份
		int now_d = calendar.get(Calendar.DATE);//得到月份中今天的号数
		int now_h = calendar.get(Calendar.HOUR_OF_DAY);//得到一天中现在的时间，24小时制
		int now_mm = calendar.get(Calendar.MINUTE);//得到分钟数
		int now_s = calendar.get(Calendar.SECOND);//得到秒数
		buff[15] = (byte) now_y;
		buff[16] = (byte) now_m;
		buff[17] = (byte) now_d;
		buff[18] = (byte) now_h;
		buff[19] = (byte) now_mm;
		buff[20] = (byte) now_s;
		try {
			aioSession.writeBuffer().write(buff);
			aioSession.writeBuffer().flush();
			String timeStrH = new SimpleDateFormat("yyyy-MM-dd_HH").format(new Date());
			String day = DateUtil.getDate();
			FileUtil.writeFileAndTime(PathUtil.otaPath+ day+"/"+timeStrH+"_ota.txt", "BeltOverTheAirProcessor---->"+Arrays.toString(buff));
		}catch (Exception e){
			log.warn("checkDate,{}",e);
		}
	}
}
