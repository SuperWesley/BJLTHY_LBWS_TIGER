package com.bjlthy.lbss.dataComm.socket.protocol;

import org.apache.commons.collections4.queue.CircularFifoQueue;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 指定结束标识的解码器
 *
 * @author 三刀
 * @version V1.0 , 2017/10/20
 */
public class DelimiterFrameDecoder implements SmartDecoder {
    /**
     * 消息结束标志
     */
    private byte[] endFLag;
    /**
     * 期望本次校验的结束标索引位置
     */
    private int exceptIndex;
    /**
     * 存储已解析的数据
     */
    private List<ByteBuffer> bufferList;
    /**
     * 是否解析完成
     */
    private boolean finishRead;

    /**
     * 按照帧尾长度进行数据缓存
     */
    private CircularFifoQueue<Byte> checkbBffer;

    private int position;

    public DelimiterFrameDecoder(byte[] endFLag) {
        this.endFLag = endFLag;
        bufferList = new ArrayList<>();
        bufferList.add(ByteBuffer.allocate(endFLag.length));
        checkbBffer = new CircularFifoQueue<Byte>(endFLag.length);
    }

    public boolean decode(ByteBuffer byteBuffer) {
        if (finishRead) {
            throw new RuntimeException("delimiter has finish read");
        }
        ByteBuffer preBuffer = bufferList.get(position);

        while (byteBuffer.hasRemaining()) {
            if (!preBuffer.hasRemaining()) {
                preBuffer.flip();
                position++;
                if (position < bufferList.size()) {
                    preBuffer = bufferList.get(position);
                    preBuffer.clear();
                } else {
                    preBuffer = ByteBuffer.allocate(preBuffer.capacity());
                    bufferList.add(preBuffer);
                }
            }
            byte data = byteBuffer.get();
            preBuffer.put(data);
            checkbBffer.add(data);
            byte[] compareArray = new byte[checkbBffer.size()];
            for(int i = 0 ; i < checkbBffer.size(); i++){
                compareArray[i] = checkbBffer.get(i);
            }
            if (!new String(endFLag).equals(new String(compareArray))) {
                continue;
            } else if (new String(endFLag).equals(new String(compareArray))) {
                preBuffer.flip();
                finishRead = true;
                break;
            }
        }

        return finishRead;
    }

    @Override
    public ByteBuffer getBuffer() {
        if (position == 0) {
            return bufferList.get(position);
        }
        byte[] data = new byte[(position) * bufferList.get(0).capacity() + bufferList.get(position).limit()];
        int index = 0;
        for (int i = 0; i < position; i++) {
            ByteBuffer b = bufferList.get(i);
            System.arraycopy(b.array(), b.position(), data, index, b.remaining());
            index += b.remaining();
        }
        ByteBuffer lastBuffer = bufferList.get(position);
        System.arraycopy(lastBuffer.array(), lastBuffer.position(), data, index, lastBuffer.remaining());
        return ByteBuffer.wrap(data);
    }

    /**
     * 重置解码器
     */
    public void reset() {
        reset(null);
    }

    /**
     * 重置解码器
     *
     * @param endFLag 更新结束标志
     */
    public void reset(byte[] endFLag) {
        if (endFLag != null) {
            this.endFLag = endFLag;
        }
        finishRead = false;
        exceptIndex = 0;
        position = 0;
        bufferList.get(position).clear();
    }
}
