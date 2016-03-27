package com.chenjj.concurrent.threadSyncUtils;

public class PrintQueueMain {

	public static void main(String[] args) {
		PrintQueue printQueue = new PrintQueue();
		Thread[] threads = new Thread[10];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new Job(printQueue), "Thread" + i);
		}
		for (int i = 0; i < threads.length; i++) {
			threads[i].start();
		}
	}

}
