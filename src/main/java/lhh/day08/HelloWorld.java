package lhh.day08;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * 其粒度比线程小很多，这意味着你可以在项目中使用大量的Actor
 */
public class HelloWorld extends UntypedActor {

    ActorRef greeter;

    /**
     * Akka的回调方法
     */
    public void preStart() {
        greeter = getContext().actorOf(Props.create(Greeter.class), "greeter");
        System.out.println("Greeter Actor path :" + greeter.path());
        greeter.tell(Greeter.Msg.GREET, getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message == Greeter.Msg.DONE) {
            greeter.tell(Greeter.Msg.GREET, getSelf());
            getContext().stop(getSelf());
        } else unhandled(message);
    }
}
