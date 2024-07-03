package com.bjlthy.lbss.dataComm.socket.protocol;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

public class RelayProtocol implements Protocol<byte[]> {

private static final int INT_LENGTH = 4;
	
	@Override
	public byte[] decode(ByteBuffer readBuffer, AioSession session) {
		// TODO Auto-generated method stub
		int length = readBuffer.limit();
		
//		if (readBuffer.remaining() < INT_LENGTH) {
//            return null;
//        }
        
        byte[] bytes = new byte[length];
        readBuffer.get(bytes, 0, length);
        
        return bytes;
	}
}
