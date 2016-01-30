package com.chenjj.concurrent.threadManager;

import java.util.concurrent.TimeUnit;

public class FileClockMain {

	public static void main(String[] args) {
		FileClock fileClock = new FileClock();
		Thread thread = new Thread(fileClock);
		thread.start();
		try {
			TimeUnit.SECONDS.sleep(5);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		thread.interrupt();
	}

}
