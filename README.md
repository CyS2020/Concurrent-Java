### 八大核心技术
### 一、实现多线程的正确姿势
#### 实现多线程的方法？
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
  - 方法一：最终调用target.run()；方法二：run()整个都被重写
```
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
 ```
#### 答题思路以下5点：
- 1. 从不同的角度看，会有不同的答案。
- 2. 典型答案是两种，分别是实现Runnable接口和继承Thread类，然后具体展开说；
- 3. 但是，我们看原理，其实Thread类实现了Runnable接口，并且看Thread类的run方法，会发现
其实那两种本质都是一样的，run方法的代码如下：
```
    @Override
    public void run() {
        if (target != null) {
            target.run();
        }
    }
 ```
- 4. 方法一和方法二，也就是“继承Thread类然后重写run()”和“实现Runnable接口并传入Thread类”在实
现多线程的本质上，并没有区别，都是最终调用了start()方法来新建线程。这两个方法的最主要区别
在于run()方法的内容来源：
方法一：最终调用target.run();
方法二：run()整个都被重写
- 5. 然后具体展开说其他方式；
还有其他的实现线程的方法，例如线程池等，它们也能新建线程，但是细看源码，从没有逃出过
本质，也就是实现Runnable接口和继承Thread类。
- 6. 结论:我们只能通过新建Thread类这一种方式来创建线程，但是类里面的run方法有两种方式来实
现，第一种是重写run方法，第二种实现Runnable接口的run方法，然后再把该runnable实例传
给Thread类。除此之外，从表面上看线程池、定时器等工具类也可以创建线程，但是它们的本
质都逃不出刚才所说的范围。
以上这种描述比直接回答一种、两种、多种都更准确。
### 二、启动线程的正确姿势
#### start方法的执行流程是什么？
- 1. 检查线程状态，只有NEW状态下的线程才能继续，否则会抛出IllegalThreadStateException（在
运行中或者已结束的线程，都不能再次启动，详见CantStartTwice类）
- 2. 被加入线程组
- 3. 调用start0()方法启动线程
- 注意点：start方法是被synchronized修饰的方法，可以保证线程安全；由JVM创建的main方法线程和
system组线程，并不会通过start来启动。
### 三、停止线程的正确姿势
#### 如何正确的停止线程？
- 使用interrupt来通知，而不是强制
- 1. 在Java中，最好的停止线程的方式是使用中断interrupt，但是这仅仅是会通知到被终止的线程“你该
停止运行了”，被终止的线程自身拥有决定权（决定是否、以及何时停止），这依赖于请求停止方和被停止
方都遵守一种约定好的编码规范。
- 2. 任务和线程的启动很容易。在大多数时候,我们都会让它们运行直到结束,或者让它们自行停止。然而,
有时候我们希望提前结束任务或线程,或许是因为用户取消了操作,或者服务需要被快速关闭，或者是运行
超时或出错了。
- 3. 要使任务和线程能安全、快速、可靠地停止下来,并不是一件容易的事。Java没有提供任何机制来安全
地终止线程。但它提供了中断( Interruption),这是一种协作机制,能够使一个线程终止另一个线程的当前
工作。
- 4. 这种协作式的方法是必要的,我们很少希望某个任务、线程或服务立即停止,因为这种立即停止会使共
享的数据结构处于不一致的状态。相反,在编写任务和服务时可以使用一种协作的方式:当需要停止时,它们
首先会清除当前正在执行的工作,然后再结束。这提供了更好的灵活性,因为任务本身的代码比发出取消请求
的代码更清楚如何执行清除工作。
- 5. 生命周期结束(End-of-Lifecycle)的问题会使任务、服务以及程序的设计和实现等过程变得复杂,而这
个在程序设计中非常重要的要素却经常被忽略。一个在行为良好的软件与勉强运的软件之间的最主要区别就是,
行为良好的软件能很完善地处理失败、关闭和取消等过程。
- 6. 想要停止线程需要：请求方、被停止方、子方法调用方互相配合
#### 线程什么时候会停止？
- 1. run方法所有代码执行完毕，最常见的停止方法。
- 2. 有异常出现并且方法中没有捕获，线程会停止。
#### 处理中断的最好方法是什么？
- 优先选择在方法上抛出异常。
- 用throws InterruptedException 标记你的方法，不采用try 语句块捕获异常，以便于该异常可以传递
到顶层，让run方法可以捕获这一异常，例如：
```
    void subTask() throws InterruptException{
        sleep(delay);
    }
```
- 由于run方法内无法抛出checked Exception（只能用try catch），顶层方法必须处理该异常，避免了
漏掉或者被吞掉的情况，增强了代码的健壮性。
- while内try/catch问题，当它一旦响应中断后便会吧Interrupt的标志位清除，因此程序会继续运行。
#### 如果不能抛出中断，要怎么做？
- 如果不想或无法传递InterruptedException（用run方法的时候就不让该方法throws InterruptedException），
那么应该选择在catch 子句中调用Thread.currentThread().interrupt() 来恢复设置中断状态，以便于在后续的
执行依然能够检查到刚才发生了中断。
#### 响应中断方法总结列表
- Object.wait()/wait(long)/wait(long, int)
- Thread.sleep(long)/sleep(long, int)
- Thread.join()/join(long)/join(long, int)
- BlockingQueue.take()/put(E)
- Lock.lockInterruptibly()
- CountDownLatch.await()
- CyclicBarrier.await()
- Exchanger.exchange(V)
- java.nio.channels.InterruptibleChannel相关方法
- java.nio.channels.Selector相关方法
#### 停止线程错误的方法
- 被启用的stop, suspend和resume方法
- 用volatile设置boolean标志位
#### 为什么用volatile停止线程不够全面
- 解答：这种做法是错误的，或者说是不够全面的，在某些情况下虽然可用，但是某些情况下有严重问题。
- 这种方法在《Java并发编程实战》中被明确指出了缺陷，我们一起来看看缺陷在哪里：
此方法错误的原因在于，如果我们遇到了线程长时间阻塞（这是一种很常见的情况，例如生产者消
费者模式中就存在这样的情况），就没办法及时唤醒它，或者永远都无法唤醒该线程，而interrupt设
计之初就是把wait等长期阻塞作为一种特殊情况考虑在内了，我们应该用interrupt思维来停止线程。
#### Thread相关重要函数解析
- boolean isInterrupted(): 返回中断状态，不清除中断状态，某个线程实例的状态
- static booelan interrupted(): 返回中断状态，并清除中断状态，执行方法该的线程的状态
#### 如何处理不可中断阻塞
- 如果线程阻塞是由于调用了wait()，sleep()或join()方法，你可以中断线程，通过抛出InterruptedException
异常来唤醒该线程。但是对于不能响应InterruptedException的阻塞，很遗憾，并没有一个通用的解决方案。但是
我们可以利用特定的其它的可以响应中断的方法，比如ReentrantLock.lockInterruptibly()，比如关闭套接字使
线程立即返回等方法来达到目的。答案有很多种，因为有很多原因会造成线程阻塞，所以针对不同情况，唤起的方法
也不同。
- 总结就是说如果不支持响应中断，就要用特定方法来唤起，没有万能药。
### 四、线程的生命周期
#### 六种线程状态
- `New`: 已将创建但还没有启动，使用new Thread之后还没有调用start方法
- `Runnable`: 一旦调用start方法线程便会进入Runnable状态，可运行的对应操作系统的Ready和Running状态
- `Blocked`: 当线程进入到synchronized修饰的代码的时候并且该锁已经被其他线程拿走了线程处于该状态
- `Waiting`: 当线程执行到Object.wait()、Thread.join()、LockSupport.park()方法的时候会进入到该状态
- `Timed Waiting`: Object.wait(time)、Thread.sleep(time)、Thread.join(time)、LockSupport.parkNanos(time)、LockSupport.partUntil(time)
- `Terminated`: run方法正常执行完毕、出现了没有捕获的异常意外终止
![线程的六种状态](https://raw.githubusercontent.com/CyS2020/Concurrent-Java/master/picture/%E7%BA%BF%E7%A8%8B%E7%9A%846%E4%B8%AA%E7%8A%B6%E6%80%81.png)
#### 状态转换的特殊情况
- 从Object.wait()状态刚被唤醒时，通常不能立刻抢到monitor锁就会从Waiting先进入Blocked状态，等抢到锁再转换到Runnable状态
- 如果发生异常，可以直接跳到终止Terminated状态，不必再遵循路径，比如可以直接从Waiting直接到Terminated
#### 阻塞状态
- 一般习惯而言，把Blocked(被阻塞)、Waiting(等待)、Timed_waiting(计时等待)都成为阻塞状态
### 五、Thread和Object类重要方法
#### wait、notify、notifyAll
- 执行这些方法必须先拥有monitor，也就是synchronized锁，否则是会抛异常的
- notify只会获取一个，取决于JVM实现，无法提前预知哪个会被唤醒
- 都属于Object类，都是final native的具体实现是JVM层，也不可以被重写
- 类似功能的Condition
- 同时持有多个锁，只会释放wait所对应的对象的那把锁
#### sleep
- 作用：我只想让线程在预期的时间执行，其他时候不需要占用CPU资源
- 不释放锁，包括synchronized和lock
