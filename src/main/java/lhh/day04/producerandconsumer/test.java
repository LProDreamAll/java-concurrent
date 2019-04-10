package lhh.day04.producerandconsumer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 堵塞队列实现：
 * 1：保证消费有序，在构建实体类的时候添加唯一id进行消费即可
 * 2:性能上很好，但是并没有最优
 */
public class test {
    public static void main(String[] args) throws InterruptedException {
        //定义一个堵塞队列 作为共享区
        LinkedBlockingQueue<Money> queue = new LinkedBlockingQueue<>(10);
        MoneyProducer moneyProducer1 = new MoneyProducer(queue);
        MoneyProducer moneyProducer2 = new MoneyProducer(queue);
        MoneyProducer moneyProducer3 = new MoneyProducer(queue);
        MoneyConsumer moneyConsumer1 = new MoneyConsumer(queue);
        MoneyConsumer moneyConsumer2 = new MoneyConsumer(queue);
        MoneyConsumer moneyConsumer3 = new MoneyConsumer(queue);

        ExecutorService service = Executors.newCachedThreadPool();
        service.execute(moneyProducer1);
        service.execute(moneyProducer2);
        service.execute(moneyProducer3);
        service.execute(moneyConsumer1);
        service.execute(moneyConsumer2);
        service.execute(moneyConsumer3);
        Thread.sleep(10 * 1000);
        moneyProducer1.stop();
        moneyProducer2.stop();
        moneyProducer3.stop();

        Thread.sleep(3000);
        service.shutdown();
    }
}
