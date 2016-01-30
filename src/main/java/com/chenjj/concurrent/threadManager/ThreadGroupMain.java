package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class ThreadGroupMain {

	public static void main(String[] args) {
		ThreadGroup threadGroup = new ThreadGroup("Searcher");
		Result result = new Result();
		SearchTask searchTask = new SearchTask(result);
		for (int i = 0; i < 5; i++) {
			Thread thread = new Thread(threadGroup, searchTask);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.printf("Number of Threads: %d\n", threadGroup.activeCount());
		System.out.printf("Information about the Thread Group:\n");
		threadGroup.list();
		Thread[] threads = new Thread[threadGroup.activeCount()];
		// 复制该线程组及其子组中的所有活动线程到指定的数组
		int count = threadGroup.enumerate(threads);
		for (int i = 0; i < count; i++) {
			System.out.printf("Thread %s: %s\n", threads[i].getName(), threads[i].getState());
		}
		waitFinish(threadGroup);
		threadGroup.interrupt();// 中断线程组里面的所有线程
	}

	private static void waitFinish(ThreadGroup threadGroup) {
		while (threadGroup.activeCount() > 4) {
			System.out.println("还有" + threadGroup.activeCount() + "个线程处于活动状态!");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
