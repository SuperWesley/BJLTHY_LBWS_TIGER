package com.bjlthy.lbss.dataComm.socket.protocol;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 解析皮带雷达数据通信接口
 * @date 2020年11月16日 下午4:52:17
 * @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class BeltLidarProtocol implements Protocol<byte[]> {
	
	private static final int INT_LENGTH = 4;
	
	//结束符\r\n
//    private static final byte[] DELIMITER_BYTES = new byte[]{32, 48, 32, 48, 32, 48, 32, 48, 32, 48, 32, 48, 3};
    
  //结束符\r\n
    private static final byte[] DELIMITER_BYTES = new byte[]{0x20, 0x30, 0x03};
	/**
	 * 解包
	 */
	@Override
	public byte[] decode(ByteBuffer buffer, AioSession session) {
		// TODO Auto-generated method stub


		DelimiterFrameDecoder delimiterFrameDecoder;
		if (session.getAttachment() == null) {//构造指定结束符的临时缓冲区
			delimiterFrameDecoder = new DelimiterFrameDecoder(DELIMITER_BYTES);
			session.setAttachment(delimiterFrameDecoder);//缓存解码器已应对半包情况
		} else {
			delimiterFrameDecoder = session.getAttachment();
		}

		//未解析到DELIMITER_BYTES则返回null
		if (!delimiterFrameDecoder.decode(buffer)) {
			return null;
		}
		//解码成功
		ByteBuffer byteBuffer = delimiterFrameDecoder.getBuffer();
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes);
		session.setAttachment(null);//释放临时缓冲区
		return bytes;

	}

	public ByteBuffer encode(String str, AioSession session) {
		// TODO Auto-generated method stub
		byte[] dd = str.getBytes();
		ByteBuffer b = ByteBuffer.allocate(INT_LENGTH);
		b.put(dd);
		b.flip();
		return b;
	}
}
