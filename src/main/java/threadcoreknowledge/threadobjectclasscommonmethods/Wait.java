package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * @author: CyS2020
 * @date: 2021/1/15
 * 描述：展示wait和notify执行顺序
 * 1. 研究代码执行顺序
 * 2. 证明wait释放锁
 */
public class Wait {

    public static final Object object = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread1();
        Thread thread2 = new Thread2();
        thread1.start();
        Thread.sleep(200);
        thread2.start();
    }

    static class Thread1 extends Thread{

        @Override
        public void run(){
            synchronized (object){
                System.out.println("线程" + Thread.currentThread().getName() + "开始执行了");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁");
            }
        }
    }

    static class Thread2 extends Thread{

        @Override
        public void run(){
            synchronized (object){
                object.notify();
                System.out.println("线程" + Thread.currentThread().getName() + "调用了notify()");
            }
        }
    }
}
