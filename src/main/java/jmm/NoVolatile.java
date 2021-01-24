package jmm;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CyS2020
 * @date: 2021/1/22
 * 描述：不适用于volatile
 */
public class NoVolatile implements Runnable {

    private volatile int a;

    private AtomicInteger realA = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        NoVolatile r = new NoVolatile();
        Thread thread1 = new Thread(r);
        Thread thread2 = new Thread(r);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println(r.a);
        System.out.println(r.realA.get());
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            a++;
            realA.incrementAndGet();
        }
    }
}
