package yanglas.flowline;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class Div implements Runnable {
    public static BlockingDeque<Msg> blockingDeque = new LinkedBlockingDeque<>();
    @Override
    public void run() {
        while(true){
            try {
                Msg msg = blockingDeque.take();
                msg.setJ(msg.getJ()/2);
                msg.setDesc(msg.getDesc()+" = "+msg.getJ());
                System.out.println(msg.getDesc());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
