package yanglas;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * fork/join
 * RecursiveTask 是 ForkJoinTask的子类，带有返回值的
 */
public class CountTask extends RecursiveTask<Long> {
    //任务分解的规模
    private static final int THRESHOLD = 10000;

    private long start;
    private long end;

    public CountTask(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * 玩具代码，累加求和(返回类型由线程指定)
     * @return
     */
    @Override
    protected Long compute() {
        //计算得到的总和
        long sum = 0;
        //可以执行计算的标识
        boolean canCompute = (end - start )<THRESHOLD;

        if(canCompute){//如果当前任务的开始到结束小于分解规模
            for(long i=start ;i<=end;i++){
                sum+=i;
            }
        }else{
            //否则分解任务
            //把当前计算数目，分成100个小任务
            long step = (start+end)/100;//每一步包含的数目
            System.out.println("step:"+step);
            List<CountTask> subTasks = new ArrayList<>();
            //开始位置
            long pos = start;
            for(int i=0;i<100;i++){
                //小任务的最后一个位置
                long lastOne = pos+step;
                if(lastOne>end)
                    lastOne = end;
             //   System.out.println("pos:"+pos+",lastOne:"+lastOne);
                //每个任务的大小都是step个
                CountTask subTask = new CountTask(pos,lastOne);
                pos += step+1;
                //添加小任务
                subTasks.add(subTask);
                //fork任务（分治，创建子进程来处理这个task）
                subTask.fork();
            }
            //当前任务的子任务们，统统完成得到子任务的sum
            for(CountTask t:subTasks){
                //t.join（） 得到线程t执行结果
                sum += t.join();
            }
        }
        return sum;
    }
    public static void main(String[] args){
        //分治池（fork子线程太多会很影响性能，所以一般）
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //要计算的任务（1~200000 求和），大任务
        CountTask task = new CountTask(0,200000l);
        //分治任务提交，得到携带结果的任务
        ForkJoinTask<Long> result = forkJoinPool.submit(task);
        try{
            //得到处理返回
            long res = result.get();
            System.out.println("sum = "+res);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
