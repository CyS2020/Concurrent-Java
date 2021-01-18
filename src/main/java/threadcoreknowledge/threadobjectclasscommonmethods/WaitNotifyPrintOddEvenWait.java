package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/15
 * 描述：两个线程交替打印0~100的奇偶数，用wait和notify
 */
public class WaitNotifyPrintOddEvenWait {

    private static int count = 0;

    private static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(new TurningRunner(), "偶数").start();
        new Thread(new TurningRunner(), "奇数").start();
    }

    // 1. 拿到锁，我们就打印
    // 2. 打印完，唤醒其他线程自己就休眠
    static class TurningRunner implements Runnable{

        @Override
        public void run() {
            while (count < 100){
                synchronized (lock){
                    System.out.println(Thread.currentThread().getName() + ":" + count++);
                    lock.notify();
                    if (count < 100){
                        try {
                            // 若任务还没结束让出锁
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }
}
