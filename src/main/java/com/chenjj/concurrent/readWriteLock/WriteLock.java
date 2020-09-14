package com.chenjj.concurrent.readWriteLock;

public class WriteLock implements Lock {
    private final ReadWriteLockImpl readWriteLock;

    public WriteLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (readWriteLock.getMUTEX()) {
            try {
                readWriteLock.incrementWaitingWriters();
                // 如果此时有其它线程在进行读或写操作，那么当前线程挂起
                if (readWriteLock.getReadingReaders() > 0 || readWriteLock.getWritingWriters() > 0) {
                    readWriteLock.getMUTEX().wait();
                }
            } finally {
                // 成功获取到了写锁后让等待获取写锁的线程数减一
                readWriteLock.decrementWaitingWriters();
            }
            // 成功获取到了写锁后将正在进行写操作的线程数加一
            readWriteLock.incrementWritingWriters();
        }
    }

    // 这种方式在没有获取对象关联的monitor锁的情况下调用notifyAll方法，会报：java.lang.IllegalMonitorStateException
    /*@Override
    public void unlock() {
        readWriteLock.decrementWritingWriters();
        // 将偏好状态修改为false，可以使得读锁被快速获得
        readWriteLock.changePrefer(false);
        readWriteLock.getMUTEX().notifyAll();
    }*/

    @Override
    public void unlock() {
        synchronized (readWriteLock.getMUTEX()) {
            readWriteLock.decrementWritingWriters();
            // 将偏好状态修改为false，可以使得读锁被快速获得
            readWriteLock.changePrefer(false);
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}
