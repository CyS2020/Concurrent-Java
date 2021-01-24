package singleton;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：饿汉式(静态常量)--可用
 */
public class Singleton1 {

    private final static Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {

    }

    public static Singleton1 getInstance() {
        return INSTANCE;
    }
}