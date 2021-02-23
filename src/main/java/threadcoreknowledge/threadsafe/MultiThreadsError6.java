package threadcoreknowledge.threadsafe;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: CyS2020
 * @date: 2021/1/20
 * 描述：构造函数中新建线程
 */
public class MultiThreadsError6 {
    private Map<String, String> states;

    public MultiThreadsError6() {
        new Thread(() -> {
            states = new HashMap<>();
            states.put("1", "周一");
            states.put("2", "周二");
            states.put("3", "周三");
            states.put("4", "周四");
        }).start();
    }

    public Map<String, String> getStates() {
        return states;
    }

    public static void main(String[] args) throws InterruptedException {
        MultiThreadsError6 error6 = new MultiThreadsError6();
        //Thread.sleep(1000); 会造成空指针异常
        System.out.println(error6.getStates().get("1"));
    }
}
