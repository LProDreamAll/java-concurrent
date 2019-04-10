package lhh.day04.disruptordemo;

import com.lmax.disruptor.WorkHandler;

public class Consumer implements WorkHandler<PCData> {

    @Override
    public void onEvent(PCData event) throws Exception {
        System.out.println(Thread.currentThread().getId() + ":Event: --" + event.getValue() * event.getValue() + "--");
    }
}
