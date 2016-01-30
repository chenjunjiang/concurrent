package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class ExceptionTaskMain {

	public static void main(String[] args) {
		ExceptionTask task = new ExceptionTask();
		Thread thread = new Thread(task);
		thread.setUncaughtExceptionHandler(new ExceptionHandler());
		thread.start();
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Main is over");
	}

}
