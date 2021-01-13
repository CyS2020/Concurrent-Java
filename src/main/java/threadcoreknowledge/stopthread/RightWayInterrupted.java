package threadcoreknowledge.stopthread;

/**
 * @author: CyS2020
 * @date: 2021/1/13
 */
public class RightWayInterrupted {
    public static void main(String[] args) throws InterruptedException {

        Thread threadOne = new Thread(() -> {
            for (; ; ) {
            }
        });

        // 启动线程
        threadOne.start();
        //设置中断标志
        threadOne.interrupt();
        //获取中断标志
        System.out.println("isInterrupted: " + threadOne.isInterrupted());
        //获取中断标志并重置
        System.out.println("isInterrupted: " + threadOne.interrupted());
        //获取中断标志并重直
        System.out.println("isInterrupted: " + Thread.interrupted());
        //获取中断标志
        System.out.println("isInterrupted: " + threadOne.isInterrupted());
        threadOne.join();
        System.out.println("Main thread is over.");
    }
}
