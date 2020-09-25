package com.chenjj.concurrent.reference;

import java.io.IOException;
import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SocketCleanTrackerTest {
    private static final List<Object> TEST_DATA = new LinkedList<>();
    private static final ReferenceQueue<Socket> QUEUE = new ReferenceQueue<>();

    public static void main(String[] args) throws InterruptedException {
        Socket socket = new Socket();
        try {
            socket.close();
        } catch (IOException e) {
            SocketCleanTracker.track(socket);
        }
        //PhantomReference<Socket> phantomReference = new PhantomReference<>(socket, QUEUE);
        // Tracker tracker =new Tracker(socket, QUEUE);
        // 该线程不断读取这个虚引用，并不断往列表里插入数据，以促使系统早点进行GC
        new Thread(() -> {
            while (true) {
                TEST_DATA.add(new byte[1024 * 1024 * 1]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
                // System.out.println(phantomReference.get());
            }
        }).start();

        // 这个线程不断读取引用队列，当弱引用指向的对象呗回收时，该引用就会被加入到引用队列中
       /* new Thread(() -> {
            try {
                for (; ; ) {
                    System.out.println("从queue中获取数据......");
                    Tracker tracker1 = (Tracker) QUEUE.poll();
                    if (tracker1 != null) {
                        tracker1.close();
                    }
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();*/

        socket = null;

        Thread.currentThread().join();
    }
}
