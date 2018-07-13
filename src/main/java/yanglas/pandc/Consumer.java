package yanglas.pandc;

import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者
 */
public class Consumer implements Runnable {
     private BlockingDeque<TheData> queue;
    private static AtomicInteger counts = new AtomicInteger();
    private static int SLEEPTIME = 1000;

    public Consumer(BlockingDeque<TheData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
          Random r = new Random();//为了随机生成等待时间
        System.out.println("start consumer id = "+Thread.currentThread().getId());
        try{
            while (true){
                //从缓冲队列获取数据
                TheData data = queue.take();
                if(data != null){
                    //平方
                    Double sqr = Math.pow(data.getIntData(),2);
                    System.out.println("consumer cost:"+sqr);
                    Thread.sleep(r.nextInt(SLEEPTIME));
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
