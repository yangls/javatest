package yanglas.pandc;

import java.util.concurrent.*;

public class RunMain {
    public static void main(String[] args) throws InterruptedException {
        //1共享队列建立
        BlockingDeque<TheData> blockingDeque = new LinkedBlockingDeque<>(10);//容纳10个
        //2.生产者
        Producter p1 = new Producter(blockingDeque);
        Producter p2 = new Producter(blockingDeque);
        Producter p3 = new Producter(blockingDeque);
        //3.消费者
        Consumer c1 = new Consumer(blockingDeque);
        Consumer c2 = new Consumer(blockingDeque);
        Consumer c3 = new Consumer(blockingDeque);
        //4.线程池
        ExecutorService executor = Executors.newCachedThreadPool();
        //5.线程运行
        executor.execute(p1);
        executor.execute(p2);
        executor.execute(p3);
        executor.execute(c1);
        executor.execute(c2);
        executor.execute(c3);
        Thread.sleep(10000);
        //停止生产
        p1.stop();
        p2.stop();
        p3.stop();
        //等待消费
        Thread.sleep(3000);
        executor.shutdown();
    }
}
