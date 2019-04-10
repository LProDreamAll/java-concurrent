package lhh.day04.disruptordemo;

import com.lmax.disruptor.EventFactory;

public class PCDataFactory implements EventFactory<PCData> {

    @Override
    public PCData newInstance() {
        return new PCData();
    }
}
