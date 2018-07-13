package yanglas;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 测试资源竞争问题
 */
public class ThreadLocalRandom {

    private  static final int MAX_COUNTS= 10000;
    private static final int THREADS = 4;
    static ExecutorService executor = Executors.newFixedThreadPool(THREADS);
    //竞争获取有性能损失
    public static Random rnd = new Random(123);

    public static ThreadLocal<Random> tl = ThreadLocal.withInitial(()->new Random(123));

    public static class RndTask implements Callable<Long>{
        private int mode = 0;

        public RndTask(int mode) {
            this.mode = mode;
        }

        public Random getRandom(){
            if(mode == 0){
                return rnd;
            }else if(mode == 1){
                return tl.get();
            }
            return null;
        }

        @Override
        public Long call() throws Exception {
            long b = System.currentTimeMillis();
            for(long i =0;i<MAX_COUNTS;i++){
                getRandom().nextInt();
            }
            long e = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+" spend "+(e-b)+" ms");
            return e-b;
        }
    }

    public  static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futs = new Future[THREADS];
        for(int i =0;i<THREADS; i++){
                futs[i] = executor.submit(new RndTask(0));
        }
        System.out.println("单random耗时为:"+ cacularTime(futs));

        for(int i =0;i<THREADS; i++){
            futs[i] = executor.submit(new RndTask(1));
        }
        System.out.println("localRandom耗时为:"+ cacularTime(futs));
    }

    private static Long cacularTime(Future<Long>[] futs) throws ExecutionException, InterruptedException {
        long time = 0;
        for(Future<Long> f:futs){
            time +=f.get();
        }
        return time;
    }
}
