package com.chenjj.concurrent.threadPool;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicThreadPool implements ThreadPool {
    // 初始化线程数量
    private final int initSize;
    // 线程池最大线程数量
    private final int maxSize;
    // 线程池核心线程数量
    private final int coreSize;
    // 当前活跃的线程数量
    private int activeCount;
    // 创建线程所需的工厂
    private final ThreadFactory threadFactory;
    // 任务队列
    private final RunnableQueue runnableQueue;
    // 线程池是否已经被shutdown
    private volatile boolean isShutdown = false;
    // 工作线程队列
    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();

    private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();

    private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    private MaintainCountThread maintainCountThread;

    private final Object monitor = new Object();

    public BasicThreadPool(int initSize, int maxSize, int coreSize, int queueSize, long keepAliveTime) {
        this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY, queueSize, DEFAULT_DENY_POLICY, keepAliveTime, TimeUnit.SECONDS);
    }

    public BasicThreadPool(int initSize, int maxSize, int coreSize, int queueSize) {
        this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY, queueSize, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
    }

    public BasicThreadPool(int initSize, int maxSize, int coreSize, ThreadFactory threadFactory,
                           int queueSize, DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    /**
     * 初始化时，先创建initSize个线程
     */
    private void init() {
        maintainCountThread = new MaintainCountThread();
        maintainCountThread.start();
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    @Override
    public void execute(Runnable runnable) {
        if (this.isShutdown) {
            throw new IllegalStateException("the thread pool is destroy.");
        }
        this.runnableQueue.offer(runnable);
    }

    /**
     * 线程池的销毁同样需要同步机制的保护，主要是为了防止在线程池线程数量的维护过程中(MaintainCountThread)出现数据不一致的问题
     */
    @Override
    public void shutdown() {
        synchronized (monitor) {
            if (isShutdown) {
                return;
            }
            isShutdown = true;
            threadQueue.forEach(threadTask -> {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });
            maintainCountThread.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        if (isShutdown) {
            throw new IllegalStateException("The thread pool is destroy.");
        }
        return this.initSize;
    }

    @Override
    public int getMaxSize() {
        if (isShutdown) {
            throw new IllegalStateException("The thread pool is destroy.");
        }
        return this.maxSize;
    }

    @Override
    public int getCoreSize() {
        if (isShutdown) {
            throw new IllegalStateException("The thread pool is destroy.");
        }
        return this.coreSize;
    }

    @Override
    public int getQueueSize() {
        if (isShutdown) {
            throw new IllegalStateException("The thread pool is destroy.");
        }
        return runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        synchronized (this) {
            return this.activeCount;
        }
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    /**
     * 创建任务线程并且启动
     */
    private void newThread() {
        InternalTask internalTask = new InternalTask(runnableQueue);
        Thread thread = this.threadFactory.createThread(internalTask);
        ThreadTask threadTask = new ThreadTask(thread, internalTask);
        threadQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }

    /**
     * 从线程池中删除某个线程
     */
    private void removeThread() {
        ThreadTask threadTask = threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }

    /**
     * InternalTask和Thread的组合
     */
    private static class ThreadTask {
        Thread thread;
        InternalTask internalTask;

        public ThreadTask(Thread thread, InternalTask internalTask) {
            this.thread = thread;
            this.internalTask = internalTask;
        }
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger GROUP_COUNTER = new AtomicInteger(1);
        private static final ThreadGroup group = new ThreadGroup("MyThreadGroup-" + GROUP_COUNTER.getAndDecrement());
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group, runnable, "thread-pool-" + COUNTER.getAndDecrement());
        }
    }

    /**
     * 维护线程池线程数量的线程
     */
    private class MaintainCountThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown && !isInterrupted()) {
                try {
                    timeUnit.sleep(keepAliveTime);
                } catch (InterruptedException e) {
                    isShutdown = true;
                    break;
                }
                // 这里使用同步块的目的是为了防止在线程池线程数量的维护过程中如果发生了线程池销毁操作，可能会引起数据不一致的问题。
                synchronized (monitor) {
                    if (isShutdown) {
                        break;
                    }
                    // 当前任务队列中还有任务尚未处理，并且activeCount<coreSize则线程池需要扩容
                    if (runnableQueue.size() > 0 && activeCount < coreSize) {
                        for (int i = initSize; i < coreSize; i++) {
                            newThread();
                        }
                        // 不再执行while循环体中continue语句之后的代码，直接进行下一次循环，这样就不执行下面的代码，以免扩容后的线程数直接就达到maxSize
                        continue;
                    }
                    // 当前任务队列中还有任务尚未处理，并且activeCount<maxSize则线程池需要继续扩容
                    if (runnableQueue.size() > 0 && activeCount < maxSize) {
                        for (int i = coreSize; i < maxSize; i++) {
                            newThread();
                        }
                    }
                    // 如果任务队列中没有任务了，则需要回收线程，回收至coreSize即可
                    if (runnableQueue.size() == 0 && activeCount > coreSize) {
                        for (int i = coreSize; i < activeCount; i++) {
                            removeThread();
                        }
                    }
                }
            }
        }
    }

}
