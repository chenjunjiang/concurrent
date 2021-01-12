package com.chenjj.concurrent.falseShare;

/**
 * 如果要测试伪共享请注释掉public long p1, p2, p3, p4, p5;
 * 如果要测试通过缓存行来解决伪共享，请放开public long p1, p2, p3, p4, p5;
 * <p>
 * 代码的逻辑很简单，就是三个线程修改一数组不同元素的内容。元素的类型是 VolatileLong，
 * 只有一个长整型成员 value 和 6 个没用到的长整型成员。value 设为 volatile 是为了让 value 的修改对
 * 所有线程都可见。程序分两种情况执行，第一种情况为不屏蔽倒数第三行（见"屏蔽此行"字样），
 * 第二种情况为屏蔽倒数第三行。为了"保证"数据的相对可靠性，程序取 10 次执行的平均时间。
 * 两个逻辑一模一样的程序，前者的耗时大概是后者的 2.5 倍，这太不可思议了！那么这个时候，我们再用伪共享
 * （False Sharing）的理论来分析一下。前者 longs 数组的 4 个元素，由于 VolatileLong 只有 1 个长整型成员，
 * 所以整个数组都将被加载至同一缓存行，但有4个线程同时操作这条缓存行，于是伪共享就悄悄地发生了。
 *
 * 我们知道一条缓存行有 64 字节，而 Java 程序的对象头固定占 8 字节(32位系统)或 12 字节
 * ( 64 位系统默认开启压缩, 不开压缩为 16 字节)，所以我们只需要填 5个无用的长整型补上5*8=40字节，
 * 让不同的 VolatileLong 对象处于不同的缓存行，就避免了伪共享( 64 位系统超过缓存行的 64 字节也无所谓，
 * 只要保证不同线程不操作同一缓存行就可以)。(我的测试机器是64位系统)
 */
public class FalseShareTest implements Runnable {
    public static int NUM_THREADS = 3;
    public final static long ITERATIONS = 500L * 1000L * 100L;
    // public final static long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;
    private static VolatileLong[] longs;
    public static long SUM_TIME = 0l;

    public FalseShareTest(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        for (int j = 0; j < 10; j++) {
            System.out.println(j);
            if (args.length == 1) {
                NUM_THREADS = Integer.parseInt(args[0]);
            }
            longs = new VolatileLong[NUM_THREADS];
            for (int i = 0; i < longs.length; i++) {
                longs[i] = new VolatileLong();
            }
            final long start = System.nanoTime();
            runTest();
            final long end = System.nanoTime();
            SUM_TIME += end - start;
        }
        System.out.println("平均耗时：" + SUM_TIME / 10);
    }

    private static void runTest() throws InterruptedException {
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(new FalseShareTest(i));
        }
        for (Thread t : threads) {
            t.start();
        }
        for (Thread t : threads) {
            t.join();
        }
    }

    @Override
    public void run() {
        long i = ITERATIONS + 1;
        while (0 != --i) {
            longs[arrayIndex].value = i;
        }
    }

    /**
     * 64 位系统超过缓存行的 64 字节也无所谓，只要保证不同线程不操作同一缓存行就可以
     */
    public final static class VolatileLong {
        public volatile long value = 0L;
        // 缓存行填充（Padding）
        public long p1, p2, p3, p4, p5;
    }

    /**
     * 由于某些 Java 编译器的优化策略，那些没有使用到的补齐数据可能会在编译期间被优化掉，
     * 我们可以在程序中加入一些代码防止被编译优化。
     * 因为我们在该方法中使用到了补齐变量而且还把它们的和作为返回值，所以不会被编译优化。
     *
     * @param v
     * @return
     */
    public static long preventFromOptimization(VolatileLong v) {
        return v.p1 + v.p2 + v.p3 + v.p4 + v.p5;
    }
}
