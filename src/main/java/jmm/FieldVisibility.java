package jmm;

/**
 * @author: CyS2020
 * @date: 2021/1/22
 * 描述：演示可见性带来的问题
 */
public class FieldVisibility {

    private volatile int a = 1;

    private volatile int b = 2;

    public static void main(String[] args) {
        while (true) {
            FieldVisibility test = new FieldVisibility();
            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.change();
            }).start();
            new Thread(() -> {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                test.print();
            }).start();
        }

    }

    private void print() {
        System.out.println("b=" + b + ";a=" + a);
    }

    private void change() {
        a = 3;
        b = a;
    }
}
