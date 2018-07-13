package yanglas.flowline;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Plus implements Runnable {

    public static BlockingDeque<Msg> blockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while (true){
            try {
                Msg msg = blockingDeque.take();
                msg.setJ(msg.getI()+msg.getJ());
                //放入第二步的流水队列之中
                Multyply.blockingDeque.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
