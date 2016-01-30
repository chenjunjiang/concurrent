package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class ThreadLocalMain {

	public static void main(String[] args) {
		/*UnsafeTask task = new UnsafeTask();
		for (int i = 0; i < 10; i++) {
			// 这里的线程会共享UnsafeTask里面的成员变量
			Thread thread = new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}*/
		SafeTask task = new SafeTask();
		for (int i = 0; i < 10; i++) {
			Thread thread = new Thread(task);
			thread.start();
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
