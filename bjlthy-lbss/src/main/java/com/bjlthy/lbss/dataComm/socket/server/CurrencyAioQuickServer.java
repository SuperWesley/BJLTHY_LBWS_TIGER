package com.bjlthy.lbss.dataComm.socket.server;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.extension.plugins.HeartPlugin;
import org.smartboot.socket.extension.processor.AbstractMessageProcessor;
import org.smartboot.socket.transport.AioQuickServer;
import org.smartboot.socket.transport.AioSession;
import org.smartboot.socket.transport.WriteBuffer;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CurrencyAioQuickServer extends AbstractMessageProcessor<byte[]> {

    private AioQuickServer aioQuickServer;

    private AioSession aioSession;

    private CurrencyServerMethod currencyServerMethod;

    public CurrencyAioQuickServer() {
    }

    // public CurrencyAioQuickServer(int SERVER_PORT, CurrencyServerMethod currencyServerMethod) {
    //     this.currencyServerMethod = currencyServerMethod;
    //     getAioQuickServer(SERVER_PORT, currencyServerMethod);
    // }

    public AioSession getAioSession() {
        return aioSession;
    }

    public void setAioSession(AioSession aioSession) {
        this.aioSession = aioSession;

    }

    public AioQuickServer setReadBufferSize(int size) {
        aioQuickServer.setReadBufferSize(size);
        return aioQuickServer;
    }

    public void start() throws Exception {
        aioQuickServer.start();
    }

    public void shutdown() {
        aioQuickServer.shutdown();
    }

    /**
     * 获取AioQuickServer对象，使用默认“END$”作为消息解析结束符
     *
     * @param SERVER_PORT          服务端服务端口
     * @param currencyServerMethod 通用方法对象
     * @return AioQuickServer
     */
    public AioQuickServer getAioQuickServer(int SERVER_PORT, CurrencyServerMethod currencyServerMethod) {
        this.currencyServerMethod = currencyServerMethod;
        this.addPlugin(new HeartPlugin<byte[]>(3, 30, TimeUnit.SECONDS) {
            @Override
            public void sendHeartRequest(AioSession aioSession) {
                currencyServerMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyServerMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickServer = new AioQuickServer(SERVER_PORT, new CurrencyServerProtocol("END$".getBytes()), this);
        return this.aioQuickServer;

    }

    /**
     * 获取AioQuickServer对象，可自定义设置单条解析结束符
     *
     * @param SERVER_PORT          服务端服务端口
     * @param currencyServerMethod 通用方法对象
     * @param protocolBytes        自定义单条结束符
     * @return AioQuickServer
     */
    public AioQuickServer getAioQuickServer(int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, CurrencyServerMethod currencyServerMethod, byte[] protocolBytes) {
        this.currencyServerMethod = currencyServerMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) {
                currencyServerMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyServerMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickServer = new AioQuickServer(SERVER_PORT, new CurrencyServerProtocol(protocolBytes), this);
        return this.aioQuickServer;
    }

    /**
     * 获取AioQuickServer对象，可自定义设置多条解析结束符
     *
     * @param SERVER_PORT          服务端服务端口
     * @param currencyServerMethod 通用方法对象
     * @param protocolBytesList    自定义多条结束符结果集
     * @return AioQuickServer
     */
    public AioQuickServer getAioQuickServer(int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyServerMethod currencyServerMethod, List<byte[]> protocolBytesList) {
        this.currencyServerMethod = currencyServerMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) {
                currencyServerMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyServerMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickServer = new AioQuickServer(SERVER_PORT, new CurrencyServerProtocol(protocolBytesList), this);
        return this.aioQuickServer;
    }

    /**
     * 获取AioQuickServer对象，可自定义Protocol解析类
     *
     * @param SERVER_PORT          服务端服务端口
     * @param currencyServerMethod 通用方法对象
     * @param protocol             自定义消息解析类对象
     * @return AioQuickServer
     */
    public AioQuickServer getAioQuickServer(int SERVER_PORT, int heartRate, int timeout, TimeUnit unit, final CurrencyServerMethod currencyServerMethod, Protocol<byte[]> protocol) {
        this.currencyServerMethod = currencyServerMethod;
        this.addPlugin(new HeartPlugin<byte[]>(heartRate, timeout, unit) {
            @Override
            public void sendHeartRequest(AioSession aioSession) {
                currencyServerMethod.sendHeartRequest(aioSession);
            }

            @Override
            public boolean isHeartMessage(AioSession aioSession, byte[] bytes) {
                return currencyServerMethod.isHeartMessage(aioSession, bytes);
            }
        });
        this.aioQuickServer = new AioQuickServer(SERVER_PORT, protocol, this);
        return this.aioQuickServer;
    }

    /**
     * 发送数据至客户端
     *
     * @param bytes
     */
    public void sendDataToClient(byte[] bytes) throws IOException {
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

    @Override
    public void process0(AioSession aioSession, byte[] bytes) {
        currencyServerMethod.process(aioSession, bytes);
    }

    @Override
    public void stateEvent0(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable) {
        switch (stateMachineEnum) {
            case NEW_SESSION:
                this.aioSession = aioSession;
                break;
            case SESSION_CLOSED:
            	aioSession.close();
                this.aioSession = null;
                break;
            default:
                this.aioSession = null;
                break;
        }
        currencyServerMethod.stateEvent(aioSession, stateMachineEnum, throwable);
    }
}
