package com.chenjj.concurrent.readWriteLock;

/**
 * 包可见，创建的时候使用ReadWriteLock的create方法
 * 虽然我们在开发一个读写锁，但是在实现内部还是需要一个锁进行数据同步以及线程之前的通讯，MUTEX的作用就在于此，
 * 而preferWriter的作用在于控制倾向性，一般来说读写锁非常适用于读多写少的场景，如果preferWriter为false，
 * 很多读线程都在读数据，那么写线程就很难得到写的机会
 */
class ReadWriteLockImpl implements ReadWriteLock {
    // 定义对象锁
    private final Object MUTEX = new Object();
    // 当前多少线程正在执行写入操作
    private int writingWriters = 0;
    // 当前多少线程正在等待写锁
    private int waitingWriters = 0;
    // 当前多少线程正在进行read操作
    private int readingReaders = 0;
    // read和write的偏好设置
    private boolean preferWriter;

    public ReadWriteLockImpl() {
        this(true);
    }

    public ReadWriteLockImpl(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }

    @Override
    public Lock readLock() {
        return new ReadLock(this);
    }

    @Override
    public Lock writeLock() {
        return new WriteLock(this);
    }

    @Override
    public int getWritingWriters() {
        return this.writingWriters;
    }

    @Override
    public int getWaitingWriters() {
        return this.waitingWriters;
    }

    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }

    /**
     * 增加写线程数量
     */
    void incrementWritingWriters() {
        this.writingWriters++;
    }

    /**
     * 增加等待写锁的线程数量
     */
    void incrementWaitingWriters() {
        this.waitingWriters++;
    }

    /**
     * 增加读线程数量
     */
    void incrementReadingReaders() {
        this.readingReaders++;
    }

    /**
     * 减少写线程数量
     */
    void decrementWritingWriters() {
        this.writingWriters--;
    }

    /**
     * 减少等待写线程数量
     */
    void decrementWaitingWriters() {
        this.waitingWriters--;
    }

    /**
     * 减少读线程数量
     */
    void decrementReadingReaders() {
        this.readingReaders--;
    }

    Object getMUTEX() {
        return this.MUTEX;
    }

    boolean getPreferWriter() {
        return this.preferWriter;
    }

    void changePrefer(boolean preferWriter) {
        this.preferWriter = preferWriter;
    }
}
