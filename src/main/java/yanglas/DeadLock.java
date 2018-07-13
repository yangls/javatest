package yanglas;

/**
 *死锁测试
 */
public class DeadLock extends Thread{
    protected  Object tool;//操作的叉子（锁）
    static Object forkLeft = new Object();//左边的叉子
    static Object forkRight = new Object();//右边的叉子


    public DeadLock( Object tool) {

        this.tool = tool;
        if(tool == forkLeft)
            this.setName("哲学家A");

        if(tool == forkRight)
            this.setName("哲学家B");
    }

    @Override
    public void run() {
        if(tool == forkRight){
            synchronized (forkRight){
                try{
                    Thread.sleep(500);
                }catch(Exception e){
                    e.printStackTrace();
                }
                synchronized (forkLeft){
                    System.out.println("哲学家A吃饭了！");
                }
            }
        }

        if(tool == forkLeft){
            synchronized (forkLeft){
                try{
                    Thread.sleep(500);
                }catch(Exception e){
                    e.printStackTrace();
                }
                synchronized (forkRight){
                    System.out.println("哲学家B吃饭了！");
                }
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        DeadLock a = new DeadLock(forkRight);
        DeadLock b = new DeadLock(forkLeft);
        a.start();
        b.start();
        Thread.sleep(1000);
    }
}
