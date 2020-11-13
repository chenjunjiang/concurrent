Thread的stackSize与-Xss参数都可以控制某个线程的栈内存大小，它们的区别你知道吗？当这两个配置同时存在时，以哪个为准？

Thread的 stackSize 是什么？
在Thread的构造器中可以传入stackSize参数。如果不传的话，默认是0。它的作用是控制jvm给线程分配栈内存的大小。
stackSize与栈深度（stack height,就是方法内调方法的套嵌层数）和同时存在的线程数的关系是与JVM平台相关的，
有些平台这个参数无效。具体怎么实现由JVM决定。
在HostSpotVM中，值较大时可能会加大线程内栈的深度；值较小时可能加大同时存在的线程数，以避免出现OutOfMemoryError（或者其他Error）
如果这个值比JVM规定的最小值还小的话，取JVM的默认值，一般是1M
这个值我尝试过最大的设置为2G-1，栈深度达到1.3亿才溢出，所以说我没有找到这个值的上限，但是我觉得一个线程分配2G已经是很变态了。
当然栈深度也与栈帧的大小有关，栈深度=stackSize/平均栈帧大小。

JVM参数-Xss
JVM的-Xss参数也是控制栈内存大小的参数

区别
-Xss是全局的，也就是所有线程共同的设置，当不设置时有默认值，一般为1M。而stackSize是某个线程的属性，只作用在某个线程上。
当设置stackSize属于<=0 时，以-Xss为准（一般设置为0即可，java提供的DefaultThreadFactory就是设置的0）。
当设置stackSize属于(0, 4k]区间时，设置的-Xss会失效，栈空间取默认值1M。这一点特别容易搞错！
当设置stackSize属于(4k, 64k]区间时，设置的-Xss会失效，栈空间取4k。
当设置stackSize属于(64k, 128k]区间时，设置的-Xss会失效，栈空间取64k。
当设置stackSize属于 >128k 时，设置的-Xss会失效，栈空间取stackSize本身
总之，如果 stackSize<=0 ，JVM会认为你没有设置 stackSize，所以以 -Xss 为准。
如果 stackSize > 0, JVM 会以 stackSize 为准， 而忽略 -Xss参数。只不过JVM会对stackSize作处理，当小于等于4k时，认为太小了，用默认值（1M）代替，另外也做了一些梯度处理。

注意：
stackSize 参数，如果没有特殊的需求，尽量不要修改，因为它本身的作用和范围取决于平台，在跨平台迁移时，需要检查是否需要修改这个参数。
stackSize 用于精细地控制线程数和栈深度的关系，也是有用武之地的。比如有些算法用递归实现，栈深度较大。
本文中的单个栈内存大小的默认值在不同JVM平台上可能会有所不同。
测试代码如下：

public class JVMStackTest {
    int count = 0;

    /**
     * java.lang.StackOverflowError
     * stack height:18562
     *
     * @author Shuaijun He
     */
    public void testStack() {
        this.count++;
        this.testStack();
    }

    /**
     * 增加参数
     * java.lang.StackOverflowError
     * stack height:17478
     *
     * @author Shuaijun He
     * @param a
     * @param b
     */
    public void testStack(int a, int b) {
        this.count++;
        this.testStack(a, b);
    }

    /**
     * 增加局部变量 数量
     * java.lang.StackOverflowError
     * stack height:7845
     *
     * -Xss=100k 时 stack height:665
     *
     * @author Shuaijun He
     * @param a
     * @param b
     */
    public void testStackWithLocalVar(int a, int b) {
        int c = 5;
        long d = 4L;
        System.out.println(c + d);
        this.count++;
        this.testStackWithLocalVar(a, b);
    }

    /**
     * Thread.stackSize = 2g-1 时 stack height:134191969
     * Thread.stackSize = 128m 时 stack height:8204399 (此行往上，删掉System.out.println(c + d);否则太慢了)
     * Thread.stackSize = 4m 时 stack height:32408
     * Thread.stackSize = 2m 时 stack height:16028
     * Thread.stackSize = 1.5m 时 stack height:11936
     * Thread.stackSize = 1m+1 时 stack height:8353
     * Thread.stackSize = 1m 时 stack height:7833
     * Thread.stackSize = 512k+1 时 stack height:4242
     * Thread.stackSize = 512k 时 stack height:3738
     * Thread.stackSize = 256k 时 stack height:1687 （设置 -Xss512k， stack height值不变）
     * Thread.stackSize = 128k+1 时 stack height:1168
     *
     * Thread.stackSize = 128k 时 stack height:661
     * Thread.stackSize = 112k 时 stack height:660
     * Thread.stackSize = 96k 时 stack height:656
     * Thread.stackSize = 80k 时 stack height:669
     * Thread.stackSize = 72k 时 stack height:659
     * Thread.stackSize = 64k+1 时 stack height:662
     *
     * Thread.stackSize = 64k 时 stack height:156
     * Thread.stackSize = 32k 时 stack height:150
     * Thread.stackSize = 16k 时 stack height:156
     * Thread.stackSize = 8k 时 stack height:150
     * Thread.stackSize = 5k 时 stack height:156
     * Thread.stackSize = 4k+1 时 stack height:146
     *
     * Thread.stackSize = 4k 时 stack height:7837
     * Thread.stackSize = 2k 时 stack height:7838
     * Thread.stackSize = 1k 时 stack height:7831
     * Thread.stackSize = 1 时 stack height:7836  （设置 -Xss512k， stack height= 7834）
     *
     * Thread.stackSize = 0 时 stack height:7839  （设置 -Xss512k， stack height= 3737）
     * Thread.stackSize = -1 时 stack height:7835 （设置 -Xss512k， stack height= 3737）
     * 总结：
     * stackSize <= 4k 会忽略此参数，取默认值 1m
     * stackSize 属于 (4k, 64k] 取4k
     * stackSize 属于 (64k, 128k] 取64k
     * stackSize 属于 (128k, Max) 取stackSize。没找到上限！
     * 注意：同时设置 -Xss 参数和 Thread.stackSize > 0时，以Thread.stackSize为准；Thread.stackSize <=0 时, 以 -Xss 为准
     *
     */
    public static void testMyThreadStackSize(){
        // 设置 Thread.stackSize = 100k
        MyThreadFactory.getMyThreadFactory().newThread(()->{
            JVMStackTest test = new JVMStackTest();
            try {
                test.testStackWithLocalVar(1, 2);
            } catch (Throwable e) {
                System.out.println(e);
                System.out.println("stack height: " + test.count);
                try {
                    Field stackSizeField = Thread.class.getDeclaredField("stackSize");
                    stackSizeField.setAccessible(true);
                    long stackSize = stackSizeField.getLong(Thread.currentThread());
                    System.out.println("stackSize(kb): " + (stackSize / 1024) + ", " + stackSize);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 测试 -Xss 的影响
     */
    private static void jvmXss(){
        JVMStackTest test = new JVMStackTest();
        try {
//            test.testStack();
//            test.testStack(1, 2);
            test.testStackWithLocalVar(1, 2);
        } catch (Throwable e) {
            System.out.println(e);
            System.out.println("stack height:" + test.count);
        }
    }

    public static void main(String[] args) {

//        jvmXss();
        testMyThreadStackSize();
    }
}

自定义线程工厂：

public class MyThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    private static MyThreadFactory myThreadFactory = new MyThreadFactory();

    private MyThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    public static MyThreadFactory getMyThreadFactory() {
        return myThreadFactory;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                 -1);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }


}