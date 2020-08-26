package com.chenjj.concurrent.threadSync;

import java.util.concurrent.TimeUnit;

/**
 * 定义了5个线程调用accessResource方法，由于同步代码块的互斥性，只能有一个线程获取了mutex monitor锁，其它线程
 * 只能进入阻塞状态，运行jconsole工具后可以看到线程的状态：
 * 名称: Thread-0
 * 状态: TIMED_WAITING
 * 总阻止数: 0, 总等待数: 1
 *
 * 堆栈跟踪:
 * java.lang.Thread.sleep(Native Method)
 * java.lang.Thread.sleep(Thread.java:340)
 * java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
 * com.chenjj.concurrent.threadSync.Mutex.accessResource(Mutex.java:11)
 *    - 已锁定 java.lang.Object@3e7f227b
 * com.chenjj.concurrent.threadSync.Mutex$$Lambda$1/290658609.run(Unknown Source)
 * java.lang.Thread.run(Thread.java:748)
 *
 * 名称: Thread-1
 * 状态: java.lang.Object@3e7f227b上的BLOCKED, 拥有者: Thread-0
 * 总阻止数: 1, 总等待数: 0
 *
 * 堆栈跟踪:
 * com.chenjj.concurrent.threadSync.Mutex.accessResource(Mutex.java:11)
 * com.chenjj.concurrent.threadSync.Mutex$$Lambda$1/290658609.run(Unknown Source)
 * java.lang.Thread.run(Thread.java:748)
 *
 * 从结果可以看出， 只有一个线程状态为TIMED_WAITING，它获得锁后在sleep，其它都是BLOCKED。
 * 还可以通过jstack 14772 > mutex.txt查看：
 * "Thread-0" #13 prio=5 os_prio=0 tid=0x00000000192d1000 nid=0x22e8 waiting on condition [0x000000001a5df000]
 *    java.lang.Thread.State: TIMED_WAITING (sleeping)
 * 	at java.lang.Thread.sleep(Native Method)
 * 	at java.lang.Thread.sleep(Thread.java:340)
 * 	at java.util.concurrent.TimeUnit.sleep(TimeUnit.java:386)
 * 	at com.chenjj.concurrent.threadSync.Mutex.accessResource(Mutex.java:11)
 * 	- locked <0x00000000d8d2fe90> (a java.lang.Object)
 * 	at com.chenjj.concurrent.threadSync.Mutex$$Lambda$1/290658609.run(Unknown Source)
 * 	at java.lang.Thread.run(Thread.java:748)
 * 	Thread-0状态为TIMED_WAITING，它获得锁后在sleep，其它都是BLOCKED：
 * 	"Thread-1" #14 prio=5 os_prio=0 tid=0x00000000192d2000 nid=0x3414 waiting for monitor entry [0x000000001a6df000]
 *    java.lang.Thread.State: BLOCKED (on object monitor)
 * 	at com.chenjj.concurrent.threadSync.Mutex.accessResource(Mutex.java:11)
 * 	- waiting to lock <0x00000000d8d2fe90> (a java.lang.Object)
 * 	at com.chenjj.concurrent.threadSync.Mutex$$Lambda$1/290658609.run(Unknown Source)
 * 	at java.lang.Thread.run(Thread.java:748)
 *
 * 	JVM指令分析
 * 	使用JDK命令javap对Mutex class进行反汇编，要在classes下执行
 * 	（D:\workspace\concurrent\target\classes\com\chenjj\concurrent\threadSync>javap -c Mutex）
 * 	，输出了大量的JVM指令，在这些指令中，你将发现monitor enter和monitor exit是成对出现的（有些时候会出现
 * 	一个monitor enter和多个monitor exit，但是每一个monitor exit之前必有相应的monitor enter）：
 * 	1、monitor enter
 * 	每个对象都与一个monitor关联，一个monitor的lock只能被一个线程在同一时间获得，在一个线程尝试获得与对象关联
 * 	monitor的所有权时会发生如下几件事情：
 * 	a、如果monitor的计数器为0，则意味着该monitor的lock还没有被获得，某个线程获得之后就立即将该计数器加一，从此
 * 	该线程就是这个monitor的所有者了。
 * 	b、如果一个已经拥有该monitor所有权的线程重入，则会导致monitor计数器再次累加。
 * 	c、如果monitor已经被其它线程所拥有， 则其它线程尝试获取该monitor的所有权时，会被陷入阻塞状态直到monitor计数器
 * 	变为0，才能再次尝试获取对monitor的所有权。
 * 	2、monitor exit
 * 	释放对monitor的所有权，想要释放对某个对象关联的monitor的所有权的前提是，你曾经获得了所有权。释放monitor所有权的过程
 * 	比较简单，就是将monitor的计数器减一，如果计数器的结果为0，那就意味着该线程不再拥有对该monitor的所有权，通俗地讲就是解锁。
 * 	与此同时被该monitor block的线程将再次尝试获得该monitor的所有权。
 *
 *
 */
public class Mutex {
    private final static Object MUTEX = new Object();

    public void accessResource() {
        synchronized (MUTEX) {
            try {
                TimeUnit.MINUTES.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final Mutex mutex = new Mutex();
        for (int i = 0; i < 5; i++) {
            new Thread(mutex::accessResource).start();
        }
    }
}
