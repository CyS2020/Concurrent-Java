package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：懒汉式(线程安全)--可用但不推荐
 */
public class Singleton4 {

    private static Singleton4 instance;

    private Singleton4() {

    }

    public synchronized static Singleton4 getInstance() {
        if (instance == null) {
            instance = new Singleton4();
        }
        return instance;
    }
}