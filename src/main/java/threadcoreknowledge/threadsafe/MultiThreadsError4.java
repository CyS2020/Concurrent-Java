package threadcoreknowledge.threadsafe;

import lombok.Data;

/**
 * @author: CyS2020
 * @date: 2021/1/20
 * 描述：初始化未完毕，就this赋值
 */
public class MultiThreadsError4 {

    public static Point point;

    public static void main(String[] args) throws InterruptedException {
        new PointMaker().start();
        Thread.sleep(105);
        if (point != null) {
            System.out.println(point);
        }
    }
}

@Data
class Point {

    private final int x, y;

    public Point(int x, int y) throws InterruptedException {
        this.x = x;
        MultiThreadsError4.point = this;
        Thread.sleep(100);
        this.y = y;
    }
}

class PointMaker extends Thread {

    @Override
    public void run() {
        try {
            new Point(1, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
