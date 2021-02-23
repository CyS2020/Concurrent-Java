package threadcoreknowledge.threadsafe;


/**
 * @author: CyS2020
 * @date: 2021/1/20
 * 描述：观察者模式
 * 构造函数中给传入的资源注册监听器
 * 构造函数尚未初始化结束，监听器已经开始执行，正常情况下监听到的数字是100
 */
public class MultiThreadsError5 {

    int count;

    public MultiThreadsError5(MySource source) {
        source.registerListener(e -> System.out.println("我得到的数字是" + count));
        for (int i = 0; i < 10000; i++) {
            System.out.print(i);
        }
        count = 100;
    }

    public static void main(String[] args) {
        MySource mySource = new MySource();
        new Thread(() -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            mySource.eventCome(new Event() {
            });
        }).start();
        MultiThreadsError5 error5 = new MultiThreadsError5(mySource);
    }

    static class MySource {

        private EventListener listener;

        void registerListener(EventListener eventListener) {
            this.listener = eventListener;
        }

        void eventCome(Event e) {
            if (listener != null) {
                listener.onEvent(e);
            } else {
                System.out.println("还未初始化完毕");
            }
        }
    }

    interface EventListener {
        void onEvent(Event e);
    }

    interface Event {

    }
}
