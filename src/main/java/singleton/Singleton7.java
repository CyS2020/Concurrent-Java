package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：懒汉式(静态内部类方式)--可用
 * 静态内部类与非静态内部类一样不会因为外部类加载而加载
 * 静态内部类的初始化和外部类初始化没关系，用到的时候才会初始化
 */
public class Singleton7 {

    private Singleton7() {

    }

    private static class SingletonInstance {

        private static final Singleton7 INSTANCE = new Singleton7();
    }

    public static Singleton7 getInstance() {
        return SingletonInstance.INSTANCE;
    }
}