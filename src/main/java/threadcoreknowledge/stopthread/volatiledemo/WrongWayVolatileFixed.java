package threadcoreknowledge.stopthread.volatiledemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author: CyS2020
 * @date: 2021/1/13
 * 描述：用中断来修复刚才出现的无尽等待
 */
public class WrongWayVolatileFixed {

    public static void main(String[] args) throws InterruptedException {
        WrongWayVolatileFixed body = new WrongWayVolatileFixed();
        ArrayBlockingQueue<Integer> storage = new ArrayBlockingQueue<>(10);
        Producer producer = body.new Producer(storage);
        Thread producerThread = new Thread(producer);
        producerThread.start();
        Thread.sleep(1000);

        Consumer consumer = body.new Consumer(storage);
        while (consumer.needMoreNumbs()) {
            System.out.println(consumer.storage.take() + "被消费了");
            Thread.sleep(100);
        }
        System.out.println("消费者不需要更多数据了");

        producerThread.interrupt();
    }


    class Producer implements Runnable {

        public BlockingQueue<Integer> storage;

        public Producer(BlockingQueue<Integer> storage) {
            this.storage = storage;
        }

        @Override
        public void run() {
            int num = 0;
            try {
                while (num <= 100000 && !Thread.currentThread().isInterrupted()) {
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
}


