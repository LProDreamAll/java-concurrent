package lhh.day06.nio;

import sun.nio.ch.SelectionKeyImpl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.*;
import java.nio.channels.spi.AbstractSelector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NioEchoServer {
    private Selector selector;
    private ExecutorService tp = Executors.newCachedThreadPool();
    private static Map<Socket, Long> time_stat = new ConcurrentHashMap<>(10240);

    public void startServer() throws Exception {
        selector = SelectorProvider.provider().openSelector();
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);//设置非堵塞模式
        InetSocketAddress isa = new InetSocketAddress(8000);
        ssc.bind(isa);
        //ServerSocketChannel 绑定到 Selector上 acceptKey表示一对 Selector和Channel的关系
        SelectionKey acceptKey = ssc.register(selector, SelectionKey.OP_ACCEPT);
        for (; ; ) {
            selector.select();
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = readyKeys.iterator();
            long e = 0;
            while (i.hasNext()) {
                SelectionKey sk = i.next();
                //处理一对就移除一对
                i.remove();

                if (sk.isAcceptable()) {
                    doAccept(sk);//进行客户端的接收
                } else if (sk.isValid() && sk.isReadable()) {
                    if (!time_stat.containsKey(((SocketChannel) sk.channel()).socket())) {
                        time_stat.put(((SocketChannel) sk.channel()).socket(), System.currentTimeMillis());
                        doRead(sk);//进行读取
                    }
                } else if (sk.isValid() && sk.isWritable()) {
                    doWrite(sk);
                    e = System.currentTimeMillis();
                    Long b = time_stat.remove(((SocketChannel) sk.channel()).socket());
                    System.out.println("spend: " + (e - b) + " ms");

                }


            }
        }
    }

    /**
     * @param sk
     */
    private void doWrite(SelectionKey sk) {
    }

    /**
     * 当channel可读的时候，该方法会被调用
     *
     * @param sk
     */
    private void doRead(SelectionKey sk) {

    }

    /**
     * 进行客户端的接收
     *
     * @param sk
     */
    private void doAccept(SelectionKey sk) {
        ServerSocketChannel server = (ServerSocketChannel) sk.channel();
        SocketChannel clientChannel;
        try {
            clientChannel = server.accept();
            clientChannel.configureBlocking(false);

            //register this channel for reading
            SelectionKey clientKey = clientChannel.register(selector, SelectionKey.OP_READ);
            NioEchoClient echoClient = new NioEchoClient();
            clientKey.attach(echoClient);

            InetAddress clientAddress = clientChannel.socket().getInetAddress();
            System.out.println("Accept connection form " + clientAddress.getHostAddress() + ".");
        } catch (IOException e) {
            System.out.println("failed ! ");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Map<Socket, Long> time_stat = new ConcurrentHashMap<>(10240);

    }
}
