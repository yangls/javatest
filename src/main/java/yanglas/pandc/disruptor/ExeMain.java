package yanglas.pandc.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExeMain {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        DataFactory factory = new DataFactory();
        int bufferSize = 1024;//要求是2的倍数
        //disruptor框架使用
        //创建数据源的工厂，环缓存的大小，线程池，生产策略（单个还是多个生产者），等待策略
        Disruptor<Data> disruptor = new Disruptor<Data>(factory,bufferSize,executorService, ProducerType.MULTI,new BlockingWaitStrategy());

        disruptor.handleEventsWithWorkerPool(new Consumer(),new Consumer(),new Consumer(),new Consumer());

        disruptor.start();
        //框架的环形队列获取，完全的cas操作
        RingBuffer<Data> ringBuffer = disruptor.getRingBuffer();
        //生产者依赖
        Producter producer = new Producter(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for(long l=0;true;l++){
            bb.putLong(0,l);
            producer.pushData(bb);
            Thread.sleep(1000);
            System.out.println("add data:"+l);
        }
    }
}
