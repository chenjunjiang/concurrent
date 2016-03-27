package com.chenjj.concurrent.threadSync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrintQueue {
	private final Lock queueLock = new ReentrantLock();// 可重入锁

	public void printJob(Object document) {
		queueLock.lock();// 获取Lock对象的控制权
		long duration = (long) (Math.random() * 10000);
		System.out.println(Thread.currentThread().getName() + ":PrintQueue: Printing a Job during " + (duration / 1000)
				+ " seconds");
		try {
			Thread.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			queueLock.unlock();//释放Lock对象的控制
		}
	}
}
