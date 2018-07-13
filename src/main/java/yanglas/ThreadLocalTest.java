package yanglas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalTest {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();//线程持有的局部变量？

    private static AtomicInteger x = new AtomicInteger();

    public static class ParseDate implements Runnable{

        int i=0;
        public ParseDate(int i){
            this.i = i;
        }


        @Override
        public void run() {
            try{
                if(tl.get()==null){
                    //如果当前执行线程没有实例，新建个
                    System.out.println(x.incrementAndGet());
                    //threadMap 当前线程作为key，保存对象
                    tl.set( new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
                //threadMap 当前线程作为key，获取对象(每个线程获取的内容唯一)
                Date t = tl.get().parse("2028-03-03 22:22:"+i%60);
             //   Date t = sdf.parse("2028-03-03 22:22:"+i%60);

                System.out.println(i+":"+t);

                //线程池的threadLocal伴随线程存在，如果线程不回收对应threadLocal中存的内容也不会回收
                //threadLocal 用在对线程间使用共享变量容易出现激烈竞争关系导致性能下降严重时，很有效
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] agrs){
        //创建10个线程的线程池
        ExecutorService es = Executors.newFixedThreadPool(10);
        //要执行的数据
        for(int i=0;i<1000;i++){
            //需要执行的线程逻辑（总共1000个，让10个线程完成）
            es.execute(new ParseDate(i));
        }
    }
}
