package threadcoreknowledge.startthread;

/**
 * @author: CyS2020
 * @date: 2021/1/11
 * 描述：演示不能两次调用start方法，否则会报错
 */
public class CantStartTwice {

    public static void main(String[] args) {
        Thread thread = new Thread();
        thread.start();
        thread.start();
    }
}
