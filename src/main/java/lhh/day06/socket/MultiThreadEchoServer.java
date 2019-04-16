package lhh.day06.socket;

import com.sun.corba.se.spi.activation.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程的服务器
 * 伪异步的实现 因为需要等待上一个线程完成才会有下一个线程连接后的结果
 */
public class MultiThreadEchoServer {

    private static ExecutorService tp = Executors.newCachedThreadPool();

    public static class HandleMsg implements Runnable {

        Socket clientSocket;

        public HandleMsg(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            BufferedReader is = null;
            PrintWriter os = null;

            try {
                is = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                os = new PrintWriter(clientSocket.getOutputStream(), true);
                String inputLine = null;
                long l = System.currentTimeMillis();
                while ((inputLine = is.readLine()) != null) {
                    os.println(inputLine);
                }
                long l1 = System.currentTimeMillis();
                System.out.println("spend:" + (l1 - l) + "ms");

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (os != null) {
                        os.close();
                    }
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
        } catch (IOException e) {
            System.out.println(e.toString());
        }

        while (true) {
            try {
                assert serverSocket != null;
                clientSocket = serverSocket.accept();
                System.out.println(clientSocket.getRemoteSocketAddress() + " connect!");
                tp.execute(new HandleMsg(clientSocket));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
