package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/15
 * 描述：两个线程交替打印0~100的奇偶数，用synchronized关键字实现
 */
public class WaitNotifyPrintOddEvenSyn {

    private static int count;

    private static final Object lock = new Object();

    // 新建两个线程，第一个只处理偶数，第二个只处理奇数(位运算)
    // 用synchronized来通信
    public static void main(String[] args) {
        new Thread(() -> {
            while(count < 100){
                synchronized (lock){
                    if ((count & 1) == 0){
                        System.out.println(Thread.currentThread().getName() + ":" + count++);
                    }
                }
            }
        },
                "偶数").start();

        new Thread(() -> {
            while(count < 100){
                synchronized (lock){
                    if ((count & 1) == 1){
                        System.out.println(Thread.currentThread().getName() + ":" + count++);
                    }
                }
            }
        }, "奇数").start();
    }
}
