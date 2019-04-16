package lhh.day06.nio;

import java.nio.ByteBuffer;
import java.util.LinkedList;

public class NioEchoClient {

    private LinkedList<ByteBuffer> outq;

    public LinkedList<ByteBuffer> getOutputQueue() {
        return outq;
    }

    public void enqueue(ByteBuffer bb) {
        outq.addFirst(bb);
    }

    public NioEchoClient() {
        outq = new LinkedList<ByteBuffer>();
    }
}
