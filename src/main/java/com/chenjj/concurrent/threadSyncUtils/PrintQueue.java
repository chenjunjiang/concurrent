package com.chenjj.concurrent.threadSyncUtils;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class PrintQueue {
	private final Semaphore semaphore;

	public PrintQueue() {
		// 初始化参数时1,说明调用semaphore.acquire()的时候只有一个线程能获得临界区的访问权
		// 因为此时计数器的值等于1,某一个线程获得访问权之后会把计数器的值减一
		this.semaphore = new Semaphore(1);
	}

	public void printJob(Object document) {
		try {
			semaphore.acquire();// 获取访问共享资源的权利
			long duration = (long) (Math.random() * 10);
			System.out.printf("%s: PrintQueue: Printing a Job during %d seconds\n", Thread.currentThread().getName(),
					duration);
			TimeUnit.SECONDS.sleep(duration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			semaphore.release();// 释放访问共享资源的权利
		}
	}
}
