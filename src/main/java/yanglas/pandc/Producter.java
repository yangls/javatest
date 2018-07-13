package yanglas.pandc;


import java.util.Random;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 生产者
 */
public class Producter implements Runnable {

    private volatile boolean isRunning = true;
    private BlockingDeque<TheData> queue;
    private static AtomicInteger counts = new AtomicInteger();
    private static int SLEEPTIME = 1000;

    public Producter(BlockingDeque<TheData> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        //1.生成数据对象的说明
        TheData data = null;
        Random r = new Random();//为了随机生成等待时间
        System.out.println("start product id = "+Thread.currentThread().getId());
        try{
            while (isRunning){
                Thread.sleep(r.nextInt(SLEEPTIME));
                data = new TheData(counts.incrementAndGet());
                System.out.println(data +" is push in queue");
                if(!queue.offer(data,2,TimeUnit.SECONDS)){
                    System.err.println("failed to put data in queue:"+data);
                }
            }
        }catch (InterruptedException e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

    public void stop(){
        System.out.println("product "+Thread.currentThread().getId()+" stop product");
        isRunning = false;
    }
}
