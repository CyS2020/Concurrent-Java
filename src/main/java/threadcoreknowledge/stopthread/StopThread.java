package threadcoreknowledge.stopthread;

/**
 * @author: CyS2020
 * @date: 2021/1/13
 * 描述：错误停止方法：stop()来停止线程，会导致程序运行一半突然停止
 * 没有办法完成一个基本单位的操作(一个连队)，会造成脏数据(有的连队多
 * 领取装备少领取装备)
 */
public class StopThread implements Runnable{

    @Override
    public void run() {
        // 模拟指挥军队：一共有5个连队，每个连队10人以连队为单位
        // 叫到号的士兵前去领取
        for (int i = 0; i < 5; i++){
            System.out.println("连队" + i + "开始领取武器");
            for (int j = 0; j < 10; j++){
                System.out.println(j);
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("连队" + i + "已经领取完毕");
        }
    }

    public static void main(String[] args) {
        Thread thread = new Thread(new StopThread());
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.stop();
    }
}