package com.bjlthy.lbss.dataComm.socket.protocol;

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
public class FixedLengthFrameDecoder implements SmartDecoder {
	private ByteBuffer buffer;
    private boolean finishRead;

    public FixedLengthFrameDecoder(int frameLength) {
        if (frameLength <= 0) {
            throw new IllegalArgumentException("frameLength must be a positive integer: " + frameLength);
        } else {
            buffer = ByteBuffer.allocate(frameLength);
        }
    }

    public boolean decode(ByteBuffer byteBuffer) {
        if (finishRead) {
            throw new RuntimeException("delimiter has finish read");
        }
        if (buffer.remaining() >= byteBuffer.remaining()) {
            buffer.put(byteBuffer);
        } else {
            int limit = byteBuffer.limit();
            byteBuffer.limit(byteBuffer.position() + buffer.remaining());
            buffer.put(byteBuffer);
            byteBuffer.limit(limit);
        }

        if (buffer.hasRemaining()) {
            return false;
        }
        buffer.flip();
        finishRead = true;
        return true;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
