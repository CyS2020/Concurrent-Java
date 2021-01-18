package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/17
 * 描述：演示join期间被中断的效果
 */
public class JoinInterrupt {

    public static void main(String[] args) {
        Thread mainThread = Thread.currentThread();
        Thread thread1 = new Thread(() -> {
            try {
                mainThread.interrupt();
                Thread.sleep(5000);
                System.out.println("Thread1 finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.println("子线程中断");
            }
        });
        thread1.start();
        System.out.println("等待子线程执行完毕");
        try {
            thread1.join();
        } catch (InterruptedException e) {
            System.out.println(Thread.currentThread().getName() + "主线程被中断");
            e.printStackTrace();
            thread1.interrupt();
        }
        System.out.println("子线程已经运行完毕");
    }
}
