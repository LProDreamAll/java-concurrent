package lhh.day04.disruptordemo;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * disruptor 实现的是无锁的内存队列
 */
public class test {
    public static void main(String[] args) throws InterruptedException {

        ExecutorService executor = Executors.newCachedThreadPool();
        PCDataFactory factory = new PCDataFactory();
        int bufferSize = 1024;//must 2 to the whole power
        Disruptor<PCData> disruptor = new Disruptor<>(
                factory,
                bufferSize,
                executor,
                ProducerType.MULTI,
                new BlockingWaitStrategy());
        //BlockingWaitStrategy 封装了Disruptor中消费者如何知道新产生的数据的策略

        disruptor.handleEventsWithWorkerPool(
                new Consumer(),
                new Consumer(),
                new Consumer(),
                new Consumer());
        disruptor.start();
        RingBuffer<PCData> ringBuffer = disruptor.getRingBuffer();
        Producer producer = new Producer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i = 0; true; i++) {
            bb.putLong(0, i);
            producer.pushData(bb);
            Thread.sleep(100);
            System.out.println("add data" + i);
        }
    }

}
