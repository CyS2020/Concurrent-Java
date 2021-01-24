### 第一部分-八大核心技术
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
- 2. 任务和线程的启动很容易。在大多数时候，我们都会让它们运行直到结束，或者让它们自行停止。然而，
有时候我们希望提前结束任务或线程，或许是因为用户取消了操作，或者服务需要被快速关闭，或者是运行
超时或出错了。
- 3. 要使任务和线程能安全、快速、可靠地停止下来，并不是一件容易的事。Java没有提供任何机制来安全
地终止线程。但它提供了中断( Interruption)，这是一种协作机制，能够使一个线程终止另一个线程的当前
工作。
- 4. 这种协作式的方法是必要的，我们很少希望某个任务、线程或服务立即停止，因为这种立即停止会使共
享的数据结构处于不一致的状态。相反，在编写任务和服务时可以使用一种协作的方式:当需要停止时，它们
首先会清除当前正在执行的工作，然后再结束。这提供了更好的灵活性，因为任务本身的代码比发出取消请求
的代码更清楚如何执行清除工作。
- 5. 生命周期结束(End-of-Lifecycle)的问题会使任务、服务以及程序的设计和实现等过程变得复杂，而这
个在程序设计中非常重要的要素却经常被忽略。一个在行为良好的软件与勉强运的软件之间的最主要区别就是，
行为良好的软件能很完善地处理失败、关闭和取消等过程。
- 6. 想要停止线程需要：请求方、被停止方、子方法调用方互相配合
#### 线程什么时候会停止？
- 1. run方法所有代码执行完毕，最常见的停止方法。
- 2. 有异常出现并且方法中没有捕获，线程会停止。
#### 子任务中断的最好方法是什么？
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
- 被启用的stop， suspend和resume方法
- 用volatile设置boolean标志位
#### 为什么用volatile停止线程不够全面
- 解答：这种做法是错误的，或者说是不够全面的，在某些情况下虽然可用，但是某些情况下有严重问题。
- 这种方法在《Java并发编程实战》中被明确指出了缺陷，我们一起来看看缺陷在哪里：
此方法错误的原因在于，如果我们遇到了线程长时间阻塞（这是一种很常见的情况，例如生产者消费者模式
中就存在这样的情况），就没办法及时唤醒它，或者永远都无法唤醒该线程，而interrupt设计之初就是把
wait等长期阻塞作为一种特殊情况考虑在内了，我们应该用interrupt思维来停止线程。
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
![线程的六种状态](https://github.com/CyS2020/Concurrent-Java/blob/master/src/main/resources/%E7%BA%BF%E7%A8%8B%E7%9A%846%E4%B8%AA%E7%8A%B6%E6%80%81.png?raw=true)
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
- sleep方法可以让线程进入Timed_Waiting状态，并且不占用CPU资源，但是不释放锁，直到规定时间后再执行，
休眠期期间如果被中断，会抛出异常清除中断状态
#### wait和sleep方法的异同
- 相同
  - wait和sleep方法都可以使线程阻塞，对应线程状态是Waiting或Time_Waiting。
  - wait和sleep方法都可以响应中断Thread.interrupt()。
- 不同
  - wait方法的执行必须在同步方法中进行，而sleep则不需要。
  - 在同步方法里执行sleep方法时，不会释放monitor锁，但是wait方法会释放monitor锁。
  - sleep方法短暂休眠之后会主动退出阻塞，而没有指定时间的 wait方法则需要被其他线程中断后才能退出阻塞。
  - wait()和notify()，notifyAll()是Object类的方法，sleep()和yield()是Thread类的方法
#### join
- 作用：因为新的线程加入了我们，所以我们要等他执行完再出发
- 用法：main等待thread1执行完毕，注意谁等谁，在join期间线程主线程是Waiting状态
- CountDownLatch或CyclicBarrier来代替join，自己不要操作底层方法，使用成熟的工具类
#### yield
- 作用:释放我的CPU时间片，JVM不保证遵循yield原则，实际开发中不使用yield
- yield和sleep区别：是否随时可能再次被调度

### 六、线程各个属性
#### 编号(ID)
- 每个线程有自己的ID，用于标识不同的线程，从1开始自增
#### 名称(Name)
- 让用户或者开发、调试或运行过程中，更容易区分每个不同的线程，定位问题等
#### 守护线程(isDaemon)
- true代表该线程是【守护线程】，false代表非守护线程，也就是【用户线程】
- 作用：给用户线程提供服务，main线程是非守护线程，不需要我们设置守护线程
- 特性：线程类型默认继承自父线程，守护线程都是由JVM自动启动，不影响JVM退出
#### 优先级(Priority)
- 优先级这个属性的目的是告诉线程调度器，用户希望哪些线程相对多运行，哪些少运行
- 10个级别，默认为5，由线程创建的子线程也会继承当前优先级(若不指定则也是5)
- 程序设计不应该依赖于优先级，因为操作系统不一样，优先级会被操作系统修改

### 七、线程异常处理
#### 两种解决方案
- 方案一(不推荐)：手动在每个run方法里进行try catch
- 方案二(推荐)：利用UncaughtExceptionHandler，在主线程中捕获
#### UncaughtExceptionHandler
- 主线程可以轻松发现异常，子线程却不行，主线程捕获子线程的异常
- void uncaughtException(Thread t, Throwable e);

### 八、线程安全问题
#### 什么是线程安全
- 当多个线程访问一个对象时，如果不用考虑这些线程在运行时环境下的调度和交替执行，也不需要进行
额外的同步，或者在调用方进行任何其他的协调操作，调用这个对象的行为都可以获得正确的结果，那这
个对象是线程安全的
- 不管业务中遇到怎样的多个线程访问某对象或某方法的情况，而在编程这个业务逻辑的时候，都不需要
额外做任何额外的处理(也就是可以像单线程编程一样)，程序也可以正常运行(不会因为多线程而出错)，
就可以成为线程安全
- 相反，如果在编程的时候，需要考虑这些线程在运行时的调度和交替(例如在get()调用到期间不能调用
set())，或者需要进行额外的同步(比如使用synchronized关键字等)，那么就是线程不安全的。
#### 线程安全场景
- 运行结果错误：a++多线程下出现消失的请求现象
- 活跃性问题：死锁、活锁、饥饿
- 对象发布和初始化的时候的安全问题
#### 有哪些典型的对象“逸出”的情况？
- 方法返回一个private对象（private的本意是不让外部访问）-- 使用副本代替真身
- 还未完成初始化（构造函数没完全执行完毕）就把对象提供给外界，比如：-- 工厂模式解决
  - 在构造函数中未初始化完毕就this赋值
  - 隐式逸出——注册监听事件
  - 构造函数中运行线程
#### 尤其需要考虑线程安全的情况有哪些？
- 访问共享的变量或资源，会有并发风险，比如对象的属性、静态变量、共享缓存、数据库等
- 所有依赖时序的操作，即使每一步操作都是线程安全的，还是存在并发问题
- 不同的数据之间存在捆绑关系的时候
- 我们使用其他类的时候，如果对方没有声明自己是线程安全的，那么大概率会存在并发问题
#### 多线程会带来性能问题
- 调度：上下文切换
- 协作：内存同步

### 第二部分-底层原理
#### 从Java代码到CPU指令的变化过程？
- 我们在Java代码中，使用的控制并发的手段例如synchronized关键字，最终也是要转化为CPU指令来生效的，我们来回顾一下从Java代码到最终执行的CPU指令的流程：
  - 最开始，我们编写的Java代码，是*.java文件
  - 在编译（javac命令）后，从刚才的*.java文件会变出一个新的Java字节码文件（*.class）
  - JVM会执行刚才生成的字节码文件（*.class），并把字节码文件转化为机器指令
  - 机器指令可以直接在CPU上运行，也就是最终的程序执行
- 而不同的JVM实现会带来不同的“翻译”，不同的CPU平台的机器指令又千差万别；所以我们在java代码层写的各种Lock，其实最后依赖的是JVM的具体实现（不同版本会有不同实现）和CPU的指令，才能帮我们达到线程安全的效果。由于最终效果依赖处理器，不同处理器结果不一样，这样无法保证并发安全，所以需要一个标准，让多线程运行的结果可预期，这个标准就是JMM。
#### 概念辨析
- JVM内存结构：和Java虚拟机的运行时区域有关
- Java内存模型：和Java的并发编程有关--JMM
- Java对象模型：和Java对象在虚拟机中的表现形式有关
#### java内存模型
- JMM是一组规范，需要各个JVM的实现来遵守，以便开发者可以利用规范更方便的开发多线程程序
- JMM是工具类和关键字的原理，volatile、synchronized、Lock等的原理都是JMM
- JMM最重要：重排序、可见性、原子性
#### 重排序的3种情况分别是什么？
- 代码与指令的对于关系 a = 3: Load a -> Set to 3 -> Store a
- 编译器优化：<br/>
  编译器（包括JVM，JIT编译器等）出于优化的目的（例如当前有了数据a，那么如果把对a的操作放到一起效率会更高，避免了读取b后又返回来重新读取a的时间开销），在编译的过程中会进行一定程度的重排，导致生成的机器指令和之前的字节码的顺序不一致。
在刚才的例子中，编译器将y=a和b=1这两行语句换了顺序（也可能是线程2的两行换了顺序，同理），因为它们之间没有数据依赖关系，那就不难得到 x =0，y = 0 这种结果了。
- 指令重排序：<br/>
  CPU 的优化行为，和编译器优化很类似，是通过乱序执行的技术，来提高执行效率。所以就算编译器不发生重排，CPU 也可能对指令进行重排，所以我们开发中，一定要考虑到重排序带来的后果。
- 内存的"重排序"：<br/>
  内存系统内不存在重排序，但是内存会带来看上去和重排序一样的效果，所以这里的“重排序”打了双引号。由于内存有缓存的存在，在JMM里表现为主存和本地内存，由于主存和本地内存的不一致，会使得程序表现出乱序的行为。
在刚才的例子中，假设没编译器重排和指令重排，但是如果发生了内存缓存不一致，也可能导致同样的情况：线程1 修改了 a 的值，但是修改后并没有写回主存，所以线程2是看不到刚才线程1对a的修改的，所以线程2看到a还是等于0。同理，线程2对b的赋值操作也可能由于没及时写回主存，导致线程1看不到刚才线程2的修改。
#### 可见性
- 为什么会有可见性问题
  - RAM -> L3 cache -> L2 cache -> L1 cache -> registers -> core1 
  - 容量变小速度加快，多级缓存导致读的数据过期
  - 如何所有核心都只用一个缓存，那么也就不存在内存可见性问题
  - 每个核心都会将自己需要的数据读到独占缓存中，数据修改后也是写入到独占缓存中，然后等待刷到主存中。
  所以会导致有些核心读取的值是一个过期的值
#### 什么是JMM里面的主内存和本地内存？
- Java 作为高级语言，屏蔽了CPU多层缓存这些底层细节，用 JMM 定义了一套读写内存数据的规范，虽然我们不再需要关心一级缓存和二级缓存的问题，但是，JMM 抽象了主内存和本地内存的概念。
- 这里说的本地内存并不是真的是一块给每个线程分配的内存，而是 JMM 的一个抽象，是对于寄存器、一级缓存、二级缓存等的抽象。<br/>
<img src="https://github.com/CyS2020/Concurrent-Java/blob/master/src/main/resources/%E4%B8%BB%E5%86%85%E5%AD%98%E5%92%8C%E5%B7%A5%E4%BD%9C%E5%86%85%E5%AD%981.png" width = "400" height = "300" alt="主内存和本地内存的图示" align=center /><br/>
#### JMM有以下规定:
- 所有的变量都存储在主内存中，同时每个线程也有自己独立的工作内存，工作内存中的变量内容是主内存中的拷贝
- 线程不能直接读写主内存中的变量,而是只能操作自己工作内存中的变量，然后再同步到主内存中
- 主内存是多个线程共享的，但线程间不共享工作内存,如果线程间需要通信，必须借助主内存中转来完成
- 所有的共享变量存在于主内存中，每个线程有自己的本地内存，而且线程读写共享数据也是通过本地内存交换的，所以才导致了可见性问题。<br/>
<img src="https://github.com/CyS2020/Concurrent-Java/blob/master/src/main/resources/%E4%B8%BB%E5%86%85%E5%AD%98%E5%92%8C%E5%B7%A5%E4%BD%9C%E5%86%85%E5%AD%982.png" width = "600" height = "300" alt="主内存和本地内存的图示2" align=center /><br/>
#### Happens-Before原则
- happens-before规则是用来解决可见性问题的：在时间上，动作A发生在B之前，B保证能看见A，这就是happens-before
- 两个操作可以用happens-before来确定它们的执行顺序：如果一个操作happens-before于另一个操作，那么我们说第一个操作对于第二个操作是可见的。
- 不是happens-before：两个线程没有互相配合的机制，所以代码X和Y的执行结果并不能保证总被对方看到，这就不具备happens-before
#### Happens-Before规则有哪些
- 1. 单线程规则
- 2. 锁操作(synchronized和Lock)
- 3. volatile变量：近朱者赤，给b加了volatile不仅b被影响，也可以实现轻量级同步
- 4. 线程start
- 5. 线程join
- 6. 传递性
- 7. 中断
- 8. 构造方法
- 9. 工具类的Happens-Before原则
  - 1. 线程安全的容器get一定能看到再次之前的put等存入动作
  - 2. CountDownLatch
  - 3. Semaphore
  - 4. Future
  - 5. 线程池
  - 6. CyclicBarrier
#### volatile
- 什么是volatile：是一种同步机制，比synchronized或者Lock相关类更轻量，因为使用volatile并不会发生上下文切换等开销很大的行为<br/>
如果一个变量被修饰成volatile，那么JVM就知道了这个变量可能会被并发修改<br/>
开销小，能力也小，虽说volatile是用来同步的保证线程安全的，但是volatile做不到synchronized那样的原子保护
- 不适用场景：a++ 操作
- 适用场景1：boolean flag，如果一个变量自始至终只被各个线程赋值，而没有其他操作那么就可以用volatile代替synchronized或者原子变量<br/>
因为赋值自身是有原子性的，而volatile又保证了可见性，所以就足以保证线程安全；赋值不取决于之前的状态
- 适用场景2：作为刷新之前变量的触发器
- 两点作用：
  - 可见性：读取一个volatile变量之前，需要先使相应的本地缓存失效，这样就必须到主内存读取最新值，写一个volatile属性会立即刷入到主内存
  - 禁止指令重排序优化：解决单例模式双重锁乱序问题
#### volatile和synchronized关系
- volatile在这方面可以看做是轻量版的synchronized：如果一个共享变量自始至终只被各个线程赋值，而没有其他操作，那么就可以用volatile来代替synchronized或代替原子变量，因为赋值自身是有原子性的，而volatile又保证了可见性，所以就足以保证线程安全
- synchronized不仅保证了原子性还保证了可见性
#### 原子性
- 一系列操作，要么全部执行成功，要么全部不执行，不会出现执行一半的情况
- 除了long和double之外的基本类型(int,byte,boolean,short,char,float)的赋值操作
- 所有引用reference的赋值操作，不管是32位的机器还是64位机器
- java.concurrent.Atomic.* 包中所有类的原子操作
- 在32位上的JVM上，long和double的操作不是原子的，但是在64位的JVM上是原子的，商用JVM保证原子性
- 简单地把原子操作组合在一起，并不能保证整体依然具有原子性
#### 单例模式
- 为什么需要单例模式：节省内存和计算、保证结果正确、方便管理
- 单例模式使用场景：
  - 1. 无状态的工具类：比如日志工具类，不管是在哪里使用我们需要的知识它帮我们记录日志信息，除此之外，并不需要它的实例对象上存储任何状态，这时候我们就只需要一个实例对象即可。
  - 2. 全局信息类：比如我们在类上记录网站的访问次数，我们不希望有的访问被记录在对象A上有的记录在对象B上，这时候我们就让这个类成为单例
- 双重检查+volatile：创建对象=给对象分配内存 -> 调用构造方法初始化成员变量 -> 引用赋值(会进行重排序造成NPE)
- 不同写法对比：
  - 饿汉：简单，但是没有lazy loading
  - 懒汉：有线程安全问题；场景：一开始要加载的资源太多，那么就应该使用懒加载
  - 静态内部类：可用
  - 双重检查：面试用
  - 枚举：最好；线程安全还可以避免反序列化破坏单例
### 第三部分-死锁
#### 什么是死锁
- 发生在并发中
- 互不相让：当两个(或更多)的线程(或进程)相互持有对方所需要的资源，又不主动释放，导致所有人都无法继续前进，导致程序陷入无尽的阻塞
- 多个线程造成死锁，形成环路的依赖关系
#### 死锁发生必要条件
- 互斥条件
- 请求与保持条件
- 不剥夺条件
- 循环等待条件
