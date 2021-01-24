package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：懒汉式(线程不安全)--不可用
 */
public class Singleton3 {
    private static Singleton3 instance;

    private Singleton3() {

    }

    public static Singleton3 getInstance() {
        if (instance == null) {
            instance = new Singleton3();
        }
        return instance;
    }
}