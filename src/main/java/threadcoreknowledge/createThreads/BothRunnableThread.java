package threadcoreknowledge.createThreads;

/**
 * @author: CyS2020
 * @date: 2021/1/10
 * 描述：同时使用Runnable和Thread两种线程
 */
public class BothRunnableThread {

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("我来自Runnable");
            }
        }) {
            @Override
            public void run() {
                System.out.println("我来自Thread");
            }
        }.start();
    }
}
