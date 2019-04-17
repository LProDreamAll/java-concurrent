package lhh.day08;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

/**
 * 现在使用Akka构建高并发程序
 * spark早期使用的就是它(scala编写-一种jvm语言)，现在使用netty4，进行并发的实现
 */
//todo 还有nio ，netty 源码没有好好分析
public class test {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("hello", ConfigFactory.load("sampleHello.conf"));
        ActorRef helloWorld_ = system.actorOf(Props.create(HelloWorld.class), "helloWorld");//这里不能有空格
        System.out.println("HelloWorld Actor Path:" + helloWorld_.path());
    }
}
