package com.bjlthy.lbss.dataComm.socket.protocol;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;


/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 解析皮带OTA业务通信接口
 * @date 2021年2月18日 下午3:46:17
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class OverTheAirProtocol implements Protocol<byte[]>{

	/**
	 * 拆包过程
	 */
	@Override
	public byte[] decode(ByteBuffer readBuffer, AioSession session) {
		// TODO Auto-generated method stub
		int length = readBuffer.limit();
		
		if(length == readBuffer.position()) {
			return null;
		}
		
		byte[] dst = new byte[length];  
		
		readBuffer.get(dst, 0, length);
		
		
		return dst; 
	}
	
}
