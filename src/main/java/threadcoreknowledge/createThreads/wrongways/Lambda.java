package threadcoreknowledge.createThreads.wrongways;

/**
 * @author: CyS2020
 * @date: 2021/1/11
 * 描述：lambda表达式创建线程
 */
public class Lambda {

    public static void main(String[] args) {
        new Thread(() -> System.out.println(Thread.currentThread().getName())).start();
    }
}
