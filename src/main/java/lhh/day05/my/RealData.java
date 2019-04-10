package lhh.day05.my;

public class RealData implements Data {

    protected final String result;

    public RealData(String P) {
        //假如很慢
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            sb.append(P);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        result = sb.toString();
    }


    @Override
    public String getResult() {
        return result;
    }
}
