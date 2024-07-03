package com.bjlthy.lbss.dataComm.socket.processor.lidar;

import com.alibaba.fastjson.JSON;
import com.bjlthy.common.core.domain.AjaxResult;
import com.bjlthy.common.utils.StringUtils;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyAioQuickClient;
import com.bjlthy.lbss.dataComm.socket.client.CurrencyClientMethod;
import com.bjlthy.lbss.dataComm.socket.protocol.BeltLidarProtocol;
import com.bjlthy.lbss.dataComm.socket.webSocket.LidarStateWebSocket;
import com.bjlthy.lbss.tool.*;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangning
 * @version V1.0
 * @description 接收皮带机激光雷达数据
 * @date 2020-11-05
 * @copyright(c) 北京龙田华远科技有限公司
 */
@Component
@ServerEndpoint("/ws/2/beltLidarDataWebSocket")
public class BeltLidar2Processor implements CurrencyClientMethod {

	 /**
     * 与雷达连接会话
     **/
	private static AioSession session;
    private  static  long count =1;
    
    /**
     * 在线数量
     */
    private static int onlineCount = 0;
    
    public static  AioQuickClient aioQuickClient = null;
	//concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
	private static CopyOnWriteArraySet<BeltLidar2Processor> webSocketSet = new CopyOnWriteArraySet<BeltLidar2Processor>();

    //日志工具类
    private static Logger log = LogbackUtil.getLogger("BeltLidarProcessor", "BeltLidar2Processor");

	private Session socketSession;
    public static Map<String, AioSession> sessionMap = new ConcurrentHashMap<>();

    //雷达有污渍标识
	public static String blotFlag = "0";
    //皮带秤名称
    public static String belt_name = "西上仓";
    /**
     * @author 张宁
     * @description 打开雷达Socket
     * @date 2020年11月14日 下午3:11:06
     */
    public static void openBeltLidarClient(String LIDAR_IP) {
        try {
        	CurrencyAioQuickClient currencyAioQuickClient = new CurrencyAioQuickClient();
        	//雷达连接
        	aioQuickClient = currencyAioQuickClient.getAioQuickClient(LIDAR_IP, ConfigBean.LIDAR_PORT, 3, 30, TimeUnit.SECONDS, new BeltLidar2Processor(),new BeltLidarProtocol());
            aioQuickClient.setReadBufferSize(2048);
            aioQuickClient.start();
           
        } catch (Exception e) {
        	aioQuickClient.shutdown();
            log.error("BeltLidar2Processor:openBeltLidarClient 皮带雷达客户端连接失败,{}",e);
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
                sessionMap.put(aioSession.getSessionID(), aioSession);
                //获取雷达数据
                getLidarData(aioSession);
                log.info("BeltLidar2Processor::stateEvent ,{} "+stateMachineEnum.toString());
                break;
            case SESSION_CLOSED:
            	sendStateToWebBrowser("0","异常");
                sessionMap.remove(aioSession.getSessionID());
                aioSession.close();
                log.warn("BeltLidar2Processor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x000006)");
                break;
            default:
                log.warn("BeltLidar2Processor::stateEvent ,{} "+stateMachineEnum.toString()+"(0x000006)");
                break;
        }
    }

    /**
	 * 发送心跳消息
	 */
	@Override
	public void sendHeartRequest(AioSession aioSession) {
		// TODO Auto-generated method stub
//		try {
//            AioSession session = sessionMap.get(aioSession.getSessionID());
//            session.writeBuffer().write(LidarUtil.readDataBuff);
//            session.writeBuffer().flush();
//		} catch (IOException e) {
//            log.warn("BeltLidar2Processor::sendHeartRequest - 获取雷达数据失败 ,{} " , e);
//		}
	}

