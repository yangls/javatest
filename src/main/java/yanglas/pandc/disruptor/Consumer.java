package yanglas.pandc.disruptor;

import com.lmax.disruptor.WorkHandler;

public class Consumer implements WorkHandler<Data> {
    /**
     * 消费的回调方法
     * @param event
     * @throws Exception
     */
    @Override
    public void onEvent(Data event) throws Exception {
        //具体数据的处理已经被disruptor处理
        //得到数据进行处理
        System.out.println("consumer:"+Thread.currentThread().getId()+" :Event: --"+event.getValue() +"  --");
    }
}
