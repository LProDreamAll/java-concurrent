package lhh.day05;

public class FutureData implements Data {

    protected RealData realData = null;

    protected boolean isReady = false;

    public synchronized void setRealData(RealData realData) {
        if (isReady) {
            return;
        }
        this.realData = realData;
        isReady = true;
        notifyAll(); //realData 已经被注入 通知getResult
    }

    public synchronized String getResult() {
        while (!isReady) {
            try {
                wait();//一直等到RealData构造成功
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return realData.result;//由RealData实现
    }


}
