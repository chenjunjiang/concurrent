package com.chenjj.concurrent.threadSync;

import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者与消费者之间共享的缓冲区
 * 
 * @author Administrator
 *
 */
public class Buffer {
	private LinkedList<String> buffer;// 存储共享数据
	private int maxSize;// 缓冲区长度
	private ReentrantLock lock;// 控制缓冲区代码块的访问
	private Condition lines;
	private Condition space;
	private boolean pendingLines;// 缓冲区中是否有行

	public Buffer(int maxSize) {
		this.maxSize = maxSize;
		buffer = new LinkedList<String>();
		lock = new ReentrantLock();
		lines = lock.newCondition();
		space = lock.newCondition();
		pendingLines = false;
	}

	public void insert(String line) {
		lock.lock();
		try {
			while (buffer.size() == maxSize) {
				/**
				 * 如果缓冲区已满，它将调用await()方法在space条件上等待释放空间。
				 * 如果其他线程在space条件上调用signal()或 signalAll()方法，这个线程将被唤醒。
				 */
				space.await();// 调用之后锁自动释放
			}
			buffer.offer(line);
			System.out.printf("%s: Inserted Line: %d\n", Thread.currentThread().getName(), buffer.size());
			lines.signalAll();// 唤醒其它在lines条件上等待的线程
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public String get() {
		String line = null;
		lock.lock();
		try {
			while ((buffer.size() == 0) && (hasPendingLines())) {
				lines.await();
			}
			if (hasPendingLines()) {
				line = buffer.poll();
				System.out.printf("%s: Line Readed: %d\n", Thread.currentThread().getName(), buffer.size());
				space.signalAll();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}

		return line;
	}

	public void setPendingLines(boolean pendingLines) {
		this.pendingLines = pendingLines;
	}

	public boolean hasPendingLines() {
		return pendingLines || buffer.size() > 0;
	}
}
