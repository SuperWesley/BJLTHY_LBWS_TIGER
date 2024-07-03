package com.bjlthy.lbss.dataComm.socket.webSocket;

import com.bjlthy.lbss.tool.ConfigBean;
import com.bjlthy.lbss.tool.LogbackUtil;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;

/**
 * @version: V1.0
 * @author: 张宁
 * @description: 皮带机 电脑端实时数据显示 WebSocket
 * @date: 2020-12-05
 * @copyright: 北京龙田华远科技有限公司
 */
@Component
@ServerEndpoint("/ws/zy/beltWebSocket/{belt_name}")
public class BeltWebSocket {
    /**
     * 在线数量
     */
    private static int onlineCount = 0;


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
        BeltWebSocket.onlineCount++;
    }

    /**
     * 减去在线数量
     */
    public static synchronized void subOnlineCount() {
        BeltWebSocket.onlineCount--;
    }

    /**
     * 心跳消息
     */
    private final static String PING = "ping";

    private final static String PONG = "pong";

    /**
     * websocket集合 concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     * 若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
     */
    private static ConcurrentHashMap<String, BeltWebSocket> webSocketMap  = new ConcurrentHashMap<>();
    //日志工具类
    private static Logger log = LogbackUtil.getLogger("ZYBeltWebSocket", "ZYBeltWebSocket");

    /**
     * webSocketSession 与某个客户端的连接会话，需要通过它来给客户端发送数据 websocket
     */
    private static Session session;
    /**
     * 接收userid
     */
    private String belt_name = "";

    /**
     * @author: 张宁
     * @description: 打开
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnOpen
    public void onOpen(Session session,@PathParam("belt_name") String belt_name) throws IOException, ExecutionException, InterruptedException {
        this.session = session;
        this.belt_name = belt_name;
        webSocketMap.put(belt_name,this);
        addOnlineCount();           //在线数加1
        // readFile();
    }

    /**
     * @author: 张宁
     * @description: 关闭
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnClose
    public void onClose() {
        if(webSocketMap.containsKey(belt_name)){
            webSocketMap.remove(belt_name);
            //从set中删除
            subOnlineCount();
        }
    }

    /**
     * @author: 张宁
     * @description: 错误
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    /**
     * @author: 张宁
     * @description: 消息 触发
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        //消息保存到数据库、redis
        if (PING.equals(message)) {
            try {
                this.sendMessage(PONG);
            } catch (IOException e) {
                log.warn("onMessage,{}",e.toString());
            }
        }
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        synchronized (session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * @author: 张宁
     * @description: 发送消息到多个客户端
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    public static synchronized  void sendMessageToAllClient(String message) {
        try {
            for (Map.Entry<String, BeltWebSocket> entry : webSocketMap.entrySet()) {
                BeltWebSocket webSocketServer = entry.getValue();
                webSocketServer.sendMessage(message);
            }
        }catch (IOException e){
            log.warn("sendMessageToAllClient,{}",e.toString());
        }
    }

    /**
     * @author: 张宁
     * @description: 发送消息到多个客户端
     * @param: 参数
     * @return: 返回类型
     * @throws:
     */
    public static synchronized  void sendMessageToClient(String belt_name,String message)  {
        try {
            synchronized (BeltWebSocket.class){
                String belt_name_en = ConfigBean.beltsMap.get(belt_name).getBelt_name_en();
                if (webSocketMap.containsKey(belt_name_en)){
                    webSocketMap.get(belt_name_en).sendMessage(message);
                }
            }
        }catch (Exception e){
            log.warn("sendMessageToClient,{}",e.toString());
        }
    }

}
