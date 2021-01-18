package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/15
 * 描述：证明wait只释放当前的那把锁
 */
public class WaitNotifyReleaseOwnMonitor {

    public static final Object resourceA = new Object();

    public static final Object resourceB = new Object();

    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("ThreadA got resourceA lock.");
                synchronized (resourceB) {
                    System.out.println("ThreadA got resourceB lock.");
                    try {
                        System.out.println("ThreadA release resourceA lock.");
                        resourceA.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Thread thread2 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (resourceA) {
                System.out.println("ThreadB got resourceA lock.");
                System.out.println("ThreadB tries to resourceB lock.");
                synchronized (resourceB) {
                    System.out.println("ThreadB got resourceB lock.");
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
