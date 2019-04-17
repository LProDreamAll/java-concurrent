package lhh.day08;

import akka.actor.UntypedActor;

/**
 * actor就是cpu上的时间片，这种说法非常贴切。Actor之间通过消息进行通讯，一切都是异步的。
 * 可以说Actor就像现实生活中的一群人，他们各司其职，互相通过消息进行交流，
 * 一个actor收到另外一个actor发来的消息后会按照消息的内容去执行指定的任务，接着再将新任务传递下去或者将执行结果返回给消息发送方。
 * Actor这种模型很好地解决了传统java并发带来的各种问题
 */
public class Greeter extends UntypedActor {

    public static enum Msg {
        GREET, DONE;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Msg.GREET) {
            System.out.println("hello world !");
            getSender().tell(Msg.DONE, getSelf());
        } else unhandled(message);

    }
}
