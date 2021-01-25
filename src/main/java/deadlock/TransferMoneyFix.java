package deadlock;

import deadlock.TransferMoney.Account;

/**
 * @author: CyS2020
 * @date: 2021/1/24
 * 描述：转账时候遇到死锁，一旦打开注释便会发生死锁
 */
public class TransferMoneyFix implements Runnable {

    private int flag = 1;

    private static final Account a = new Account(500);

    private static final Account b = new Account(500);

    private static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        TransferMoneyFix r1 = new TransferMoneyFix();
        TransferMoneyFix r2 = new TransferMoneyFix();
        r1.flag = 1;
        r2.flag = 0;
        Thread thread1 = new Thread(r1);
        Thread thread2 = new Thread(r2);
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();
        System.out.println("a的余额：" + a.balance);
        System.out.println("b的余额：" + b.balance);

    }

    @Override
    public void run() {
        if (flag == 1) {
            transferMoney(a, b, 200);
        }
        if (flag == 0) {
            transferMoney(b, a, 200);
        }
    }

    public static void transferMoney(Account from, Account to, int amount) {

        class Helper {
            public void transfer() {
                if (from.balance - amount < 0) {
                    System.out.println("余额不足，转账失败");
                    return;
                }
                from.balance -= amount;
                to.balance += amount;
                System.out.println("成功转账" + amount + "元");
            }
        }

        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);
        if (fromHash < toHash) {
            synchronized (from) {
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        } else if (fromHash > toHash) {
            synchronized (to) {
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        } else {
            synchronized (lock) {
                synchronized (from) {
                    synchronized (to) {
                        new Helper().transfer();
                    }
                }
            }
        }
    }
}
