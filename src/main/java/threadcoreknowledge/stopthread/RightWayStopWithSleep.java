package threadcoreknowledge.stopthread;

/**
 * @author: CyS2020
 * @date: 2021/1/12
 * 描述：带有sleep的中断线程的写法
 */
public class RightWayStopWithSleep {

    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = () -> {
            int num = 0;
            try {
                while (num <= Integer.MAX_VALUE / 2 && !Thread.currentThread().isInterrupted()) {
                    if (num % 100 == 0) {
                        System.out.println(num + "是100的倍数");
                    }
                    num++;
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
        Thread.sleep(500);
        thread.interrupt();
    }
}
