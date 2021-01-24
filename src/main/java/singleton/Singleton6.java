package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：懒汉式(双重检查)--推荐面试时使用
 * 有点：线程安全；延迟加载，效率较高
 */
public class Singleton6 {

    private volatile static Singleton6 instance;

    private Singleton6() {

    }

    public static Singleton6 getInstance() {
        if (instance == null) {
            synchronized (Singleton6.class) {
                if (instance == null) {
                    instance = new Singleton6();
                }
            }
        }
        return instance;
    }
}