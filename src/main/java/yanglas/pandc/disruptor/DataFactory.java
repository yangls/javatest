package yanglas.pandc.disruptor;

import com.lmax.disruptor.EventFactory;


public class DataFactory implements EventFactory<Data> {
    @Override
    public Data newInstance() {
        return new Data();
    }
}
