package com.chenjj.concurrent.threadManager;

public class ThreadFactoryMain {

	public static void main(String[] args) {
		MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");
		ThreadFactoryTask task = new ThreadFactoryTask();
		System.out.printf("Starting the Threads\n");
		Thread thread;
		for (int i = 0; i < 10; i++) {
			thread = factory.newThread(task);
			thread.start();
		}
		System.out.printf("Factory stats:\n");
		System.out.printf("%s\n", factory.getStats());
	}

}
