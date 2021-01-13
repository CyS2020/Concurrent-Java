package threadcoreknowledge.stopthread;

/**
 * @author: CyS2020
 * @date: 2021/1/12
 * 描述：run方法无法抛出checked Exception, 只能用try/catch
 */
public class RunThrowException {

    public void aVoid() throws Exception {
        throw new Exception();
    }

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
