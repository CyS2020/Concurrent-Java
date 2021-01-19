package threadcoreknowledge.threadsafe;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CyS2020
 * @date: 2021/1/19
 * 描述：第一种：运行结果出错
 * 演示技术不准确(减少)， 找出具体出错位置
 */
public class MultiThreadsError implements Runnable {

    private int index = 0;

    private static final AtomicInteger realIndex = new AtomicInteger();

    private static final AtomicInteger wrongCount = new AtomicInteger();

    private CyclicBarrier cyclicBarrier1 = new CyclicBarrier(2);

    private CyclicBarrier cyclicBarrier2 = new CyclicBarrier(2);

    private final boolean[] marked = new boolean[20001];

    private static final MultiThreadsError instance = new MultiThreadsError();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread(instance);
        Thread thread2 = new Thread(instance);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("表面上结果是" + instance.index);
        System.out.println("真正运行的次数" + realIndex.get());
        System.out.println("错误次数" + wrongCount.get());
    }

    @Override
    public void run() {
        marked[0] = true;
        for (int i = 0; i < 10000; i++) {
            try {
                cyclicBarrier2.reset();
                cyclicBarrier1.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            index++;
            try {
                cyclicBarrier1.reset();
                cyclicBarrier2.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
            realIndex.incrementAndGet();
            synchronized (instance) {
                if (marked[index] && marked[index - 1]) {
                    System.out.println("发生错误" + i);
                    wrongCount.incrementAndGet();
                }
                marked[index] = true;
            }
        }
    }
}
