# concurrent
1、JVM程序什么情况下会退出？
   在正常情况下，若JVM中没有了一个非守护线程，则JVM的进程会退出。当然System.exit()这种非正常情况也会退出。
JVM进程退出的时候，所有线程都要退出。
   如果是JVM进程 crash了（例如说发生了不可恢复的SIGSEGV），那这个进程肯定是彻底退出了，渣都不会剩。里面的线程自然也都要全部死掉。
如果是被调用了System.exit(int) / Runtime.getRuntime().exit(int)，那么是Java程序主动要求彻底退出程序。
这种情况下所有Java线程也要都退出。
如果不是以上两种情况，则程序要跑到Java的入口方法main()结束后自然退出。
这种情况下就有很多好玩的事情了。如果到主线程跑完入口方法main()的时候，系统中还存在其它non-daemon线程，
那么JVM要一直等到这些non-daemon线程也跑完才结束。而如果剩下的都是daemon线程了，则JVM不会等待它们跑完而直接可以退出。

2、yield 和 sleep 的异同
   yield 即 "谦让"，也是 Thread 类的方法。它让掉当前线程 CPU 的时间片，使正在运行中的线程重新变成就绪状态，
   并重新竞争CPU的调度权。它可能会获取到，也有可能被其他线程获取到。
   a、yield,sleep都能暂停当前线程，sleep 可以指定具体休眠的时间，而 yield 则依赖 CPU 的时间片划分。
   b、yield,sleep两个在暂停过程中，如已经持有锁，则都不会释放锁资源。
   c、yield不能被中断，而sleep则可以接受中断。
   
3、异常退出
   在一个线程的执行单元中，是不允许抛出checked异常的，不论Thread的run方法还是Runnable的run方法，如果线程运行
过程中需要捕获checked异常并且判断是否还有运行下去的必要，那么此时可以将checked异常封装成unchecked异常抛出进而结束
线程的生命周期。

4、进程假死
   所谓假死就是进程虽然存在，但没有日志输出，程序不进行任何作业，看起来就像死了一样，但事实上它没有死，程序之所以出现这样的
情况，绝大部分原因就是某个线程阻塞了，或者线程出现了死锁的情况。可以借助jvisualvm这类工具来诊断。

5、synchronized
   synchronized关键字提供了一种锁的机制，能够确保共享变量的互斥访问，从而防止数据不一致问题的出现。
synchronized关键字包括monitor enter和monitor exit两个JVM指令，它能够保证在任何时候任何线程执行到
monitor enter成功之前都必须从主内存中获取数据，而不是从缓存中，在monitor exit运行成功之后，共享变量被
更新后的值必须刷入主内存。synchronized的指令严格遵守java happens-before原则，一个monitor exit之前必定
要有一个monitor enter。
   synchronized可以用于对代码块或方法继续修饰，而不能够用于对class以及变量进行修饰。
   很多地方将synchronized(mutex)称为锁，其实这种说法是不严谨的，准确地讲应该是某个线程获取了与mutex关联的monitor的锁。
   关于它的详细解释可以参考Mutex类。
   使用synchronized需要注意的问题：
   a、与monitor关联的对象不能为空
   b、synchronized的作用域不能太大，作用越越大，代表其效率越低，甚至还会丧失并发的优势。
   c、线程之间进行monitor lock的争抢只能发生在与monitor关联的同一个对象(引用)上。
   d、多个锁的交叉很容易引起线程出现死锁的情况，程序并没有任何错误输出，但就是不工作。
   一个线程可以获取多个不同对象关联的monitor的lock。
   
6、程序死锁的原因以及如何诊断
   a、交叉锁可导致程序出现死锁
   线程A持有R1的锁等待获取R2的锁，线程B持有R2的锁等待获取R1的锁，这种情况最容易导致程序发生死锁。
   b、内存不足
   当并发请求系统可用内存时，如果此时系统内存不足，则可能会出现死锁的情况。比如：两个线程T1和T2，执行某个任务，其中T1已经获取
了10M内存，T2获取了20M内存，如果每个线程的执行单元都需要30M的内存，但是剩余的内存刚好为20M，那么两个线程有可能都在等待彼此能够释放内存资源。
   c、一问一答式的数据交换
   服务开启某个端口，等待客户端访问，客户端发送请求立即等待接收，由于某种原因服务端错过了客户端的请求，仍然在等待一问一答式
