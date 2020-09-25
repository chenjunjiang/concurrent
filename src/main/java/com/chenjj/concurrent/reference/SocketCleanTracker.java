package com.chenjj.concurrent.reference;

import java.lang.ref.ReferenceQueue;
import java.util.concurrent.TimeUnit;

public class SocketCleanTracker {
    private static final ReferenceQueue<Socket> queue = new ReferenceQueue<>();
    // 对PhantomReference对象的引用(tracker)(Tracker继承了PhantomReference)必须保证socket被垃圾回收的时候还存在，否则tracker将不会被放入到队列中
    private static Tracker tracker;
    static {
        new Cleaner().start();
    }

    public static void track(Socket socket) {
        tracker = new Tracker(socket, queue);
        // 这种方式在track方法结束后，tracker也就被回收了，当socket被垃圾回收的时候它已经不存在了，就不能被放到queue里面了，后面从queue里面肯定是取不到数据的
        // Tracker tracker = new Tracker(socket, queue);
    }

    private static class Cleaner extends Thread {
        public Cleaner() {
            super("SocketCleanTracker");
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                for (; ; ) {
                    System.out.println("从queue中获取数据......");
                    Tracker tracker = (Tracker) queue.poll();
                    if (tracker != null) {
                        tracker.close();
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
