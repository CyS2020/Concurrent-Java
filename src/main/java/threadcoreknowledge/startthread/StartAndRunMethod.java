package threadcoreknowledge.startthread;

/**
 * @author: CyS2020
 * @date: 2021/1/11
 * 描述：对比start和run两种启动线程的方式
 */
public class StartAndRunMethod {

    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println(Thread.currentThread().getName());
        runnable.run();

        new Thread(runnable).start();
    }
}
