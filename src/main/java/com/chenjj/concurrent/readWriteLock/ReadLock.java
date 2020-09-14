package com.chenjj.concurrent.readWriteLock;

public class ReadLock implements Lock {
    private final ReadWriteLockImpl readWriteLock;

    public ReadLock(ReadWriteLockImpl readWriteLock) {
        this.readWriteLock = readWriteLock;
    }

    @Override
    public void lock() throws InterruptedException {
        synchronized (readWriteLock.getMUTEX()) {
            /**
             *若此时有线程在进行写操作，或者有写线程在等待并且偏向被设置为true，那么就无法获得读锁，只能等待
             */
            while (readWriteLock.getWritingWriters() > 0 || (readWriteLock.getPreferWriter()
                    && readWriteLock.getWaitingWriters() > 0)) {
                readWriteLock.getMUTEX().wait();
            }
            // 成功获得读锁，并且让readingReaders增加
            readWriteLock.incrementReadingReaders();
        }
    }

    @Override
    public void unlock() {
        synchronized (readWriteLock.getMUTEX()) {
            readWriteLock.decrementReadingReaders();
            // 将preferWriter设置为true，可以使得writer线程获得更多的机会
            readWriteLock.changePrefer(true);
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}
