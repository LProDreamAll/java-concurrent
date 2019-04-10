package lhh.day04.disruptordemo;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * 生产者需要ringBuffer的引用。环形缓冲区，pushData把产生的数据推入缓冲区。
 */
public class Producer {

    private final RingBuffer<PCData> ringBuffer;

    public Producer(RingBuffer<PCData> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer bb) {
        long sequence = ringBuffer.next();//grab the next sequence-序列号
        try {
            PCData event = ringBuffer.get(sequence);//Get entity in the disruptor
            event.setValue(bb.getLong(0));//设置为期望值
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
