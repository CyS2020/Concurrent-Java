package threadcoreknowledge.createThreads.wrongways;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author: CyS2020
 * @date: 2021/1/11
 * 描述：定时器创建线程
 */
public class DemoTimmerTask {

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        }, 1000, 1000);
    }
}
