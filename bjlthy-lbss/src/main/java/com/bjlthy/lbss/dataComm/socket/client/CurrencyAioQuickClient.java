package com.bjlthy.lbss.dataComm.socket.client;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickClient;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CurrencyAioQuickClient extends AbstractMessageProcessor<byte[]> {

    /**
     * Cilent客户端对象
     */
    private AioQuickClient aioQuickClient;

    /**
     * Client客户端通讯Session
     */
    private AioSession aioSession;

    /**
     * Client客户端通用方法接口对象
     */
    private CurrencyClientMethod currencyClientMethod;

    public AioQuickClient setReadBufferSize(int size) {
        aioQuickClient.setReadBufferSize(size);
        return aioQuickClient;
    }

    public void start() throws Exception {
        aioQuickClient.start();
    }

    /**
     * 获取AioQuickClient对象，使用默认“END$”作为消息解析结束符；使用默认心跳插件配置心跳每3秒发一次、超时时间30秒、每分钟的生命周期；
     *
     * @param SEVER_IP             连接服务端IP地址
     * @param SERVER_PORT          连接服务端端口号
     * @param currencyClientMethod 通用方法对象
     * @return AioQuickClient
     */
    public AioQuickClient getAioQuickClient(String SEVER_IP, int SERVER_PORT, final CurrencyClientMethod currencyClientMethod) {
        this.currencyClientMethod = currencyClientMethod;
        this.addPlugin(new HeartPlugin<byte[]>(3, 10, TimeUnit.SECONDS) {
            @Override
            public void sendHeartRequest(AioSession aioSession) throws IOException {
                currencyClientMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyClientMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickClient = new AioQuickClient(SEVER_IP, SERVER_PORT, new CurrencyClientProtocol("END$".getBytes()), this);
        return this.aioQuickClient;
    }

    /**
     * 获取AioQuickClient对象，使用默认“END$”作为消息解析结束符
     *
     * @param SEVER_IP             连接服务端IP地址
     * @param SERVER_PORT          连接服务端端口号
     * @param heartRate            自定义心跳插件每多少秒发送一次
     * @param timeout              自定义心跳插件超时时间，必须大于heartRate
     * @param unit                 自定义心跳插件生命周期
     * @param currencyClientMethod 通用方法对象
     * @return AioQuickClient
     */
    public AioQuickClient getAioQuickClient(String SEVER_IP, int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyClientMethod currencyClientMethod) {
        this.currencyClientMethod = currencyClientMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) throws IOException {
                currencyClientMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyClientMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickClient = new AioQuickClient(SEVER_IP, SERVER_PORT, new CurrencyClientProtocol("END$".getBytes()), this);
        return this.aioQuickClient;
    }

    /**
     * 获取AioQuickClient对象，可自定义设置单条解析结束符
     *
     * @param SEVER_IP             连接服务端IP地址
     * @param SERVER_PORT          连接服务端端口号
     * @param heartRate            自定义心跳插件每多少秒发送一次
     * @param timeout              自定义心跳插件超时时间，必须大于heartRate
     * @param unit                 自定义心跳插件生命周期
     * @param currencyClientMethod 通用方法对象
     * @param protocolBytes        自定义单条结束符
     * @return AioQuickClient
     */
    public AioQuickClient getAioQuickClient(String SEVER_IP, int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyClientMethod currencyClientMethod, byte[] protocolBytes) {
        this.currencyClientMethod = currencyClientMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) throws IOException {
                currencyClientMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyClientMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickClient = new AioQuickClient(SEVER_IP, SERVER_PORT, new CurrencyClientProtocol(protocolBytes), this);
        return this.aioQuickClient;
    }

    /**
     * 获取AioQuickClient对象，可自定义设置多条解析结束符
     *
     * @param SEVER_IP             连接服务端IP地址
     * @param SERVER_PORT          连接服务端端口号
     * @param heartRate            自定义心跳插件每多少秒发送一次
     * @param timeout              自定义心跳插件超时时间，必须大于heartRate
     * @param unit                 自定义心跳插件生命周期
     * @param currencyClientMethod 通用方法对象
     * @param protocolBytesList    自定义多条结束符结果集
     * @return AioQuickClient
     */
    public AioQuickClient getAioQuickClient(String SEVER_IP, int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyClientMethod currencyClientMethod, List<byte[]> protocolBytesList) {
        this.currencyClientMethod = currencyClientMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) throws IOException {
                currencyClientMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyClientMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickClient = new AioQuickClient(SEVER_IP, SERVER_PORT, new CurrencyClientProtocol(protocolBytesList), this);
        return this.aioQuickClient;
    }

    /**
     * 获取AioQuickClient对象，可自定义Protocol解析类
     *
     * @param SEVER_IP             连接服务端IP地址
     * @param SERVER_PORT          连接服务端端口号
     * @param heartRate            自定义心跳插件每多少秒发送一次
     * @param timeout              自定义心跳插件超时时间，必须大于heartRate
     * @param unit                 自定义心跳插件生命周期
     * @param currencyClientMethod 通用方法对象
     * @param protocol             自定义消息解析类对象
     * @return AioQuickClient
     */
    public AioQuickClient getAioQuickClient(String SEVER_IP, int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyClientMethod currencyClientMethod, Protocol<byte[]> protocol) {
        this.currencyClientMethod = currencyClientMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) throws IOException {
                currencyClientMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyClientMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickClient = new AioQuickClient(SEVER_IP, SERVER_PORT, protocol, this);
        return this.aioQuickClient;
    }

    /**
     * 发送数据至服务端
     *
     * @param bytes
     */
    public void sendDataToServer(byte[] bytes) throws IOException {
        WriteBuffer writeBuffer = this.aioSession.writeBuffer();
        writeBuffer.write(bytes);
        writeBuffer.flush();
    }

    public void sendDataEnd(byte[] bytes) throws IOException {
        byte[] ends = "END$".getBytes();
        byte[] res = new byte[bytes.length + ends.length];
        System.arraycopy(bytes, 0, res, 0, bytes.length);
        System.arraycopy(ends, 0, res, bytes.length, ends.length);
        WriteBuffer writeBuffer = this.aioSession.writeBuffer();
        writeBuffer.write(bytes);
        writeBuffer.flush();
    }

    /**
     * @param aioSession
     * @param bytes
     */
    public void process0(AioSession aioSession, byte[] bytes) {
        currencyClientMethod.process(aioSession, bytes);
    }

    /**
     * @param aioSession
     * @param stateMachineEnum
     * @param throwable
     */
    public void stateEvent0(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        this.aioSession = aioSession;
        currencyClientMethod.stateEvent(aioSession, stateMachineEnum, throwable);
    }

    public void shutdown() {
        this.aioQuickClient.shutdown();
    }
}
