### 八大核心技术
### 一、实现多线程的正确姿势
#### 实现多线程的方法
- 方法一：实现Runnable接口；方法二：继承Thread类；  通常我们优先选择方法一。
- 实现Runnable接口好在哪里？继承Thread类是不推荐的，因为它有以下的一些缺点：
  - 1. 从代码架构角度：具体的任务（run方法）应该和“创建和运行线程的机制（Thread类）”解耦，用runnable对象可以实现解耦。
  - 2. 使用继承Thread的方式的话，那么每次想新建一个任务，只能新建一个独立的线程，而这样做的损耗会比较大
  （比如重头开始创建一个线程、执行完毕以后再销毁等。如果线程的实际工作内容，也就是run()函数里只是简单的打印一行文字的话，那么可能线程的实际工作内容还不如损耗来的大）。
  如果使用Runnable和线程池，就可以大大减小这样的损耗。
  - 3. 继承Thread类以后，由于Java语言不支持双继承，这样就无法再继承其他的类，限制了可扩展性。
- 两种方法的本质对比
  - 方法一和方法二，也就是“实现Runnable接口并传入Thread类”和“继承Thread类然后重写run()”在实现多线程的本质上，并没有区别，
  都是最终调用了start()方法来新建线程。这两个方法的最主要区别在于run()方法的内容来源：
```
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
 ```
  - 方法一：最终调用target.run()；方法二：run()整个都被重写
