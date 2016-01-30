package com.chenjj.concurrent.threadManager;

public class ThreadGroupExceptionTaskMain {

	public static void main(String[] args) {
		MyThreadGroup threadGroup = new MyThreadGroup("MyThreadGroup");
		ThreadGroupExceptionTask task = new ThreadGroupExceptionTask();
		for (int i = 0; i < 2; i++) {
			Thread thread = new Thread(threadGroup, task);
			thread.start();
		}
	}

}
