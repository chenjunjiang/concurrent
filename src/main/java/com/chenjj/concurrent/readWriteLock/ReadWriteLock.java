package com.chenjj.concurrent.readWriteLock;

/**
 * ReadWriteLock并不是Lock，只是用来创建Lock
 */
public interface ReadWriteLock {
    // 创建读锁
    Lock readLock();

    // 创建写锁
    Lock writeLock();

    // 获取当前有多少线程正在执行写操作
    int getWritingWriters();

    // 获取当前有多少线程正在等待写入锁
    int getWaitingWriters();

    // 获取当前有多少线程正在执行读操作
    int getReadingReaders();

    /**
     * 接口中的静态方法和类中定义的静态方法一样，不属于特定对象，所以它们不是实现接口的api的一部分，
     * 必须使用InterfaceName.staticMethod来调用它们。Java8新增的特性
     *
     * @return
     */
    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }

    static ReadWriteLock readWriteLock(boolean preferWriter) {
        return new ReadWriteLockImpl(preferWriter);
    }
}
