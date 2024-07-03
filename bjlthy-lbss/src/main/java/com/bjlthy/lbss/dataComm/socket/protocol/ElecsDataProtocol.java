package com.bjlthy.lbss.dataComm.socket.protocol;

import org.smartboot.socket.Protocol;
import org.smartboot.socket.transport.AioSession;

import java.nio.ByteBuffer;

/**
 * 
 * @version V1.0
 * @author 张宁
 * @description 解析皮带电子秤通信数据接口
 * @date 2020年11月16日 下午4:52:17 @copyright(c) 北京龙田华远科技有限公司
 *
 */
public class ElecsDataProtocol implements Protocol<byte[]> {

	private static final int INT_LENGTH = 4;

	// 结束符\r\n
	private static final byte[] DELIMITER_BYTES = new byte[] { 'E', 'N', 'D', '$' };

	/**
	 * 解包
	 */
	@Override
	public byte[] decode(ByteBuffer buffer, AioSession session) {
		// TODO Auto-generated method stub

		DelimiterFrameDecoder delimiterFrameDecoder;
		if (session.getAttachment() == null) {// 构造指定结束符的临时缓冲区
			delimiterFrameDecoder = new DelimiterFrameDecoder(DELIMITER_BYTES);
			session.setAttachment(delimiterFrameDecoder);// 缓存解码器已应对半包情况
		} else {
			delimiterFrameDecoder = session.getAttachment();
		}

		// 未解析到DELIMITER_BYTES则返回null
		if (!delimiterFrameDecoder.decode(buffer)) {
			return null;
		}
		// 解码成功
		ByteBuffer byteBuffer = delimiterFrameDecoder.getBuffer();
		byte[] bytes = new byte[byteBuffer.remaining()];
		byteBuffer.get(bytes);
		session.setAttachment(null);// 释放临时缓冲区

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
