package com.bjlthy.lbss.dataComm.socket.client;

import org.smartboot.socket.StateMachineEnum;
import org.smartboot.socket.transport.AioSession;

public interface CurrencyClientMethod {

    public void process(AioSession aioSession, byte[] bytes);

    public void stateEvent(AioSession aioSession, StateMachineEnum stateMachineEnum, Throwable throwable);

    public void sendHeartRequest(AioSession aioSession);

    public boolean isHeartMessage(AioSession aioSession, byte[] bytes);
}
