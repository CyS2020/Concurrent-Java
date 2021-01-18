package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/15
 * 描述：3个线程，线程1和线程2首先被阻塞，线程3唤醒他们，notify、notifyAll
 * start先执行不代表线程先启动
 */
public class WaitNotifyAll implements Runnable {

    public static final Object resourceA = new Object();

    @Override
    public void run() {
        synchronized (resourceA){
            System.out.println(Thread.currentThread().getName() + "get resourceA lock.");
            try {
                System.out.println(Thread.currentThread().getName() + "waits to start");
                resourceA.wait();
                System.out.println(Thread.currentThread().getName() + " 's waiting to end");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Runnable r = new WaitNotifyAll();
        Thread threadA = new Thread(r);
        Thread threadB = new Thread(r);
        Thread threadC = new Thread(() -> {
            synchronized (resourceA){
                //resourceA.notify();
                resourceA.notifyAll();
                System.out.println("ThreadC notified");
            }
        });
        threadA.start();
        threadB.start();
        Thread.sleep(200);
        threadC.start();
    }
}
