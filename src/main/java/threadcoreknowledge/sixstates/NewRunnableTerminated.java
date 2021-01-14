package threadcoreknowledge.sixstates;

/**
 * @author: CyS2020
 * @date: 2021/1/14
 * 描述：展示线程NEW、RUNNABLE、TERMINATED。
 * 即使是正在运行也是Runnable状态而不是RUNNING
 */
public class NewRunnableTerminated implements Runnable{

    public static void main(String[] args) {
        Thread thread = new Thread(new NewRunnableTerminated());
        System.out.println(thread.getState());
        thread.start();
        System.out.println(thread.getState());
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 打印出RUNNABLE状态，即使正在运行也是RUNNABLE，而不是RUNNING
        System.out.println(thread.getState());
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 打印出TERMINATED状态
        System.out.println(thread.getState());
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++){
            System.out.println(i);
        }
    }
}
