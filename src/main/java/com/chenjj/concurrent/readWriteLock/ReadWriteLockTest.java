package com.chenjj.concurrent.readWriteLock;

/**
 * 我们自己实现的读写锁存在一定的缺陷和需要补强的地方，比如：可以结合BooleanLock的方式增加超时的功能，提供查询
 * 哪些线程被陷入阻塞的方法，判断当前线程是否被某个lock锁定等
 */
public class ReadWriteLockTest {
    private final static String text = "Thisistheexampleforreadwritelock";

    public static void main(String[] args) {
        final ShareData shareData = new ShareData(50);
        // 创建两个线程进行写操作
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int index = 0; index < text.length(); index++) {
                    char c = text.charAt(index);
                    try {
                        shareData.write(c);
                        System.out.println(Thread.currentThread() + " write " + c);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

        // 创建10个线程进行读操作
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                while (true) {
                    try {
                        System.out.println(Thread.currentThread() + " read " + new String(shareData.read()));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
