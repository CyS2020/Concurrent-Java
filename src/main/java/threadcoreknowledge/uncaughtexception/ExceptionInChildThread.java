package threadcoreknowledge.uncaughtexception;

/**
 * @author: CyS2020
 * @date: 2021/1/18
 * 描述：单线程，抛出，处理，有异常堆栈
 */
public class ExceptionInChildThread implements Runnable {

    public static void main(String[] args) {
        new Thread(new ExceptionInChildThread()).start();
        for (int i = 0; i < 1000; i++) {
            System.out.println(i);
        }
    }

    @Override
    public void run() {
        throw new RuntimeException();
    }
}
