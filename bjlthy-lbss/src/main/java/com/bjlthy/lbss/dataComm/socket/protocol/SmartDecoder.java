package com.bjlthy.lbss.dataComm.socket.protocol;

import java.nio.ByteBuffer;

public interface SmartDecoder {

	/**
	 * @author 三刀
	 * @version V1.0 , 2018/1/28
	 */
    boolean decode(ByteBuffer byteBuffer);

    ByteBuffer getBuffer();
}