的数据交换，此时服务端和客户端都在等待双方发送数据。
   d、数据库锁
   无论是数据库表级别的锁，还是行级别的锁，比如某个线程执行了for update语句意外退出了事务，其它线程访问数据库时将陷入死锁。
   e、文件锁
   某个线程获得了文件锁意外退出，其它读取文件的线程也将会进入死锁直到系统释放文件句柄资源。
   f、死循环引起的死锁
   程序由于代码原因或者对某些异常处理不恰当，进入死循环，虽然查看线程栈信息不会发现任何死锁的现象，但是程序不工作，CPU占有率
居高不下，这种死锁一般称为系统假死，是一种最为致命也是最难排查的死锁现象，由于重现困难，进程对系统资源的使用量又达到了极限，
想要做出dump有时候也是非常困难的。 严格意义上来说，它算不上真正的死锁，但是某个线程对CPU消耗过多，会导致其它线程
等待CPU，内存等资源也会陷入死锁等待。

7、关于wait和notify的注意事项
   a、wait方法是可中断方法，这也就意味着，当前线程一旦调用了wait方法进入阻塞状态，其它线程是可以使用interrupt方法将其
打断的，可中断方法被打断后会收到中断异常，同时interrupt标识也会被擦除。
   b、线程执行了某个对象的wait方法以后，会加入与之对应的wait set中，每一个对象的monitor都有一个与之关联的wait set。
   c、当线程进入wait set之后，notify方法可以将其唤醒，也就是从wait set中弹出；还有，中断wait中的线程也会将其唤醒。
   d、必须在同步方法中使用wait和notify方法，因为执行wait和notify的前提是必须持有同步方法的monitor的所有权。
   e、同步代码的对象必须与执行wait和notify方法的对象一致。
   
8、线程休息室wait set
   在虚拟机规范中存在一个wait set(又称线程休息室)的概念，至于该wait set是怎样的数据结构，JDK官方并没有给出明确的定义，不同厂家
的JDK有着不同的实现方式，甚至相同的JDK厂家不同的版本也存在着差异，但是不管怎样，线程调用某个对象的wait方法之后都会被加入到与该对象
的monitor关联的wait set中，并且释放monitor的所有权。若干个线程调用了wait方法之后被加入到与对象的monitor关联的wait set中，待
另一个线程调用该monitor的notify方法之后，其中一个线程会从wait set中弹出，至于弹出的先后顺序，虚拟机规范也没有给出强制的要求。
而执行了notifyAll方法则不需要考虑哪个线程会被弹出，wait set中的所有wait线程都会被弹出。

9、synchronized的缺陷
   synchronized提供了一种排他式的数据同步机制，某个线程在获取monitor lock的时候可能会被阻塞，而这种阻塞有两个明显
的缺陷：第一，无法控制阻塞时长，第二，阻塞不可被中断。

10、ThreadGroup的interrupt
    interrupt一个thread group会导致该group中所有的active线程都被interrupt，也就是说该group中每一个线程的interrupt
标识都被设置了。

11、ThreadGroup的destroy
    destroy用于销毁ThreadGroup，该方法只是针对一个没有任何active线程的group进行一次destroy标记，调用该方法的直接
结果是自己将从父group被移除。

12、守护ThreadGroup
    线程可以设置为守护线程，ThreadGroup也可以设置为守护ThreadGroup，但是若将一个ThreadGroup设置为deamon，也并不会
影响线程的deamon属性，如果一个ThreadGroup的deamon被设置为true，那么在group中没有任何active线程的时候该group将自动
destroy。

13、获取线程运行时异常
    线程在执行单元中是不允许抛出checked异常的，而且线程运行在自己的上下文中，派生它的线程将无法直接获得它运行中出现的
异常信息。Java为我们提供了一个UncaughtExceptionHandler接口，当线程运行过程中出现异常时，会回调UncaughtExceptionHandler，，
从而得知是哪个线程在运行时出错，以及出现了什么样的错误。

14、Hook(钩子)线程
    JVM进程的退出是由于JVM进程中没有了活跃的非守护线程，或者收到了系统的中断信号。向JVM程序注入一个Hook线程，在JVM退出的时候，
Hook线程会启动执行，通过Runtime可以为JVM注入多个Hook线程。
    Hook线程使用场景及注意事项：
    a、Hook线程只有在接收到退出信号的时候会被执行，如果在kill的时候使用了参数-9，那么Hook线程不会得到执行，进程将会立即
退出。
    b、Hook线程中可以执行一些资源释放的工作，比如关闭文件句柄，socket链接，数据库connection等。
    c、尽量不要在Hook线程中执行一些耗时非常长的操作，因为会导致程序迟迟不能退出。

