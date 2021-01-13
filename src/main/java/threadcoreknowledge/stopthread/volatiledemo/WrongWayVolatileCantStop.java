package threadcoreknowledge.stopthread.volatiledemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: CyS2020
 * @date: 2021/1/13
 * 描述：演示用volatile的局限part2 ：陷入阻塞时，volatile是无法停止线程的
 * 此例中，生产者的生产速度很快，消费者消费速度很慢，生产者会阻塞，等待消费者消费
 */
public class WrongWayVolatileCantStop {

    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueue<Integer> storage = new ArrayBlockingQueue<>(10);
        Producer producer = new Producer(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Thread.sleep(1000);

        Consumer consumer = new Consumer(storage);
        while (consumer.needMoreNumbs()) {
            System.out.println(consumer.storage.take() + "被消费了");
            Thread.sleep(100);
        }
        System.out.println("消费者不需要更多数据了");

        //一旦消费者不需要更多数据了，应该让生产者也停下来
        producer.canceled = true;
        System.out.println(producer.canceled);
    }
}

class Producer implements Runnable {

    public volatile boolean canceled;

    public BlockingQueue<Integer> storage;

    public Producer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        int num = 0;
        try {
            while (num <= 100000 && !canceled) {
                if (num % 100 == 0) {
                    storage.put(num);
                    System.out.println(num + "是100倍数，被放入仓库");
                }
                num++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("生产者结束运行");
        }
    }
}

class Consumer {
    public BlockingQueue<Integer> storage;

    public Consumer(BlockingQueue<Integer> storage) {
        this.storage = storage;
    }

    public boolean needMoreNumbs() {
        return !(Math.random() > 0.95);
    }
}
