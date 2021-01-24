package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：懒汉式(线程不安全)--不可用
 */
public class Singleton5 {

    private static Singleton5 instance;

    private Singleton5() {

    }

    public static Singleton5 getInstance() {
        if (instance == null) {
            synchronized (Singleton5.class) {
                instance = new Singleton5();
            }
        }
        return instance;
    }
}