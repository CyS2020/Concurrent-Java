package threadcoreknowledge.createThreads;

/**
 * @author: CyS2020
 * @date: 2021/1/9
 * 描述：使用 Runnable方式创建线程
 */
public class RunnableStyle implements Runnable {

    @Override
    public void run() {
        System.out.println("用Runnable方法实现线程");
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new RunnableStyle());
        thread.start();
    }
}