	/**
	 * 判断心跳还是数据信息
	 */
	@Override
	public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
		// TODO Auto-generated method stub
		String msgs = new String(bytes);
		if (!msgs.contains("sSN LMDscandata")) {
  		   return true;
  	    }
		return false;
	}
	
	/**
     * 处理接收到的消息
     *
     * @param aioSession 通信会话
     * @param bytes    待处理的业务消息
     */
	@Override
	public void process(AioSession aioSession, byte[] bytes) {
		// TODO Auto-generated method stub
		String msgs = new String(bytes);
		synchronized (BeltLidar2Processor.class) {
            if (msgs.indexOf("sSN LMDscandata") > -1) {
                // 数据清洗
                 String path = PathUtil.path+"/Belt/"+belt_name+PathUtil.bLidarPath;
                 saveLidarDataInfo(msgs,path);
                // 雷达状态每秒更新一次（雷达数据每秒50HZ）
                count++;
                if (count % 50 == 0) {
                    sendStateToWebBrowser("1", "正常");
                    count = 1;
                }
            }
        }
	}
	
	
	 /**
     * 获取雷达数据
     */
    public void getLidarData(AioSession aioSession){
        try {
            AioSession session = sessionMap.get(aioSession.getSessionID());
            session.writeBuffer().write(LidarUtil.readDataBuff);
            session.writeBuffer().flush();
        }catch (Exception e){
            log.error("BeltLidar2Processor:getLidarData ,{} ",e);
        }
    }

    /**
     * @param message
     * @author 张宁
     * @description 接收雷达数据并在页面展示
     * @date 2020年11月16日 下午4:12:17
     */
    public static void sendToWebBrowser(String message)  {
    	
    	for(BeltLidar2Processor item: webSocketSet){
    		try {
    			String json ="";
    			if (message.indexOf("1388 12D") > -1) {
    				String saveValueM = message.substring(message.indexOf("1388 12D"));
    				String saveValueN = saveValueM.substring(9, saveValueM.length() - 13);
    				json = saveValueN;
    			}
    			String[] jsonArray = json.split(" ");
    			
    			double[] y = {};
    			//数据转16进制进行组合处理
    			int length = jsonArray.length;
    			for (int i = 0; i < length; i++) {
    				if (StringUtils.isNotEmpty(jsonArray[i])) {
    					y = insertElement(y, Integer.parseInt(jsonArray[i], 16), i);
    				}
    			}
                int begin = DictionariesHelper.getDicIntegerValueByCode(belt_name+"-lidarBegin");
                int end = DictionariesHelper.getDicIntegerValueByCode(belt_name+"-lidarEnd");
    			//消息发送至界面
    			//雷达数据0值过滤
                if(y.length>137) {
                	//截取数据范围
                    y = Arrays.copyOfRange(y, begin+3,end-3);
                    int y_length = y.length;
                    double[][] arrays= new double[y_length][2];//汇总数组
                    //统计0值个数
                    int num=0;
					for(int i=0;i<y_length;i++){
						if( i>0 && Math.abs(y[i] - y[i-1]) > 140){
							double data = new BigDecimal(y[i-1]/Math.cos(0.5*Math.PI/180)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
							y[i]=data;
							num++;
						}else if(y[0]==0){
							double data = new BigDecimal(1088/Math.cos(0.5*Math.PI/180)).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
							y[i]=data;
						}
						arrays[i][0] = y[i];
						arrays[i][1] = 60.5 +i * 0.5;
					}
					//每帧0值个数超过10个，证明雷达玻璃表面有污渍，提示用户擦拭雷达
					if(num >10){
						checkLidarData();
					}else{
						blotFlag = "0";
					}
//                    System.out.println(JSON.toJSONString(arrays));
					//消息发送至界面
					item.socketSession.getBasicRemote().sendText(JSON.toJSONString(arrays));
                }
    		}catch (Exception e){
                log.error("BeltLidar2Processor:sendToWebBrowser 发送至上位机失败,{} ",e);
    			continue;
    		}
    	}

    }
    
    /**
     * 
     * @author 张宁
     * @Description: 检查雷达数据是否异常
     * @return void    返回类型
     * @date 2022年5月18日 上午11:48:53
     * @throws
     */
    public static void checkLidarData(){
    	//雷达有污渍标识
    	if("0".equals(blotFlag)){
    		Calendar calendar = new GregorianCalendar();
    		calendar.add(calendar.MINUTE, 10);
    		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		//记录10分钟后的时间
    		ConfigBean.recordTime = sdf.format(calendar.getTime());
    		//标识更改为1，雷达有污渍
			blotFlag = "1";
		}else{
			
			String currentTime = DateUtil.getDateTime();
			//判断雷达数据异常是否超过10分钟
			if(currentTime.compareTo(ConfigBean.recordTime)>0){
				sendStateToWebBrowser("2","激光雷达防爆玻璃表面有污渍请擦拭");
			}
		}
    }
    
    /**
     * @author 张宁
     * @description 发送雷达状态到web浏览器
     * @date 2020年11月16日 下午4:12:17
     */
    public static void sendStateToWebBrowser(String code,String msg) {
        AjaxResult ajaxResult = AjaxResult.success();
        ajaxResult.put("code", code);
        ajaxResult.put("msg", msg);
        ajaxResult.put("belt_name", belt_name);
        LidarStateWebSocket.sendMessageToClient(belt_name,JSON.toJSONString(ajaxResult));
    }

    
    /**
     * @param msg  数据信息
     * @param path 保存文件路径
     * @author 张宁
     * @description 保存雷达数据
     * @date 2020年11月16日 下午4:05:13
     */
    public void saveLidarDataInfo(String msg, String path) {
        try {
        	String[] parts = msg.split(new String(new byte[]{0x20, 0x30, 0x03}));
            for(String part : parts){
                if("".equals(part.trim())){
                    continue;
                }
                String str = part + new String(new byte[]{0x20, 0x30, 0x03});
                String[] params = str.split(" ");
                int year = hexStr2int(params[params.length - 8]);
                int month = hexStr2int(params[params.length - 7]);
                String monthStr = month + "";
                if(month < 10) {
                    monthStr = "0" + month;
                }
                int day = hexStr2int(params[params.length - 6]);
                String dayStr = day + "";
                if(day < 10) {
                    dayStr = "0" + day;
                }
                int hour = hexStr2int(params[params.length - 5]);
                String hourStr = hour + "";
                if(hour < 10) {
                    hourStr = "0" + hour;
                }
                int min = hexStr2int(params[params.length - 4]);
                String minStr = min + "";
                if(min < 10) {
                    minStr = "0" + min;
                }
                int sec = hexStr2int(params[params.length - 3]);
                String secStr = sec + "";
                if(sec < 10) {
                    secStr = "0" + sec;
                }
                int umsec = hexStr2int(params[params.length - 2]);
                String msecStr = (int)(umsec/1000) +"";
                while(msecStr.length() < 3){
                    msecStr = "0" + msecStr;
                }
                String fileDay = year + "-" + monthStr + "-" + dayStr;
                String timeStrH = year + "-" + monthStr + "-" + dayStr+"_" + hourStr;
                String time = year + "-" + monthStr + "-" + dayStr + " " + hourStr + ":" + minStr + ":" + secStr + "." + msecStr;
                FileUtil.writeFile(path + fileDay+"/"+timeStrH + "_laser.txt",time + " " + str);
                //接收雷达数据并在页面展示
                sendToWebBrowser(str);
            }
            
        } catch (Exception e) {
            log.warn("BeltLidar2Processor:saveLidarDataInfo 数据保存失败 ,{} ",e);
        }
    }

    /**
     * @param dims
     * @author 张宁
     * @description 组织雷达数据
     * @date 2020年11月16日 下午4:43:40
     */
    private static double[] insertElement(double[] dims, double element, int index) {
        int length = dims.length;
        double destination[] = new double[length + 1];
        System.arraycopy(dims, 0, destination, 0, index);
        destination[index] = element;
        System.arraycopy(dims, index, destination, index + 1, length - index);
        return destination;
    }
    
    public int hexStr2int(String hexString){
        int val = Integer.parseInt(hexString, 16);
        return val;
    }
    
    /**
     * @author: 张宁
     * @description: 页面访问时触发
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnOpen
    public void onOpen(Session session) throws IOException, ExecutionException, InterruptedException {

        this.socketSession = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
//         readFile();
    }
    /**
     * @author: 张宁
     * @description: 页面关闭时触发
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
    }
    /**
     * 获取在线数量
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 增加在线数量
     */
    public static synchronized void addOnlineCount() {
    	BeltLidar2Processor.onlineCount++;
    }

    /**
     * 减去在线数量
     */
    public static synchronized void subOnlineCount() {
    	BeltLidar2Processor.onlineCount--;
    }


	public static AioSession getSession() {
		return session;
	}


	public static void setSession(AioSession session) {
		BeltLidar2Processor.session = session;
	}
    
	   public static void readFile(){
		   File file = new File("E:\\现场数据\\兴隆庄\\2023-03-29_16_laser.txt");// Text文件
			String s = null;
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
				while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
					try {
						synchronized (BeltLidar2Processor.class) {
							sendToWebBrowser(s);
							sendStateToWebBrowser("1","正常");
						}
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				br.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	   }
}
