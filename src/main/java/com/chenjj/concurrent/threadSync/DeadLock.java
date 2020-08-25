package com.chenjj.concurrent.threadSync;

/**
 *会出现死锁的例子
 */
public class DeadLock {
    private final Object MUTEX_READ = new Object();
    private final Object MUTEX_WRITE = new Object();

    public void read() {
        synchronized (MUTEX_READ) {
            System.out.println(Thread.currentThread().getName() + " get READ lock");
            synchronized (MUTEX_WRITE) {
                System.out.println(Thread.currentThread().getName() + " get WRITE lock");
            }
            System.out.println(Thread.currentThread().getName() + " release WRITE lock");
        }
        System.out.println(Thread.currentThread().getName() + " release READ lock");
    }

    public void write() {
        synchronized (MUTEX_WRITE) {
            System.out.println(Thread.currentThread().getName() + " get WRITE lock");
            synchronized (MUTEX_READ) {
                System.out.println(Thread.currentThread().getName() + " get READ lock");
            }
            System.out.println(Thread.currentThread().getName() + " release READ lock");
        }
        System.out.println(Thread.currentThread().getName() + " release WRITE lock");
    }

    public static void main(String[] args) {
        final DeadLock deadLock = new DeadLock();
        new Thread(() -> {
            while (true) {
                deadLock.read();
            }
        }, "READ_THREAD").start();

        new Thread(() -> {
            while (true) {
                deadLock.write();
            }
        }, "WRITE_THREAD").start();
    }
}
