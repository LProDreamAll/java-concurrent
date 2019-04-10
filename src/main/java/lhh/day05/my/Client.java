package lhh.day05.my;

public class Client {

    private Data request(final String queryStr) {
        final FutureData future = new FutureData();
        new Thread(() -> {
            RealData realData = new RealData(queryStr);
            future.setRealData(realData);
        }).start();//这个线程不启动永远拿不到future
        return future;
    }

    public static void main(String[] args) {

        Client client = new Client();
        //这里会立刻返回FutureData 但是并不会马上返回RealData
        Data myName = client.request("MyName");

        System.out.println("发送请求完毕");


        try {
            Thread.sleep(120);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("myName.getResult() = " + myName.getResult());
    }
}
