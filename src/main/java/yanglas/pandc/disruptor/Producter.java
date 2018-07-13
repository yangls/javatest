package yanglas.pandc.disruptor;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class Producter {
    private RingBuffer<Data> ringBuffer;

    public Producter(RingBuffer<Data> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     * 将产生的数据放入缓存区
     * @param byteBuffer 可以包装任何类型的数据
     */
    public void pushData(ByteBuffer byteBuffer){

        long seq = ringBuffer.next();//获取下一个队列可用位置
        try{
            Data event = ringBuffer.get(seq);//获取队列位置的这个容器
            event.setValue(byteBuffer.getLong(0));
        }finally {
            //数据发布
            ringBuffer.publish(seq);
        }
    }
}
