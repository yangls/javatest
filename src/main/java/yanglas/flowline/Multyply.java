package yanglas.flowline;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 流水线第二步
 */
public class Multyply implements Runnable {
    public static BlockingDeque<Msg> blockingDeque = new LinkedBlockingDeque<>();

    @Override
    public void run() {
        while(true){
            try {
                Msg msg = blockingDeque.take();
                msg.setJ(msg.getI()*msg.getJ());
                Div.blockingDeque.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
